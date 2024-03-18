package work.szczepanskimichal.exception;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class SecretKeyNotAssignedException extends RuntimeException {

    public SecretKeyNotAssignedException(UUID userId) {
        super(String.format("Secret key not assigned to user with email: %s", userId));
        log.warn("logging details here?");
    }
}