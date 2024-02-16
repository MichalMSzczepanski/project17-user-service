package work.szczepanskimichal.exception;

public class UserInactiveException extends RuntimeException {

    public UserInactiveException(String email) {
        super(String.format("user with email: %s has not been activated", email));
    }
}