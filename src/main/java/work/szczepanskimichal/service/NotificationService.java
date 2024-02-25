package work.szczepanskimichal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import work.szczepanskimichal.enums.NotificationSubject;
import work.szczepanskimichal.enums.NotificationType;
import work.szczepanskimichal.model.notification.Email;

import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final KafkaService kafkaService;

    public void sendActivationMessage(String userEmail, UUID userId, UUID secretKey) {
        var parameters = new HashMap<String, String>();
        parameters.put("userId", userId.toString());
        parameters.put("secretKey", secretKey.toString());
        var notification = Email.builder()
                .addressee(userEmail)
                .type(NotificationType.EMAIL)
                .subject(NotificationSubject.USER_ACTIVATION)
                .messageParameters(parameters)
                .build();
        kafkaService.sendMessage(notification);
    }

    public void sendActivationConfirmationMessage(String userEmail) {
        var notification = Email.builder()
                .addressee(userEmail)
                .type(NotificationType.EMAIL)
                .subject(NotificationSubject.USER_ACTIVATION_CONFIRMATION)
                .build();
        kafkaService.sendMessage(notification);
    }

    public void sendNewEmailConfiguredMessage(String userEmail) {
        var notification = Email.builder()
                .addressee(userEmail)
                .type(NotificationType.EMAIL)
                .subject(NotificationSubject.USER_EMAIL_UPDATE)
                .build();
        kafkaService.sendMessage(notification);
    }

    public void sendResetPasswordConfirmationMessage(String userEmail, UUID userId, UUID secretKey) {
        var parameters = new HashMap<String, String>();
        parameters.put("userId", userId.toString());
        parameters.put("secretKey", secretKey.toString());
        var notification = Email.builder()
                .addressee(userEmail)
                .type(NotificationType.EMAIL)
                .subject(NotificationSubject.USER_PASSWORD_UPDATE)
                .messageParameters(parameters)
                .build();
        kafkaService.sendMessage(notification);
    }
}
