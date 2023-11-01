package ua.ithillel.io.decorator.ownimpl;

import ua.ithillel.io.decorator.notifier.Notifier;

public abstract class NotifierDecorator implements Notifier {
    protected Notifier notifier;

    public NotifierDecorator(Notifier notifier) {
        this.notifier = notifier;
    }
}
