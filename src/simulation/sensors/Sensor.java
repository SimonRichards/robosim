
package simulation.sensors;

import simulation.geometry.Environment;
import simulation.entities.Cup;
import simulation.entities.Robot;
import java.awt.Shape;
import java.text.DecimalFormat;
import java.util.Collection;

/**
 * Sensor.java
 * The sensor superclass.
 *
 * @author Simon, Jermin Tiu
 * @WalkedThrough Kevin
 * @Deskchecked Ben
 */
public interface Sensor {
    /**
     * Format for floats in the getValue().
     */
    public static final DecimalFormat valueFormat = new DecimalFormat("0.00");

    // Location of sensor relative to robot centre
    // Location of sensor relative to robot centre

    /**
     * The common method for all sensors. Runs the sensor's 'sensing'
     * functionality.
     * @param env The environment to scan.
     * @param robots Robots to check
     * @param things Things to check
     */
    void analyse(Environment env, Collection<Robot> robots, Collection<Cup> things);

    /**
     * To be passed in before calling <code>analyse()</code>.
     * @param obj The object which the sensor is attached to
     * @see SensorAble
     * @since 1.1
     */
    void setObject(SensorAble obj);

    /**
     * A sensor can sense with in a given area of operation.
     * @return The sensor's area of operation shape.
     */
    Shape getRange();

    /**
     * String returning a output to the sensor's current state.
     * @return
     */
    String getValue();

    /**
     * A Description of the Sensor.
     * @return The description of the sensor.
     */
    String getDescription();
}



