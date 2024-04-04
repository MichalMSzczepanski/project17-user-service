package work.szczepanskimichal.exception;

public class InvalidPhoneNumberException extends RuntimeException {

    public InvalidPhoneNumberException() {
        super("Invalid phone number");
    }
}