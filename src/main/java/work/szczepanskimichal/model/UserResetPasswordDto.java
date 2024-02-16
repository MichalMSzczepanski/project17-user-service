package work.szczepanskimichal.model;

import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class UserResetPasswordDto {

    private String email;
    private String newPassword;
    private String newPasswordConfirmation;

}
