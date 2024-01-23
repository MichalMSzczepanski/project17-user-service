package work.szczepanskimichal.userservice.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(UUID userId) {
        super(String.format("user with id: %s not found", userId));
    }
}