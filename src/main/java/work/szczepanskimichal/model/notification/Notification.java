package work.szczepanskimichal.model.notification;

import work.szczepanskimichal.enums.NotificationType;

public interface Notification {

    NotificationType getType();

    String getAddressee();

    String getSubject();

    String getMessage();

}
