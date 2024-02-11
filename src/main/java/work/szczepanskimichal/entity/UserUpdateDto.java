package work.szczepanskimichal.entity;

import lombok.Builder;
import lombok.Getter;
import work.szczepanskimichal.enums.Type;

@Builder(toBuilder = true)
@Getter
public class UserUpdateDto {

    private String email;
    private String phoneNumber;
    private Type type;

}
