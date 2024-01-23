package work.szczepanskimichal.userservice.exception;

public class InvalidEmailException extends RuntimeException {

    public InvalidEmailException() {
        super("Invalid email");
    }
}