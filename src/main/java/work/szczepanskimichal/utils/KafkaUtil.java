package work.szczepanskimichal.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("kafka.topic")
@Getter
@Setter
public class KafkaUtil {
    private String notificationTopic;
}
