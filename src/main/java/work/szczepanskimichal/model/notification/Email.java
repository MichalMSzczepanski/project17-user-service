package work.szczepanskimichal.model.notification;

import lombok.Builder;
import lombok.Getter;
import work.szczepanskimichal.enums.NotificationSubject;
import work.szczepanskimichal.enums.NotificationType;

import java.io.Serializable;
import java.util.Map;

@Builder
@Getter
public class Email implements Notification, Serializable {

    private String addressee;
    private NotificationType type;
    private NotificationSubject subject;
    private Map<String, String> messageParameters;
}
