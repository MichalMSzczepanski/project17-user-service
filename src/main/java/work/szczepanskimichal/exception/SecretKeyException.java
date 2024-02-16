package work.szczepanskimichal.exception;

public class SecretKeyException extends RuntimeException {

    public SecretKeyException(String errorMessage) {
        super(String.format("Encountered issue on secret key persistence. Error: %s", errorMessage));
    }
}