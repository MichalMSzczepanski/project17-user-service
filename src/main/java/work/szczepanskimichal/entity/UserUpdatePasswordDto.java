package work.szczepanskimichal.entity;

import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class UserUpdatePasswordDto {

    private String currentPassword;
    private String newPassword;
    private String newPasswordConfirmation;

}
