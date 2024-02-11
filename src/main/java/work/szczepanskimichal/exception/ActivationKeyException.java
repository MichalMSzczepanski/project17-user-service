package work.szczepanskimichal.exception;

public class ActivationKeyException extends RuntimeException {

    public ActivationKeyException(String errorMessage) {
        super(String.format("Encountered issue on activation key persistence. Error: %s", errorMessage));
    }
}