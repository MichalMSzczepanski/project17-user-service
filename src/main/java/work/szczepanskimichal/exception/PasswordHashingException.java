package work.szczepanskimichal.exception;

public class PasswordHashingException extends RuntimeException {

    public PasswordHashingException(Throwable e) {
        super("Error hashing password", e);
    }
}