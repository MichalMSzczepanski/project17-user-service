package work.szczepanskimichal.exception;

import java.util.UUID;

public class UserDeactivationException extends RuntimeException {

    public UserDeactivationException(UUID userId) {
        super(String.format("user with id: %s activity update has failed", userId));
    }
}