package work.szczepanskimichal.exception;

public class EmailDuplicationException extends RuntimeException {

    public EmailDuplicationException(String email) {
        super(String.format("Email already registered: %s", email));
    }
}