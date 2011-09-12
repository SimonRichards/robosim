package ux.display;

import simulation.geometry.Entity;

/**
 * A {@code SelectionSubscriber} subscribers to a SelectionPublisher in order to
 * receive notifications when a simulation item is selected. A subscriber only
 * needs to implement one method to allow the SelectionPublisher to notify it
 * that a simulation item has been selected.
 *
 * @author Kevin Doran
 */
public interface SelectionSubscriber {

    /**
     * This method is called by a SelectionPublisher on all its subscribers when
     * its state has changed.
     *
     * @param publisher The publisher who called the update method.
     */
    public void setSelected(Entity selectedItem);
}



