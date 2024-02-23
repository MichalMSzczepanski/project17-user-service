package work.szczepanskimichal.model.notification;

import lombok.Builder;
import lombok.Getter;
import work.szczepanskimichal.enums.NotificationType;

import java.io.Serializable;

@Builder
@Getter
public class Email implements Notification, Serializable {

    private NotificationType type;
    private String addressee;
    private String subject;
    private String message;
}
