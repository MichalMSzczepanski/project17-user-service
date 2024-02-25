package work.szczepanskimichal.model.user.dto;

import lombok.Builder;
import lombok.Getter;
import work.szczepanskimichal.model.user.UserType;

@Builder(toBuilder = true)
@Getter
public class UserUpdateDto {

    private String email;
    private String phoneNumber;
    private UserType userType;

}
