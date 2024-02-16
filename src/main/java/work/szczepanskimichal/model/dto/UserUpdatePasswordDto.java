package work.szczepanskimichal.model.dto;

import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class UserUpdatePasswordDto {

    private String currentPassword;
    private String newPassword;
    private String newPasswordConfirmation;

}
