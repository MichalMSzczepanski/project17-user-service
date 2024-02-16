package work.szczepanskimichal.model.dto;

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
