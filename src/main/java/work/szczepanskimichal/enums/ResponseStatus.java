package work.szczepanskimichal.enums;

public enum ResponseStatus {
    OK(200, "OK"),
    CREATED(201, "Created"),
    ACCEPTED(202, "Accepted"),

    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),

    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),

    UNKNOWN(0, "Unknown");

    private final int code;
    private final String description;

    ResponseStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ResponseStatus valueOf(int statusCode) {
        for (ResponseStatus responseStatus : values()) {
            if (responseStatus.getCode() == statusCode) {
                return responseStatus;
            }
        }
        return UNKNOWN;
    }
}