package work.szczepanskimichal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import work.szczepanskimichal.entity.UserDAO;
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

    public UserDto createUser(UserDto userDto) {
        log.info("initiating user creation for email: {}", userDto.getEmail());
        validateUserFields(userDto);
        var createdDto = userMapper.toUserDto(userRepository.save(userMapper.toEntity(userDto)));
        log.info("successfully created user. user id: {}", createdDto.getId());
        return createdDto;
    }

    public UserDto getUser(UUID userId) {
        if (userId == null) {
            log.error("missing field: user id");
            throw new MissingFieldException("user id");
        }
        var userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            log.error("user with id: {} not found", userId);
            throw new UserNotFoundException(userId);
        } else {
            return userMapper.toUserDto(userOptional.get());
        }
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserDto)
                .toList();
    }

    public UserDto updateUser(UUID userId, UserUpdateDto userUpdateDto) {
        var userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            log.error("user with id: {} not found", userId);
            throw new UserNotFoundException(userId);
        } else {
            validateUserFields(userUpdateDto);
            var userEntity = userOptional.get();
            var updatedUser =
                    userMapper.toEntity(userUpdateDto).toBuilder()
                            .id(userId)
                            .createdAt(userEntity.getCreatedAt())
                            .build();
            return userMapper.toUserDto(userRepository.save(updatedUser));
        }
    }

    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
        log.info("successfully deleted user with id: {}", userId);
    }

    private void validateUserFields(UserDAO userDto) {
        validateType(userDto);
        validateEmail(userDto.getEmail());
        validatePassword(userDto);
    }

    private void validateType(UserDAO userDto) {
        var type = userDto.getType();
        if (type == null) {
            log.error("missing field: type");
            throw new MissingFieldException("user type");
        }
        if (type.equals(Type.ADMIN)) {
            log.error("attempt to create admin. for user with email: {}",
                    userDto.getEmail());
            throw new AdminProgrammaticCreationException();
        }
    }

    private void validatePassword(UserDAO userDto) {
        var password = userDto.getPassword();
        var passwordConfirmation = userDto.getPasswordConfirmation();
        if (password == null || password.isEmpty() || passwordConfirmation == null || passwordConfirmation.isEmpty()) {
            log.error("missing field: password");
            throw new MissingFieldException("user password");
        }
        if (!userDto.getPassword().equals(userDto.getPasswordConfirmation())) {
            log.error("passwords do not match. attempted by user with email: {}", userDto.getEmail());
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

    private boolean isEmailValid(String email) {
        var pattern = Pattern.compile(validationUtil.getEmailRegex());
        var matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
