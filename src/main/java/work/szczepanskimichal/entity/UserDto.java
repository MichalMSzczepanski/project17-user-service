package work.szczepanskimichal.entity;

import lombok.Builder;
import lombok.Getter;
import work.szczepanskimichal.enums.Type;

import java.util.UUID;

@Builder(toBuilder = true)
@Getter
public class UserDto implements UserDAO {

    private UUID id;
    private String email;
    private String password;
    private String passwordConfirmation;
    private Type type;
    private boolean active;
    private String phoneNumber;

}
