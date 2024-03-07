package work.szczepanskimichal.service;

import com.mongodb.MongoException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import work.szczepanskimichal.model.key.KeyType;
import work.szczepanskimichal.model.user.UserType;
import work.szczepanskimichal.model.*;
import work.szczepanskimichal.exception.*;
import work.szczepanskimichal.mapper.UserMapper;
import work.szczepanskimichal.model.user.User;
import work.szczepanskimichal.model.user.dto.*;
import work.szczepanskimichal.repository.UserRepository;
import work.szczepanskimichal.util.ValidationUtil;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final LoginResponseService loginResponseService;
    private final SecretKeyService secretKeyService;
    private final HashingService hashingService;
    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final ValidationUtil validationUtil;
    private final UserMapper userMapper;

    public ResponseEntity<LoginResponse> login(UserLoginDto dto) {
        var isUserActive =
                userRepository.isUserActive(dto.getEmail()).orElseThrow(InvalidLoginAttemptException::new);
        if (!isUserActive) {
            throw new UserInactiveException(dto.getEmail());
        }
        var persistedPassword =
                userRepository.findPasswordByEmail(dto.getEmail())
                        .orElseThrow(() -> new UserNotFoundException(dto.getEmail()));
        var passwordsCheckSuccessful =
                persistedPassword.equals(hashingService.hashPassword(dto.getPassword()));
        return loginResponseService.registerLogin(dto, passwordsCheckSuccessful);
    }

    public UserDto register(UserCreateDto userCreateDto) {
        return createUser(userCreateDto);
    }

    @Transactional
    public UserDto createUser(UserCreateDto dto) {
        log.info("Initiating user creation for email: {}", dto.getEmail());
        if (userRepository.userWithEmailExists(dto.getEmail()) > 0) {
            throw new EmailDuplicationException(dto.getEmail());
        }
        validateUserFields(dto);
        var createdDto =
                userMapper.toUserDto(userRepository.save(userMapper.toEntity(dto)));
        try {
            var secretKey = secretKeyService.assignSecretKeyToUser(createdDto.getId(), KeyType.USER_CREATION).getKey();
            createdDto = createdDto.toBuilder().secretKey(secretKey).build();
            notificationService.sendActivationMessage(createdDto.getEmail(), createdDto.getId(), secretKey);
        } catch (MongoException e) {
            throw new SecretKeyException(e.getMessage());
        }
        log.info("Successfully created user. user id: {}", createdDto.getId());
        return createdDto;
    }

    @Transactional
    public void activateUser(UUID userId, UUID secretKey) {
        var userEmail = userRepository.findEmailById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        var updatedRecords = userRepository.activateUser(userId);
        if (updatedRecords < 1) {
            throw new UserInactiveException(userEmail);
        }
        try {
            secretKeyService.deleteByUserIdAndKey(secretKey, userId, KeyType.USER_CREATION);
        } catch (MongoException e) {
            log.error("Failed to manage user secret key.", userId);
            throw new SecretKeyException(e.getMessage());
        }
        notificationService.sendActivationConfirmationMessage(userEmail);
    }

    public UserDto getUser(UUID userId) {
        if (userId == null) {
            throw new MissingFieldException("user id");
        }
        var user = findUserById(userId);
        return userMapper.toUserDto(user);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserDto).toList();
    }

    @Transactional
    public UserDto updateUser(UUID userId, UserUpdateDto dto) {
        var dtoType = dto.getUserType();
        validateType(dtoType);
        var dtoEmail = dto.getEmail();
        validateEmail(dtoEmail);
        var user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        if (emailUpdateRequested(dtoEmail, user.getEmail())) {
            validateEmailAvailability(dtoEmail);
        }
        user = user.toBuilder()
                .email(dtoEmail)
                .userType(dtoType)
                .phoneNumber(dto.getPhoneNumber())
                .build();
        var userUpdated = userMapper.toUserDto(userRepository.save(user));
        notificationService.sendNewEmailUpdateMessage(userUpdated.getEmail());
        return userUpdated;
    }

    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
        //todo delete all secret keys
        log.info("Successfully deleted user with id: {}", userId);
    }

    @Transactional
    public void updatePassword(String email, UserUpdatePasswordDto dto) {
        validatePasswords(dto.getNewPassword(),
                dto.getNewPasswordConfirmation());
        var hashedCurrentPassword =
                userRepository.findPasswordByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        var hashedConfirmationPassword =
                hashingService.hashPassword(dto.getCurrentPassword());
        if (!hashedCurrentPassword.equals(hashedConfirmationPassword)) {
            throw new InvalidPasswordException();
        }
        userRepository.updatePasswordByEmail(email,
                hashingService.hashPassword(dto.getNewPassword()));
    }

    public void resetPassword(String userEmail) {
        var userId = userRepository.findIdByEmail(userEmail).orElseThrow(() -> new UserNotFoundException(userEmail));
        var secretKey = secretKeyService.assignSecretKeyToUser(userId, KeyType.PASSWORD_RESET);
        notificationService.sendResetPasswordConfirmationMessage(userEmail, secretKey.getKey());
    }

    @Transactional
    public void setNewPassword(UUID secretKey, UserResetPasswordDetailsDto dto) {
        var email = dto.getEmail();
        var userId =
                userRepository.findIdByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        secretKeyService.validateSecretKey(userId, secretKey);
        validatePasswords(dto.getNewPassword(), dto.getNewPasswordConfirmation());
        userRepository.updatePasswordByEmail(email, hashingService.hashPassword(dto.getNewPassword()));
        try {
            secretKeyService.deleteByUserIdAndKey(secretKey, userId, KeyType.PASSWORD_RESET);
        } catch (MongoException e) {
            log.error("Failed to manage user secret key.", userId);
            throw new SecretKeyException(e.getMessage());
        }
        //todo send notification that password has been updated
    }

    private User findUserById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    private void validateUserFields(UserCreateDto dto) {
        validateType(dto.getUserType());
        validateEmail(dto.getEmail());
        validatePasswords(dto.getPassword(), dto.getPasswordConfirmation());
    }

    private void validateType(UserType userType) {
        if (userType == null) {
            throw new MissingFieldException("user type");
        }
        if (userType.equals(UserType.ADMIN)) {
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

    private boolean validateEmailAvailability(String email) {
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

    private boolean emailUpdateRequested(String newEmail, String oldEmail) {
        return !oldEmail.equals(newEmail);
    }
}
