package work.szczepanskimichal.exception;

public class InvalidActivationKeyException extends RuntimeException {

    public InvalidActivationKeyException() {
        super("Invalid or expired activation key");
    }
}