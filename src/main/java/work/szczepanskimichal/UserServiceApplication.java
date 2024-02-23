package work.szczepanskimichal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(excludeName = {"de.flapdoodle.embed.mongo.spring.autoconfigure.EmbeddedMongoAutoConfiguration"})
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}