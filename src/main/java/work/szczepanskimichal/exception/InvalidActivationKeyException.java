package work.szczepanskimichal.exception;

import java.util.UUID;

public class InvalidActivationKeyException extends RuntimeException {

    public InvalidActivationKeyException(UUID key) {
        super(String.format("Invalid or expired activation key used by user: %s ", key));
    }
}