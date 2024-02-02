package work.szczepanskimichal.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import work.szczepanskimichal.entity.User;
import work.szczepanskimichal.exception.AdminProgrammaticCreationException;
import work.szczepanskimichal.exception.InvalidEmailException;
import work.szczepanskimichal.exception.PasswordMismatchException;
import work.szczepanskimichal.mapper.UserMapper;
import work.szczepanskimichal.repository.UserRepository;
import work.szczepanskimichal.enums.Type;
import work.szczepanskimichal.exception.MissingFieldException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UserServiceIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

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
        assertEquals(Type.USER, result.getType());
    }

    @Test
    void shouldActivateUser() {
        //given
        var userDto = UserAssembler.assembleRandomUserDto();
        var user = userService.createUser(userDto);

        //when
        var changedRows = userService.activateUser(user.getId());

        //then
        assertTrue(changedRows > 0);
        var isActive = userRepository.findById(user.getId()).get().isActive();
        assertTrue(isActive);
        assertEquals(Type.USER, user.getType());
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
        var corruptedUserDto = userDto.toBuilder().email("not_an_email").build();

        //when-then
        assertThrows(InvalidEmailException.class, () -> userService.createUser(corruptedUserDto));
    }

    @Test
    void shouldThrowIAdminProgrammaticCreationException_onUserCreation() {
        var userDto = UserAssembler.assembleRandomUserDto();
        var corruptedUserDto = userDto.toBuilder().type(Type.ADMIN).build();

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
    void shouldUpdateUser() {
        //given
        var oldPhoneNumber = "1234567890";
        var user = User.builder()
                .email("test@example.com")
                .password("password")
                .phoneNumber(oldPhoneNumber)
                .type(Type.USER)
                .build();
        var persistedUser = userRepository.save(user);

        //when
        var newPhoneNumber = "0987654321";
        var newPassword = "pass";
        var updatedUser = userMapper.toUserUpdateDto(persistedUser).toBuilder()
                .password(newPassword)
                .passwordConfirmation(newPassword)
                .phoneNumber(newPhoneNumber)
                .build();
        var result = userService.updateUser(persistedUser.getId(), updatedUser);

        //then
        assertEquals(result.getPhoneNumber(), newPhoneNumber);
        assertEquals(result.getPassword(), userMapper.hashPassword(newPassword));
        var isActive = userRepository.findById(user.getId()).get().isActive();
        assertFalse(isActive);
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