package work.szczepanskimichal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import work.szczepanskimichal.model.LoginResponse;
import work.szczepanskimichal.model.user.dto.UserLoginDto;
import work.szczepanskimichal.repository.LoginResponseRepository;
import work.szczepanskimichal.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginResponseService {

    private final LoginResponseRepository loginResponseRepository;
    private final UserRepository userRepository;

    public Optional<LoginResponse> registerAuthentication(UserLoginDto userLoginDto, boolean successfulLogin) {
        String message;
        HttpStatus status;
        if (successfulLogin) {
            message = String.format("user with email: %s provided valid credentials", userLoginDto.getEmail());
            status = HttpStatus.OK;
        } else {
            message = String.format("user with email: %s provided invalid credentials", userLoginDto.getEmail());
            status = HttpStatus.UNAUTHORIZED;
        }
        var userIdOptional = userRepository.findIdByEmail(userLoginDto.getEmail());
        if (userIdOptional.isEmpty()) {
            message = String.format("user with email: %s not found", userLoginDto.getEmail());
            status = HttpStatus.NOT_FOUND;
        }
        var loginResponse = buildLoginResponse(userIdOptional.get(), userLoginDto.getEmail(), message, status);
        return Optional.of(loginResponseRepository.save(loginResponse));
    }

    public LoginResponse buildLoginResponse(UUID userId, String email, String message, HttpStatus status) {
        return LoginResponse.builder()
                .userId(userId)
                .email(email)
                .responseStatus(status)
                .message(message)
                .timeStamp(LocalDateTime.now())
                .build();
    }
}
