package work.szczepanskimichal.entity;

import work.szczepanskimichal.enums.Type;

public interface UserDAO {

    String getEmail();
    Type getType();
    String getPassword();
    String getPasswordConfirmation();

}
