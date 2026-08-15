package observer.implementation;

import observer.Notification;
import observer.Publisher;
import observer.Subscriber;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PublisherImplementation implements Publisher {

    private final List<Subscriber> subscribers = new CopyOnWriteArrayList<>();

    @Override
    public void addSubscriber(Subscriber sub) {
        if (sub == null || subscribers.contains(sub)) {
            return;
        }
        subscribers.add(sub);
    }

    @Override
    public void removeSubscriber(Subscriber sub) {
        if (sub == null) {
            return;
        }
        subscribers.remove(sub);
    }

    @Override
    public void notifySubscribers(Notification notification) {
        if (notification == null) {
            return;
        }
        for (Subscriber listener : subscribers) {
            listener.update(notification);
        }
    }
}
