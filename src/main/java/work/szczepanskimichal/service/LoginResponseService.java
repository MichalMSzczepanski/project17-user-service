package work.szczepanskimichal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import work.szczepanskimichal.model.LoginResponse;
import work.szczepanskimichal.model.UserLoginDto;
import work.szczepanskimichal.repository.LoginResponseRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginResponseService {

    private final LoginResponseRepository loginResponseRepository;

    public ResponseEntity<LoginResponse> registerLogin(UserLoginDto userLoginDto, boolean successfulLogin) {
        String message;
        HttpStatus status;
        if (successfulLogin) {
            message = String.format("user with email: %s provided valid credentials", userLoginDto.getEmail());
            status = HttpStatus.OK;
        } else {
            message = String.format("user with email: %s provided invalid credentials", userLoginDto.getEmail());
            status = HttpStatus.UNAUTHORIZED;
        }
        var loginResponse = LoginResponse.builder()
                .email(userLoginDto.getEmail())
                .responseStatus(status)
                .message(message)
                .timeStamp(LocalDateTime.now())
                .build();
        log.info(message);
        return ResponseEntity.status(status).body(loginResponseRepository.save(loginResponse));
    }
}
