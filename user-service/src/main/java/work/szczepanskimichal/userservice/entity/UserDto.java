package work.szczepanskimichal.userservice.entity;

import lombok.Builder;
import lombok.Getter;
import work.szczepanskimichal.userservice.enums.Type;

import java.util.UUID;

@Builder
@Getter
public class UserDto implements UserDAO {

    private UUID id;
    private String email;
    private String password;
    private String passwordConfirmation;
    private boolean active;
    private String phoneNumber;
    private Type type;

}
