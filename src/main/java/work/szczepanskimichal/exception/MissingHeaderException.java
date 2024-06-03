package work.szczepanskimichal.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MissingHeaderException extends RuntimeException {

    public MissingHeaderException(String header) {
        super(String.format("missing header: %s", header));
        log.error(String.format("missing header: %s", header));
    }
}