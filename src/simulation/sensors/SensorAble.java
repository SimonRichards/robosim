package simulation.sensors;

import simulation.geometry.Entity;
import simulation.geometry.XPoint;
import java.util.Collection;

/**
 * Allows an object to interact with a Sensor. Functions may not necessarily be called but must be implemented.
 * The Sensor object will look for these functions to extract information
 *
 * @author Jermin
 * @version 1.0 5/05/2011
 *
 * @since 1.0
 */
public interface SensorAble {

    /**
     * Returns the current angle of the object
     * @return angle of the object
     */
    public double getAngle();

    /**
     * Returns a Point containing the location of the object
     * @return location of the object
     */
    public XPoint getCom();

    /**
     * Returns the velocity of the object
     * @return the velocity of the object
     */
    public double getVelocity();

    /**
     * @return All items that the robot has collected
     */
    Collection<? extends Entity> getHeldItems();
}



