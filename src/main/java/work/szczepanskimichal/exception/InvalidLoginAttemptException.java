package work.szczepanskimichal.exception;

public class InvalidLoginAttemptException extends RuntimeException {

    public InvalidLoginAttemptException() {
        super("Invalid email or password");
    }
}