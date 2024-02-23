package work.szczepanskimichal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import work.szczepanskimichal.enums.NotificationType;
import work.szczepanskimichal.model.notification.Email;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final KafkaService kafkaService;

    public void sendActivationEmail(String userEmail, UUID userId, UUID secretKey) {
        var message = String.format("Please activate your account at : http://localhost:8081/activate/%s/%s", userId,
                secretKey);
        var notification = Email.builder()
                .type(NotificationType.EMAIL)
                .addressee(userEmail)
                .subject("Activate account for presentday")
                .message(message)
                .build();
        kafkaService.sendMessage(notification);
    }
}
