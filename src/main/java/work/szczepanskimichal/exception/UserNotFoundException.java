package work.szczepanskimichal.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(UUID userId) {
        super(String.format("user with id: %s not found", userId));
    }

    public UserNotFoundException(String email) {
        super(String.format("user with email: %s not found", email));
    }
}