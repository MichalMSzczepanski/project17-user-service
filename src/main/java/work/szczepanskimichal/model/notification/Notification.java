package work.szczepanskimichal.model.notification;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.Map;

@Builder
@Getter
public class Notification implements Serializable {

    private String addressee;
    private NotificationType type;
    private NotificationSubject subject;
    private Map<String, String> messageParameters;
}
