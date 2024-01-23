package work.szczepanskimichal.userservice.exception;

public class PasswordMismatchException extends RuntimeException {

    public PasswordMismatchException() {
        super("Provided passwords do not match");
    }
}