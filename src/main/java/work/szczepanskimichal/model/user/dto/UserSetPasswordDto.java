package work.szczepanskimichal.model.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder(toBuilder = true)
@Getter
public class UserSetPasswordDto {

    private UUID userId;
    private String newPassword;
    private String newPasswordConfirmation;

    @JsonCreator
    public UserSetPasswordDto(@JsonProperty("userId") UUID userId,
                              @JsonProperty("newPassword") String newPassword,
                              @JsonProperty("newPasswordConfirmation") String newPasswordConfirmation) {
        this.userId = userId;
        this.newPassword = newPassword;
        this.newPasswordConfirmation = newPasswordConfirmation;
    }

}
