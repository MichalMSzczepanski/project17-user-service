package work.szczepanskimichal.exception;

public class MissingHeaderException extends RuntimeException {

    public MissingHeaderException(String header) {
        super(String.format("missing header: %s", header));
    }
}