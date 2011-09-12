package simulation.entities.behaviour;

import simulation.entities.RobotOutput;

/**
 *s
 * @author trc29
 */
public interface Behaviour {

    /**
     * Sends the robot outputs to the simulator.
     * Map is: String OutputName , Double OutputState.
     *  OutputNames Steering and Motor are required.
     *  OutputName Arm is optional.
     *
     * @return
     */
    public RobotOutput update();

    /**
     * True once the behaviour is completed.
     * @return the completion state of the behaviour
     */
    public boolean isFinished();

    /**
     * Resets the state of the behaviour.
     */
    public void reset();
}



