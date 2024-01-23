package work.szczepanskimichal.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import work.szczepanskimichal.userservice.entity.UserDAO;
import work.szczepanskimichal.userservice.entity.UserDto;
import work.szczepanskimichal.userservice.entity.UserUpdateDto;
import work.szczepanskimichal.userservice.enums.Type;
import work.szczepanskimichal.userservice.exception.*;
import work.szczepanskimichal.userservice.mapper.UserMapper;
import work.szczepanskimichal.userservice.repository.UserRepository;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto createUser(UserDto userDto) {
        log.info("initiating user creation for email: {}", userDto.getEmail());
        validateUserFields(userDto);
        var createdDto = userMapper.toDto(userRepository.save(userMapper.toEntity(userDto)));
        log.info("successfully created user. email: {}", userDto.getId());
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
            return userMapper.toDto(userOptional.get());
        }
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
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
            return userMapper.toDto(userRepository.save(updatedUser));
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
        var EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        var pattern = Pattern.compile(EMAIL_REGEX);
        var matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
