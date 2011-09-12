package simulation.entities;

import simulation.sensors.Sensor;

import java.util.Collection;

/**
 * RubyRobot.java
 * Supplies the simulator update interface between robot and simulator.
 *
 * @author Simon
 * @Walkedthrough
 * @Deskchecked
 */
public interface RubyRobot {

    /**
     * Updates the robot according to its ruby brain.
     */
    void update();

    /**
     *
     * @return A collection of sensors the robot possesses.
     */
    Collection<Sensor> getSensors();

    /**
     *
     * @return The output from the robot behaviours.
     */
    RobotOutput getOutput();
}



