package work.szczepanskimichal.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import work.szczepanskimichal.entity.User;
import work.szczepanskimichal.entity.UserCreateDto;
import work.szczepanskimichal.entity.UserDto;
import work.szczepanskimichal.entity.UserUpdateDto;
import work.szczepanskimichal.exception.*;
import work.szczepanskimichal.mapper.UserMapper;
import work.szczepanskimichal.repository.UserRepository;
import work.szczepanskimichal.enums.Type;
import work.szczepanskimichal.utils.ValidationUtil;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ValidationUtil validationUtil;
    private final ActivationKeyService activationKeyService;

    public UserDto createUser(UserCreateDto userCreateDto) {
        log.info("initiating user creation for email: {}", userCreateDto.getEmail());
        if (userRepository.userWithEmailExists(userCreateDto.getEmail()) > 0) {
            throw new EmailDuplicationException(userCreateDto.getEmail());
        }
        validateUserFields(userCreateDto);
        var createdDto = userMapper.toUserDto(userRepository.save(userMapper.toEntity(userCreateDto)));
        activationKeyService.assignActivationKeyToUser(createdDto.getId(), createdDto.getEmail());
        log.info("successfully created user. user id: {}", createdDto.getId());
        return createdDto;
    }

    @Transactional
    public int activateUser(UUID userId, UUID activationKey) {
        activationKeyService.findByActivationKeyAndUserIdAndDelete(activationKey, userId);
        return userRepository.activateUser(userId);
    }

    public UserDto getUser(UUID userId) {
        if (userId == null) {
            throw new MissingFieldException("user id");
        }
        var user = userExists(userId);
        return userMapper.toUserDto(user);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserDto)
                .toList();
    }

    public UserDto updateUser(UUID userId, UserUpdateDto userUpdateDto) {
        var user = userExists(userId);
        validateType(userUpdateDto.getType());
        if (!user.getEmail().equals(userUpdateDto.getEmail()) && isEmailAvailable(userUpdateDto.getEmail())) {
            activationKeyService.assignActivationKeyToUser(userId, userUpdateDto.getEmail());
        }
        validateEmail(userUpdateDto.getEmail());
        var updatedUser =
                userMapper.toEntity(userUpdateDto).toBuilder()
                        .id(userId)
                        .active(false)
                        .build();
        return userMapper.toUserDto(userRepository.save(updatedUser));
    }

//    public UserCreateDto updatePassword(UUID userId, UserUpdatePasswordDto userUpdateDto) {
//    }

    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
        log.info("successfully deleted user with id: {}", userId);
    }

    private User userExists(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    private void validateUserFields(UserCreateDto userDto) {
        validateType(userDto.getType());
        validateEmail(userDto.getEmail());
        validatePasswords(userDto.getPassword(), userDto.getPasswordConfirmation());
    }

    private void validateType(Type type) {
        if (type == null) {
            throw new MissingFieldException("user type");
        }
        if (type.equals(Type.ADMIN)) {
            throw new AdminProgrammaticCreationException();
        }
    }

    private void validatePasswords(String password, String passwordConfirmation) {
        if (password == null || password.isEmpty() || passwordConfirmation == null || passwordConfirmation.isEmpty()) {
            throw new MissingFieldException("user password");
        }
        if (!password.equals(passwordConfirmation)) {
            throw new PasswordMismatchException();
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            log.error("missing field: email");
            throw new MissingFieldException("user email");
        }
        if (!isEmailValid(email)) {
            throw new InvalidEmailException();
        }
    }

    private boolean isEmailAvailable(String email) {
        if (userRepository.userWithEmailExists(email) > 0) {
            throw new EmailDuplicationException(email);
        }
        return true;
    }

    private boolean isEmailValid(String email) {
        var pattern = Pattern.compile(validationUtil.getEmailRegex());
        var matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
