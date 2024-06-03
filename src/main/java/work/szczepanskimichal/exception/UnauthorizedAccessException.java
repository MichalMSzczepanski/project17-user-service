package work.szczepanskimichal.exception;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class UnauthorizedAccessException extends RuntimeException {

    public UnauthorizedAccessException(UUID userId) {
        super("User is not authorized to access this resource");
        log.error("Unauthorized attempt to access resources using user id: {}", userId);
    }
}