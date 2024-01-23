package work.szczepanskimichal.userservice.entity;

import work.szczepanskimichal.userservice.enums.Type;

public interface UserDAO {

    String getEmail();
    Type getType();
    String getPassword();
    String getPasswordConfirmation();

}
