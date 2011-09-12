
package simulation.sensors;

import simulation.geometry.Environment;
import simulation.geometry.Entity;
import simulation.entities.Cup;
import simulation.entities.Robot;
import java.awt.Shape;
import java.util.Collection;

/**
 * VelocitySensor.java
 *
 * @author Sam Sanson, Jermin Tiu
 * @author Jermin Tiu
 * @WalkedThrough
 * @DeskChecked Ben
 */
public class VelocitySensor extends Entity implements Sensor {
    private double maxNoise = 0;
    private double velocity;

    /**
     * Creates a standard velocity sensor with no noise.
     */
    public VelocitySensor() {
        this(0);
    }

    /**
     * Creates a velocity sensor with a specified uncertainty.
     * @param SNR the signal to noise ratio of the velocity (usually between 0 and 1)
     */
    public VelocitySensor(double SNR) {
        super(new Entity(20, 20, 20));
        maxNoise = SNR;
    }

    /**
     * Calculates the velocity of the sensor.
     * Note: The parameters are not used but are included for consistency.
     * @param env    unused
     * @param robots unused
     * @param things unused
     */
    public void analyse(final Environment env, final Collection<Robot> robots, final Collection<Cup> things) {

        // Add noise to the signal
        velocity = velocity + (2 * maxNoise * Math.random() - maxNoise) * velocity;
    }

    /**
     * @inheritdoc
     */
    @Override
    public String getValue() {
        return valueFormat.format(velocity);
    }

    /**
     * Returns the sensors result. Should only be called after <code>analyse</code>.
     * @return The velocity of the sensor
     */
    public double getOutput() {
        return velocity;
    }

    /**
     * @inheritdoc 
     */
    public void setObject(final SensorAble obj) {
        this.velocity = obj.getVelocity();
    }

    /**
     * @inheritdoc 
     */
    public String getDescription() {
        return "Speedometer (velocity)";
    }
    
    /**
     * @inheritdoc 
     */
    @Override
    public Shape getRange() {
        return null;
    }
}



