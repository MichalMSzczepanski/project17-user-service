package work.szczepanskimichal.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "validation")
@Component
@Getter
@Setter
public class ValidationUtil {
    private String emailRegex;
}
