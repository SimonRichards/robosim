
package simulation.sensors;

import simulation.geometry.Environment;
import simulation.geometry.Entity;
import simulation.entities.Cup;
import simulation.entities.Robot;
import java.awt.Shape;
import java.util.Collection;
import java.util.Random;

/**
 * CompassSensor.java
 * Finds the orientation of the robot relative to the environment.
 *
 * @author Sam Sanson, Joshua Jordan, Jermin Tiu
 * @WalkedThrough
 * @DeskChecked Ben
 */
public class CompassSensor extends Entity implements Sensor {
    private final static double DEFAULT_NOISE    = 0.1;     // The defaule noise of the sensor
    private double              output           = 0;       // The output of the sensor
    private double              uncertainty      = 0;       // The maximum noise present in the sensor
    private boolean             FLAG_INITIALIZED = false;   // Flag to determine if the first value of the sensor has been read
    private Random              noise;                      // Noise generated during measurement

    /**
     * Creates a standard compass sensor with default noise.
     */
    public CompassSensor() {
        this(DEFAULT_NOISE);
    }

    /**
     * Creates a compass sensor that has a specified noise value.
     * @param uncertainty Signal to noise ratio of the sensor (usually between 0 and 1)
     */
    public CompassSensor(double uncertainty) {
        super(new Entity(20, 20, 20));
        this.uncertainty = uncertainty;
        noise            = new Random();
    }

    /**
     * @inheritdoc
     *
     * @param env
     * @param robots
     * @param things
     */
    @Override
    public void analyse(final Environment env, final Collection<Robot> robots, final Collection<Cup> things) {
        output += noise.nextGaussian() * uncertainty * output;
    }

    /**
     * Returns the value of the sensor with noise if required.
     * @return the angle of the sensor
     */
    public double getOutput() {
        return output;
    }

    /**
     * Sets the angle of the sensor.
     * @param obj the object to get the angle for
     *
     * @since 1.1
     */
    @Override
    final public void setObject(final SensorAble obj) {
        output           = obj.getAngle();
        FLAG_INITIALIZED = true;
    }

    @Override
    public String getValue() {
        return valueFormat.format(output);
    }

    @Override
    public String getDescription() {
        return "Compass (angle: radian)";
    }

    @Override
    public Shape getRange() {
        return null;
    }

    /**
     * Determines if the first value of a sensor has been read
     * @return true if the first value has been read
     */
    public boolean isInitialized() {
        return FLAG_INITIALIZED;
    }
}



