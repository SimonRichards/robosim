package publishersubscriber;

import simulation.geometry.Environment;
import simulation.geometry.RigidBody;

import simulation.entities.Cup;

import simulation.entities.Robot;

import java.util.Collection;

import javax.script.ScriptException;
import simulation.Scheduler;

/**
 * Defines the publishing/observable functionality of a Simulator. Subscribers
 * can be added an removed. SimulatorPublisher also defines the methods that a
 * subscriber can call to retrieve updated information.
 *
 * @author Kevin Doran
 *
 */
public interface SimulatorPublisher {

    /**
     * Registers a {@code SimulatorSubscriber} as an observer of the
     * {@code SimulatorPublisher}. The observer will have its update method
     * called when the the simulator changes state.
     *
     * @param subscriber    the {@code SimulatorSubscriber} to register.
     */
    void addSubscriber(SimulatorSubscriber subscriber);

    /**
     * Removes a subscriber.
     *
     * @param   subscriber  the subscriber to be removed
     */
    void removeSubscriber(SimulatorSubscriber subscriber);

    /**
     * @return All the robot's in the current simulation
     */
    Collection<? extends Robot> getRobots();

    /**
     * @return  The environment object of the current simulation
     */
    Environment getEnvironment();

    /**
     * @return All the inanimates in the current simulation
     */
    Collection<? extends Cup> getThings();

    /**
     * @return The length in time of the current Simulation run.
     */
    double getTimeElapsed();

    /**
     * If this is null all is well. If a
     * ScriptException object is returned then
     * that issue is cleared and the sim reset.
     * @return The current issue.
     */
    ScriptException getIssue();
    /**
     * Determines whether the RigidBody is in the Simulator.
     * @param RigidBody object
     * @return true if the RigidBody is in the simulator.
     */
    boolean isInSimulator(RigidBody object);

    /**
     * Retrieves the Scheduler
     * @return Scheduler
     */
    Scheduler getScheduler();

    public boolean canPlace(RigidBody oldShape, RigidBody newShape);
}



