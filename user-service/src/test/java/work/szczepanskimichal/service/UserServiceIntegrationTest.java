package work.szczepanskimichal.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import work.szczepanskimichal.entity.User;
import work.szczepanskimichal.entity.UserDto;
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
        var userDto = UserDto.builder()
                .email("email@email.com")
                .password("pass")
                .passwordConfirmation("pass")
                .type(Type.USER)
                .build();

        //when
        var result = userService.createUser(userDto);

        //then
        assertNotNull(result.getId());
        assertEquals(Type.USER, result.getType());
    }

    @Test
    void shouldThrowPasswordMismatchException_onUserCreation() {
        //given
        var userDto = UserDto.builder()
                .email("email@email.com")
                .password("pass")
                .passwordConfirmation("passss")
                .type(Type.USER)
                .build();

        //when-then
        assertThrows(PasswordMismatchException.class, () -> userService.createUser(userDto));
    }

    @Test
    void shouldThrowMissingFieldException_onUserCreation() {
        //given
        var userDto = UserDto.builder()
                .email(null)
                .password("pass")
                .passwordConfirmation("pass")
                .type(Type.USER)
                .build();

        //when-then
        assertThrows(MissingFieldException.class, () -> userService.createUser(userDto));
    }

    @Test
    void shouldThrowInvalidEmailException_onUserCreation() {
        //given
        var userDto = UserDto.builder()
                .email("email#email.com")
                .password("pass")
                .passwordConfirmation("pass")
                .type(Type.USER)
                .build();

        //when-then
        assertThrows(InvalidEmailException.class, () -> userService.createUser(userDto));
    }

    @Test
    void shouldThrowIAdminProgrammaticCreationException_onUserCreation() {
        //given
        var userDto = UserDto.builder()
                .email("email@email.com")
                .password("pass")
                .passwordConfirmation("pass")
                .type(Type.ADMIN)
                .build();

        //when-then
        assertThrows(AdminProgrammaticCreationException.class, () -> userService.createUser(userDto));
    }

    @Test
    void shouldReturnTestUser() {
        // given
        var user = User.builder()
                .email("test@example.com")
                .password("password")
                .phoneNumber("1234567890")
                .active(true)
                .type(Type.USER)
                .build();
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
        var user1 = User.builder()
                .email("test@example1.com")
                .password("password")
                .phoneNumber("1234567890")
                .active(true)
                .type(Type.USER)
                .build();
        userRepository.save(user1);
        var user2 = User.builder()
                .email("test@example2.com")
                .password("password")
                .phoneNumber("1234567890")
                .active(true)
                .type(Type.USER)
                .build();
        userRepository.save(user2);

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
                .active(true)
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
        assertEquals(result.getPassword(), newPassword);
    }

    @Test
    void shouldDeleteUser() {
        //given
        var user = User.builder()
                .email("test@example.com")
                .password("password")
                .phoneNumber("1234567890")
                .active(true)
                .type(Type.USER)
                .build();
        var persistedUser = userRepository.save(user);

        //when
        userService.deleteUser(persistedUser.getId());

        //then
        assertTrue(userRepository.findById(persistedUser.getId()).isEmpty());
    }
}