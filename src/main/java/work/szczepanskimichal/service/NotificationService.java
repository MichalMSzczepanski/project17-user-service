package work.szczepanskimichal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import work.szczepanskimichal.model.notification.Notification;
import work.szczepanskimichal.model.notification.NotificationSubject;
import work.szczepanskimichal.model.notification.NotificationType;

import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final KafkaService kafkaService;

    public void sendUserActivationNotification(String userEmail, UUID userId, UUID secretKey) {
        var parameters = new HashMap<String, String>();
        parameters.put("userId", userId.toString());
        parameters.put("secretKey", secretKey.toString());
        var notification = Notification.builder()
                .addressee(userEmail)
                .type(NotificationType.EMAIL)
                .subject(NotificationSubject.USER_ACTIVATION)
                .messageParameters(parameters)
                .build();
        kafkaService.sendMessage(notification);
    }

    public void sendUserDeactivationNotification(String userEmail, UUID userId, UUID secretKey) {
        var parameters = new HashMap<String, String>();
        parameters.put("userId", userId.toString());
        parameters.put("secretKey", secretKey.toString());
        var notification = Notification.builder()
                .addressee(userEmail)
                .type(NotificationType.EMAIL)
                .subject(NotificationSubject.USER_DEACTIVATION)
                .messageParameters(parameters)
                .build();
        kafkaService.sendMessage(notification);
    }

    public void sendUserActivationConfirmationNotification(String userEmail) {
        var notification = Notification.builder()
                .addressee(userEmail)
                .type(NotificationType.EMAIL)
                .subject(NotificationSubject.USER_ACTIVATION_CONFIRMATION)
                .build();
        kafkaService.sendMessage(notification);
    }

    public void sendNewUserDataUpdateNotification(String userEmail) {
        var notification = Notification.builder()
                .addressee(userEmail)
                .type(NotificationType.EMAIL)
                .subject(NotificationSubject.USER_DATA_UPDATE)
                .build();
        kafkaService.sendMessage(notification);
    }

    public void sendResetUserPasswordConfirmationNotification(String userEmail, UUID secretKey) {
        var parameters = new HashMap<String, String>();
        parameters.put("secretKey", secretKey.toString());
        var notification = Notification.builder()
                .addressee(userEmail)
                .type(NotificationType.EMAIL)
                .subject(NotificationSubject.USER_PASSWORD_UPDATE)
                .messageParameters(parameters)
                .build();
        kafkaService.sendMessage(notification);
    }

    public void sendUserPasswordUpdatedNotification(String userEmail) {
        var notification = Notification.builder()
                .addressee(userEmail)
                .type(NotificationType.EMAIL)
                .subject(NotificationSubject.USER_PASSWORD_UPDATED)
                .build();
        kafkaService.sendMessage(notification);
    }
}
