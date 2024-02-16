package work.szczepanskimichal.service;

import com.mongodb.MongoException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import work.szczepanskimichal.model.*;
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

    private final LoginResponseService loginResponseService;
    private final ActivationKeyService activationKeyService;
    private final HashingService hashingService;
    private final UserRepository userRepository;
    private final ValidationUtil validationUtil;
    private final UserMapper userMapper;

    @Transactional
    public UserDto createUser(UserCreateDto userCreateDto) {
        log.info("Initiating user creation for email: {}", userCreateDto.getEmail());
        if (userRepository.userWithEmailExists(userCreateDto.getEmail()) > 0) {
            throw new EmailDuplicationException(userCreateDto.getEmail());
        }
        validateUserFields(userCreateDto);
        var createdDto =
                userMapper.toUserDto(userRepository.save(userMapper.toEntity(userCreateDto)));
        try {
            var activationKey = activationKeyService.assignActivationKeyToUser(createdDto.getId()).getKey();
            createdDto = createdDto.toBuilder().activationKey(activationKey).build();
        } catch (MongoException e) {
            throw new ActivationKeyException(e.getMessage());
        }
        log.info("Successfully created user. user id: {}", createdDto.getId());
        return createdDto;
    }

    @Transactional
    public int activateUser(UUID userId, UUID activationKey) {
        try {
            activationKeyService.deleteByUserIdAndKey(activationKey, userId);
        } catch (MongoException e) {
            log.error("Failed to manage user activation key.", userId);
            throw new ActivationKeyException(e.getMessage());
        }
        //todo send email confirming activation
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
        return userRepository.findAll().stream().map(userMapper::toUserDto).toList();
    }

    @Transactional
    public UserDto updateUser(UUID userId, UserUpdateDto userUpdateDto) {
        validateType(userUpdateDto.getType());
        validateEmail(userUpdateDto.getEmail());
        var user = userExists(userId);
        boolean userActiveStatus = user.isActive();
        if (emailUpdated(userUpdateDto.getEmail(), user.getEmail()) && isEmailAvailable(userUpdateDto.getEmail())) {
            try {
                activationKeyService.assignActivationKeyToUser(userId);
                userActiveStatus = false;
            } catch (MongoException e) {
                log.error("Failed to update user due to activation key issue. Terminating user " + "update.", userId);
                throw new ActivationKeyException(e.getMessage());
            }
        }
        var updatedUser =
                userMapper.toEntity(userUpdateDto).toBuilder().id(userId).active(userActiveStatus).build();
        return userMapper.toUserDto(userRepository.save(updatedUser));
    }

    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
        log.info("Successfully deleted user with id: {}", userId);
    }

    public ResponseEntity<LoginResponse> login(UserLoginDto userLoginDto) {
        if (!userRepository.isUserActive(userLoginDto.getEmail())) {
            throw new UserInactiveException(userLoginDto.getEmail());
        }
        var persistedPassword =
                userRepository.findPasswordByEmail(userLoginDto.getEmail())
                        .orElseThrow(() -> new UserNotFoundException(userLoginDto.getEmail()));
        var passwordsCheckSuccessful = !persistedPassword.equals(hashingService.hashPassword(userLoginDto.getPassword()));
        return loginResponseService.registerLogin(userLoginDto, passwordsCheckSuccessful);
    }

    public UserDto register(UserCreateDto userCreateDto) {
        return createUser(userCreateDto);
    }

    @Transactional
    public void updatePassword(String email, UserUpdatePasswordDto userUpdateDto) {
        validatePasswords(userUpdateDto.getNewPassword(),
                userUpdateDto.getNewPasswordConfirmation());
        var hashedCurrentPassword =
                userRepository.findPasswordByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        var hashedConfirmationPassword =
                hashingService.hashPassword(userUpdateDto.getCurrentPassword());
        if (!hashedCurrentPassword.equals(hashedConfirmationPassword)) {
            throw new InvalidPasswordException();
        }
        userRepository.updatePasswordByEmail(email,
                hashingService.hashPassword(userUpdateDto.getNewPassword()));
    }

    public void resetPassword(String email) {
        var userId = userRepository.findIdByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        activationKeyService.assignActivationKeyToUser(userId);
    }

    @Transactional
    public void setNewPassword(UUID activationKey, UserResetPasswordDto userResetPasswordDto) {
        var email = userResetPasswordDto.getEmail();
        var userId =
                userRepository.findIdByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        if (!activationKeyService.isActivationKeyValid(userId, activationKey)) {
            throw new InvalidActivationKeyException();
        }
        validatePasswords(userResetPasswordDto.getNewPassword(), userResetPasswordDto.getNewPasswordConfirmation());
        userRepository.updatePasswordByEmail(email, hashingService.hashPassword(userResetPasswordDto.getNewPassword()));
        try {
            activationKeyService.deleteByUserIdAndKey(activationKey, userId);
        } catch (MongoException e) {
            log.error("Failed to manage user activation key.", userId);
            throw new ActivationKeyException(e.getMessage());
        }
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

    private boolean emailUpdated(String newEmail, String oldEmail) {
        return !oldEmail.equals(newEmail);
    }
}
