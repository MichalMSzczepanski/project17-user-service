package work.szczepanskimichal.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "validation")
@Getter
@Setter
public class ValidationUtil {
    private String emailRegex;
}
