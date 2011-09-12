package ux.display;

/**
 * Defines the publishing/observable functionality of a graphical display than
 * enables selecting simulation items.. Subscribers can be added an removed.
 *
 * @author Kevin Doran
 *
 */
public interface SelectionPublisher {

    /**
     * Registers a {@code SelectionSubscriber} as an observer of the
     * {@code SelectionPublisher}. The observer will have its
     * setSelected method called when an item is selected.
     *
     * @param subscriber    the {@code SelectionSubscriber} to register.
     * @return             {@code true} if the observer was added;
     *                      {@code false} if the observer is already registered.
     */
    public boolean addSubscriber(SelectionSubscriber subscriber);

    /**
     * Removes a subscriber from.
     *
     * @param   subscriber  the subscriber to be removed
     * @return  {@code true} if the subscriber was removed, {@code false}
     *          if subscriber was not actually a registered subscriber.
     */
    public boolean removeSubscriber(SelectionSubscriber subscriber);
}



