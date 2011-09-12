
package simulation.sensors;

import simulation.geometry.Environment;
import simulation.geometry.Entity;
import simulation.geometry.XPoint;
import simulation.entities.Cup;
import simulation.entities.Robot;
import java.awt.Shape;
import java.util.Collection;

/**
 * GPS.java
 * Allows the robot to 'sence' its position as a set of coordinates.
 *
 * @author Sam Sanson, Jermin Tiu
 * @Walkedthrough
 * @DeskChecked
 */
public class GPS extends Entity implements Sensor {
    private double maxNoise = 0;    // The max noise that the sensor can have
    private XPoint location;        // The location of the host(robot)

    /**
     * Creates a standard GPS sensor with no noise.
     */
    public GPS() {
        super(new Entity(20, 20, 20));
    }

    /**
     * Creates a GPS sensor which has a specified uncertainty (noise).
     * @param uncertainty the uncertainty of the position of the sensor (usually between 0 and 1)
     */
    public GPS(double uncertainty) {
        this();
        maxNoise = uncertainty;
    }

    /**
     * @inheritdoc
     *
     * NOTE: Parameters are not used but are included for consistency.
     * @param env    unused
     * @param robots unused
     * @param things unused
     */
    public void analyse(final Environment env, final Collection<Robot> robots, final Collection<Cup> things) {

        // Add noise to signal
        double x = location.getX() + (2 * maxNoise * Math.random() - maxNoise) * location.getX();
        double y = location.getY() + (2 * maxNoise * Math.random() - maxNoise) * location.getY();

        location.setLocation(x, y);
    }

    @Override
    public String getValue() {
        String result = "(" + valueFormat.format(location.getX()) + "," + valueFormat.format(location.getY()) + ")";

        return result;
    }

    /**
     * Returns the sensors result.
     * @return The location of the host relative to the environment
     */
    public XPoint getOutput() {
        return location;
    }

    /**
     * Sets the location of the host(robot).
     * @param obj The host
     * @since 1.1
     */
    public void setObject(final SensorAble obj) {
        this.location = obj.getCom();
    }

    
    @Override
    public String getDescription() {
        return "GPS (x,y)";
    }

    @Override
    public Shape getRange() {
        return null;
    }
}



