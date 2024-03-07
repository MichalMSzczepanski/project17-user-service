package work.szczepanskimichal.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "password")
@Component
@Getter
@Setter
public class PasswordHashingUtil {
    private String hashingSalt;
}
