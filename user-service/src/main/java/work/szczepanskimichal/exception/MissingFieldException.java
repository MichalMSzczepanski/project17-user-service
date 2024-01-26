package work.szczepanskimichal.exception;

public class MissingFieldException extends RuntimeException {

    public MissingFieldException(String fieldType) {
        super(String.format("missing or empty field: %s", fieldType));
    }
}