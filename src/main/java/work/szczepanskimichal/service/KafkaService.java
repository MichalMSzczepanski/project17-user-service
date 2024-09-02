package work.szczepanskimichal.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import work.szczepanskimichal.model.notification.Notification;
import work.szczepanskimichal.util.KafkaUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaService {

    private final KafkaUtil kafkaUtil;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendMessage(Notification notification) {
        try {
            var message = objectMapper.writeValueAsString(notification);
            kafkaTemplate.send(kafkaUtil.getUserTopic(), message);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse notification: {}", notification);
        }
    }
}
