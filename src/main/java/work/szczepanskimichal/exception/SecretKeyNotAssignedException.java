package work.szczepanskimichal.exception;

import java.util.UUID;

public class SecretKeyNotAssignedException extends RuntimeException {

    public SecretKeyNotAssignedException(UUID userId) {
        super(String.format("Secret key not assigned to user with email: %s", userId));
    }
}