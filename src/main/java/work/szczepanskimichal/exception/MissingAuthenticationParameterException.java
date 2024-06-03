package work.szczepanskimichal.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MissingAuthenticationParameterException extends RuntimeException {

    public MissingAuthenticationParameterException(String parameterName) {
        super(String.format("missing authentication parameter: %s", parameterName));
        log.error(String.format("missing authentication parameter: %s", parameterName));
    }
}