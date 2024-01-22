package work.szczepanskimichal.userservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import work.szczepanskimichal.userservice.enums.Type;

import java.util.UUID;

@Builder
public class UserDto {

    private UUID id;

    @Email
    private String email;

    private String password;
    private String passwordConfirmation;

    @Enumerated(EnumType.STRING)
    private Type type;

}
