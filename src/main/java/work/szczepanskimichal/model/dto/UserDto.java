package work.szczepanskimichal.model.dto;

import lombok.Builder;
import lombok.Getter;
import work.szczepanskimichal.enums.Type;

import java.util.UUID;

@Builder(toBuilder = true)
@Getter
public class UserDto {

    private UUID id;
    private String email;
    private Type type;
    private boolean active;
    private String phoneNumber;
    private UUID secretKey;

}