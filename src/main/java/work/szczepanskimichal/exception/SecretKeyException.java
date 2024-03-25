package work.szczepanskimichal.exception;

import java.util.UUID;

public class SecretKeyException extends RuntimeException {

    public SecretKeyException( UUID userId, String errorMessage) {
        super(String.format("Encountered issue when managing secret key for user: %s. Error: %s", userId, errorMessage));
    }
}