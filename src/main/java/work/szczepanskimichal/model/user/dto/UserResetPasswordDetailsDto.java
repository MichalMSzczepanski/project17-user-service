package work.szczepanskimichal.model.user.dto;

import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class UserResetPasswordDetailsDto {

    private String email;
    private String newPassword;
    private String newPasswordConfirmation;

}
