package publishersubscriber;

/**
 * A {@code SimulatorSubscriber} subscribers to a simulator in order to receive
 * notifications when the simulator updates. A subscriber only needs to
 * implement one method to allow the simulator to notify it of its state change.
 *
 * @author Simon, Kevin Doran
 */
public interface SimulatorSubscriber {

    /**
     * This method is called by a SimulatorPublisher on all its subscribers when
     * its state has changed.
     *
     * @param publisher The publisher who called the update method.
     */
    void update(SimulatorPublisher publisher);

    /**
     * This method is called by SimulatorPublisher on all its subscribers when a
     * call to reset the simulator is made.
     * 
     * @param publisher The publisher that called the reset method.
     */
    void simulatorResetted(SimulatorPublisher publisher);
}



