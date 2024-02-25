package work.szczepanskimichal.model.user.dto;

import lombok.Builder;
import lombok.Getter;
import work.szczepanskimichal.model.user.UserType;

import java.util.UUID;

@Builder(toBuilder = true)
@Getter
public class UserDto {

    private UUID id;
    private String email;
    private UserType userType;
    private boolean active;
    private String phoneNumber;
    private UUID secretKey;

}
