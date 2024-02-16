package work.szczepanskimichal.model;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class UserLoginDto {

    @Email
    private String email;

    private String password;

}
