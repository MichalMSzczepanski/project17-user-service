package work.szczepanskimichal.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "password.hashing")
@Getter
@Setter
public class PasswordHashingUtil {
    private String salt;
}
