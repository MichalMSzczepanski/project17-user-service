package work.szczepanskimichal.exception;

import java.util.UUID;

public class InvalidActivationKeyException extends RuntimeException {

    public InvalidActivationKeyException(UUID userId) {
        super(String.format("Invalid or expired activation key used by user: %s ", userId));
    }
}