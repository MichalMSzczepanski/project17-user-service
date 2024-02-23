package work.szczepanskimichal.model.dto;

import lombok.Builder;
import lombok.Getter;
import work.szczepanskimichal.enums.UserType;

@Builder(toBuilder = true)
@Getter
public class UserUpdateDto {

    private String email;
    private String phoneNumber;
    private UserType userType;

}
