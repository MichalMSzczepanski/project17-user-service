package work.szczepanskimichal.userservice.exception;

public class AdminProgrammaticCreationException extends RuntimeException {

    public AdminProgrammaticCreationException() {
        super("Admins cannot be created programmatically");
    }
}