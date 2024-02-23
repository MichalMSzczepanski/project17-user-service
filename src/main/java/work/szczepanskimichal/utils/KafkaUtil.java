package work.szczepanskimichal.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "kafka.topic")
@Component
@Getter
@Setter
public class KafkaUtil {
    String notificationTopic;
}
