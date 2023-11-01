package ua.ithillel.io.decorator.ownimpl;

import ua.ithillel.io.decorator.notifier.Notifier;

public class SmsNotifier extends NotifierDecorator {
    public SmsNotifier(Notifier notifier) {
        super(notifier);
    }

    @Override
    public void doNotify(String message) {
        System.out.println("Sending SMS: " + message);

        notifier.doNotify(message);
    }
}
