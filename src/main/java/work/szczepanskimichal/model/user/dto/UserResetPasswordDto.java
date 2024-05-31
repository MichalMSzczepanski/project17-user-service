package work.szczepanskimichal.model.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder(toBuilder = true)
@Getter
public class UserResetPasswordDto {

    private UUID userId;

    @JsonCreator
    public UserResetPasswordDto(@JsonProperty("userId") UUID userId) {
        this.userId = userId;
    }

}
