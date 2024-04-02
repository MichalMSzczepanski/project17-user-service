package work.szczepanskimichal.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import work.szczepanskimichal.model.key.KeyType;
import work.szczepanskimichal.model.user.UserType;
import work.szczepanskimichal.model.user.User;
import work.szczepanskimichal.model.user.dto.UserUpdatePasswordDto;
import work.szczepanskimichal.exception.*;
import work.szczepanskimichal.mapper.UserMapper;
import work.szczepanskimichal.repository.SecretKeyRepository;
import work.szczepanskimichal.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Testcontainers
@EmbeddedKafka(partitions = 1, topics = {"NOTIFICATION_TOPIC"})
class UserServiceIntegrationTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SecretKeyRepository secretKeyRepository;
    @Autowired
    private HashingService hashingService;

    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @DynamicPropertySource
    static void mongoDbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeAll
    public static void setUp() {
        mongoDBContainer.start();
    }

    @AfterAll
    public static void tearDown() {
        mongoDBContainer.stop();
    }

    @Test
    void shouldCreateUser() {
        //given
        var userDto = UserAssembler.assembleRandomUserDto();

        //when
        var result = userService.createUser(userDto);

        //then
        var isActive = userRepository.findById(result.getId()).get().isActive();
        assertFalse(isActive);
        assertNotNull(result.getId());
        assertEquals(UserType.USER, result.getUserType());
    }

    @Test
    void shouldActivateUser() {
        //given
        var userDto = UserAssembler.assembleRandomUserDto();
        var user = userService.createUser(userDto);
        var secretKeyOptional = secretKeyRepository.getKeyByUserIdAndKeyType(user.getId(), KeyType.USER_CREATION);
        var secretKey = secretKeyOptional.get();

        //when
        userService.activateUser(user.getId(), secretKey.getKey());

        //then
        var isActive = userRepository.findById(user.getId()).get().isActive();
        assertTrue(isActive);
        assertEquals(UserType.USER, user.getUserType());
        var secretKeyAfterActivation = secretKeyRepository.getKeyByUserIdAndKeyType(user.getId(),
                KeyType.USER_CREATION);
        assertTrue(secretKeyAfterActivation.isEmpty());
    }

    @Test
    void shouldThrowPasswordMismatchException_onUserCreation() {
        //given
        var userDto = UserAssembler.assembleRandomUserDto();
        var corruptedUserDto = userDto.toBuilder().password("wrong_password").build();

        //when-then
        assertThrows(PasswordMismatchException.class, () -> userService.createUser(corruptedUserDto));
    }

    @Test
    void shouldThrowEmailDuplicationException_onUserCreation() {
        //given
        var userDto = UserAssembler.assembleRandomUserDto();
        userRepository.save(userMapper.toEntity(userDto));

        //when-then
        assertThrows(EmailDuplicationException.class, () -> userService.createUser(userDto));
    }

    @Test
    void shouldThrowMissingFieldException_onUserCreation() {
        //given
        var userDto = UserAssembler.assembleRandomUserDto();
        var corruptedUserDto = userDto.toBuilder().email(null).build();

        //when-then
        assertThrows(MissingFieldException.class, () -> userService.createUser(corruptedUserDto));
    }

    @Test
    void shouldThrowInvalidEmailException_onUserCreation() {
        //given
        var userDto = UserAssembler.assembleRandomUserDto();
        var corruptedUserDto = userDto.toBuilder().email("invalid&email.com").build();

        //when-then
        assertThrows(InvalidEmailException.class, () -> userService.createUser(corruptedUserDto));
    }

    @Test
    void shouldThrowIAdminProgrammaticCreationException_onUserCreation() {
        var userDto = UserAssembler.assembleRandomUserDto();
        var corruptedUserDto = userDto.toBuilder().userType(UserType.ADMIN).build();

        //when-then
        assertThrows(AdminProgrammaticCreationException.class, () -> userService.createUser(corruptedUserDto));
    }

    @Test
    void shouldReturnTestUser() {
        // given
        var user = UserAssembler.assembleRandomUser();
        var userId = userRepository.save(user).getId();

        // when
        var result = userService.getUser(userId);

        // then
        assertEquals(userId, result.getId());
        assertTrue(user.isActive());
    }

    @Test
    void shouldReturnAllUsers() {
        //given
        userRepository.save(UserAssembler.assembleRandomUser());
        userRepository.save(UserAssembler.assembleRandomUser());

        //when
        var result = userService.getAllUsers();

        //then
        assertEquals(2, result.size());
    }

    @Test
    void shouldUpdateUsersPhoneNumber() {
        //given
        var oldPhoneNumber = "1234567890";
        var user = User.builder()
                .email("test@example.com")
                .password("password")
                .phoneNumber(oldPhoneNumber)
                .userType(UserType.USER)
                .build();
        var persistedUser = userRepository.save(user);

        //when
        var newPhoneNumber = "0987654321";
        var updatedUser = userMapper.toUserUpdateDto(persistedUser).toBuilder()
                .phoneNumber(newPhoneNumber)
                .build();
        var result = userService.updateUser(persistedUser.getId(), updatedUser);

        //then
        assertEquals(result.getPhoneNumber(), newPhoneNumber);
        var isActive = userRepository.findById(user.getId()).get().isActive();
        assertFalse(isActive);
    }

    @Test
    void shouldUpdateUsersPassword() {
        //given
        var currentPassword = "password";
        var user = UserAssembler.assembleRandomUser();
        user = UserAssembler.hashUserPassword(user, hashingService, currentPassword);
        var persistedUser = userRepository.save(user);

        //when
        var newPassword = "changedPassword";
        var userDtoWithNewPassword = UserUpdatePasswordDto.builder()
                .currentPassword(currentPassword)
                .newPassword(newPassword)
                .newPasswordConfirmation(newPassword)
                .build();
        userService.updatePassword(persistedUser.getEmail(), userDtoWithNewPassword);

        //then
        var newPasswordHashedExplicitly = hashingService.hashPassword(newPassword);
        var userPasswordAfterPasswordUpdate = userRepository.findPasswordByEmail(persistedUser.getEmail()).get();
        assertEquals(newPasswordHashedExplicitly, userPasswordAfterPasswordUpdate);
    }

    @Test
    void shouldNotUpdateUsersPassword_dueToInvalidPasswordException() {
        //given
        var currentPassword = "password";
        var user = UserAssembler.assembleRandomUser();
        user = UserAssembler.hashUserPassword(user, hashingService, currentPassword);
        var persistedUser = userRepository.save(user);
        var email = persistedUser.getEmail();

        //when
        var newPassword = "changedPassword";
        var userDtoWithNewPassword = UserUpdatePasswordDto.builder()
                .currentPassword("currentPasswordWithTypo")
                .newPassword(newPassword)
                .newPasswordConfirmation(newPassword)
                .build();

        //then
        assertThrows(InvalidPasswordException.class, () -> userService.updatePassword(email,
                userDtoWithNewPassword));
    }

    @Test
    void shouldDeleteUser() {
        //given
        var user = userRepository.save(UserAssembler.assembleRandomUser());

        //when
        userService.deleteUser(user.getId());

        //then
        assertTrue(userRepository.findById(user.getId()).isEmpty());
    }
}