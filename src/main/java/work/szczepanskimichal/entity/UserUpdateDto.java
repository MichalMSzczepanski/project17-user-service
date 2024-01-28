package work.szczepanskimichal.entity;

import lombok.Builder;
import lombok.Getter;
import work.szczepanskimichal.enums.Type;

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
