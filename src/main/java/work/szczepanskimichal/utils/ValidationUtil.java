package work.szczepanskimichal.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "validation")
public class ValidationUtil {
    private String emailRegex;

    public String getEmailRegex() {
        return emailRegex;
    }

    public void setEmailRegex(String emailRegex) {
        this.emailRegex = emailRegex;
    }
}
