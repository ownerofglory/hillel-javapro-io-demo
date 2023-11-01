package ua.ithillel.io.decorator.notifier;

public class DefaultNotifier implements Notifier {
    @Override
    public void doNotify(String message) {
        System.out.println("Sending notification: "+ message);
    }
}
