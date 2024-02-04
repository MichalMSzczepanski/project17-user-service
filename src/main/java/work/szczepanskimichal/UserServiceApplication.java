package work.szczepanskimichal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import work.szczepanskimichal.utils.PasswordHashingUtil;
import work.szczepanskimichal.utils.ValidationUtil;

@SpringBootApplication(excludeName = {"de.flapdoodle.embed.mongo.spring.autoconfigure.EmbeddedMongoAutoConfiguration"})
@EnableConfigurationProperties({PasswordHashingUtil.class, ValidationUtil.class})
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}