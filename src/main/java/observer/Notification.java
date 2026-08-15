package observer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import observer.enums.NotificationCode;

@Getter
@Setter
@AllArgsConstructor
public class Notification {

    private NotificationCode code;
    private Object data;
}
