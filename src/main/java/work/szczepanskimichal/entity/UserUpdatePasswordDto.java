package work.szczepanskimichal.entity;

import lombok.Builder;
import lombok.Getter;
import work.szczepanskimichal.enums.Type;

@Builder(toBuilder = true)
@Getter
public class UserUpdatePasswordDto {

    private String email;
    private boolean active;
    private String phoneNumber;
    private Type type;

}
