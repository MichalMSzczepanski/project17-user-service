package work.szczepanskimichal.userservice.entity;

import lombok.Builder;
import lombok.Getter;
import work.szczepanskimichal.userservice.enums.Type;

import java.util.UUID;

@Builder(toBuilder = true)
@Getter
public class UserUpdateDto implements UserDAO {

    private String email;
    private String password;
    private String passwordConfirmation;
    private boolean active;
    private String phoneNumber;
    private Type type;

}
