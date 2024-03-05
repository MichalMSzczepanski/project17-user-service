package work.szczepanskimichal.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.mail")
@Getter
@Setter
public class CredentialsUtil {
    String password;
}
