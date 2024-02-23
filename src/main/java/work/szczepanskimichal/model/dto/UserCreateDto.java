package work.szczepanskimichal.model.dto;

import lombok.Builder;
import lombok.Getter;
import work.szczepanskimichal.enums.UserType;

import java.util.UUID;

@Builder(toBuilder = true)
@Getter
public class UserCreateDto {

    private UUID id;
    private String email;
    private String password;
    private String passwordConfirmation;
    private UserType userType;
    private boolean active;
    private String phoneNumber;

}
