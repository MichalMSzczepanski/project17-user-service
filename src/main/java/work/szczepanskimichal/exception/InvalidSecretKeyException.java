package work.szczepanskimichal.exception;

public class InvalidSecretKeyException extends RuntimeException {

    public InvalidSecretKeyException() {
        super("Invalid or expired secret key");
    }
}