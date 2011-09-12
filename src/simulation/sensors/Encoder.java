package simulation.sensors;

import simulation.geometry.Environment;
import simulation.geometry.Entity;
import simulation.geometry.XPoint;
import simulation.entities.Cup;
import simulation.entities.Robot;
import java.awt.Shape;
import java.util.Collection;

/**
 * Encoder.java
 * Keeps a track of how far a robot has traveled.
 *
 * @author Jermin
 * @Walkedthrough
 * @DeskChecked Ben
 *
 * @since 1.0
 */
public class Encoder extends Entity implements Sensor {
    private final static int MAX_RESOLUTION    = 0;
    private int              currentResolution = 0;
    private XPoint           lastLocation      = new XPoint(0, 0);
    private double           distanceTravelled;
    private double           lastDistance;

    /**
     * Constructs a standard encoder at the center of the robot.
     */
    public Encoder() {
        super(new Entity(20, 20, 20));
        distanceTravelled = 0;
    }

    @Override
    public void analyse(Environment env, Collection<Robot> robots, Collection<Cup> things) {
        // do nothing
    }

    @Override
    public void setObject(SensorAble obj) {
        currentResolution++;

        if (currentResolution >= MAX_RESOLUTION) {
            currentResolution = 0;
            lastDistance      = obj.getCom().distance(lastLocation);
            System.out.println(lastDistance);
            distanceTravelled += lastDistance;
            lastLocation.setLocation(obj.getCom());
        }
    }

    @Override
    public String getDescription() {
        return "Encoder (distance travelled)";
    }

    @Override
    public String getValue() {
        return valueFormat.format(distanceTravelled);
    }

    /**
     * Returns the distance traveled by the robot since its initiation.
     *
     * @return a double which represents the distance traveled
     */
    public double getOutput() {
        return distanceTravelled;
    }

    @Override
    public Shape getRange() {
        return null;
    }
}



