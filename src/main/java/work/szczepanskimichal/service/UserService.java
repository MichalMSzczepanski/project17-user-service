package work.szczepanskimichal.service;

import com.mongodb.MongoException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import work.szczepanskimichal.entity.*;
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
    private final HashingService hashingService;

    @Transactional
    public UserDto createUser(UserCreateDto userCreateDto) {
        log.info("Initiating user creation for email: {}", userCreateDto.getEmail());
        if (userRepository.userWithEmailExists(userCreateDto.getEmail()) > 0) {
            throw new EmailDuplicationException(userCreateDto.getEmail());
        }
        validateUserFields(userCreateDto);
        var createdDto = userMapper.toUserDto(userRepository.save(userMapper.toEntity(userCreateDto)));
        try {
            activationKeyService.assignActivationKeyToUser(createdDto.getId(), createdDto.getEmail());
        } catch (MongoException e) {
            userRepository.deleteById(createdDto.getId());
            log.error("Failed to create user due to activation key issue. Rolled back user creation: {}",
                    createdDto.getId());
            throw new ActivationKeyException(e.getMessage());
        }
        log.info("Successfully created user. user id: {}", createdDto.getId());
        return createdDto;
    }

    @Transactional
    public int activateUser(UUID userId, UUID activationKey) {
        try {
            activationKeyService.findByActivationKeyAndUserIdAndDelete(activationKey, userId);
        } catch (MongoException e) {
            log.error("Failed to manage user activation key.", userId);
            throw new ActivationKeyException(e.getMessage());
        }
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

    @Transactional
    public UserDto updateUser(UUID userId, UserUpdateDto userUpdateDto) {
        var user = userExists(userId);
        validateType(userUpdateDto.getType());
        if (!user.getEmail().equals(userUpdateDto.getEmail()) && isEmailAvailable(userUpdateDto.getEmail())) {
            try {
                activationKeyService.assignActivationKeyToUser(userId, userUpdateDto.getEmail());
            } catch (MongoException e) {
                log.error("Failed to update user due to activation key issue. Terminating user update.",
                        userId);
                throw new ActivationKeyException(e.getMessage());
            }
        }
        validateEmail(userUpdateDto.getEmail());
        var updatedUser =
                userMapper.toEntity(userUpdateDto).toBuilder()
                        .id(userId)
                        .active(false)
                        .build();
        return userMapper.toUserDto(userRepository.save(updatedUser));
    }

    @Transactional
    public void updatePassword(UUID userId, UserUpdatePasswordDto userUpdateDto) {
        validatePasswords(userUpdateDto.getNewPassword(), userUpdateDto.getNewPasswordConfirmation());
        var hashedCurrentPassword = userRepository.findPasswordById(userId);
        var hashedConfirmationPassword = hashingService.hashPassword(userUpdateDto.getCurrentPassword());
        if (!hashedCurrentPassword.equals(hashedConfirmationPassword)) {
            throw new InvalidPasswordException();
        }
        userRepository.updatePassword(userId, hashingService.hashPassword(userUpdateDto.getNewPassword()));
    }

    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
        log.info("Successfully deleted user with id: {}", userId);
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
