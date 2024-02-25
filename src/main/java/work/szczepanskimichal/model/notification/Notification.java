package work.szczepanskimichal.model.notification;

import work.szczepanskimichal.enums.NotificationSubject;
import work.szczepanskimichal.enums.NotificationType;

import java.util.Map;

public interface Notification {

    String getAddressee();

    NotificationType getType();

    NotificationSubject getSubject();

    Map<String, String> getMessageParameters();

}
