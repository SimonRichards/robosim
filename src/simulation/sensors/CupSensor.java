package simulation.sensors;

import simulation.geometry.Environment;
import simulation.geometry.Entity;
import simulation.geometry.Terrain;
import simulation.geometry.XPoint;
import simulation.entities.Cup;
import simulation.entities.Robot;
import java.awt.Shape;
import java.util.Collection;

/**
 * CupSensor.java
 * Detects cups in a straight line from the sensor. Cups are not detected if another
 * object is in the way.
 *
 * @author Sam Sanson
 * @Walkedthrough
 * @DeskChecked
 */
public class CupSensor extends Entity implements Sensor {
    private double     MAX = 1000;     // The range of the sensor
    private double     angle;          // The current angle of the sensor
    private XPoint     location;       // The current location of the sensor
    private SensorAble obj;            // The host of the sensor
    private double     offsetAngle;    // The angle of the sensor relative to its host
    private boolean    output;         // The result after analysis of the arena

    /**
     * Constructor for a new sensor that detects cups.
     * @param offsetAngle the angle of the sensor relative to its host
     * @param max the maximum range of the sensor
     */
    public CupSensor(double offsetAngle, double max) {
        super(new Entity(20, 20, 20));
        this.offsetAngle = offsetAngle;
        this.MAX         = max;
    }

    /**
     * @inheritdoc
     *
     * @param env
     * @param robots
     * @param things
     */
    @Override
    public void analyse(Environment env, Collection<Robot> robots, Collection<Cup> things) {
        double closestDist = MAX;
        double temp;

        output = false;

        // Find the closest robot excluding itself
        for (Robot robot : robots) {
            if (!robot.equals(obj)) {
                temp = location.distanceToPolygon(robot, angle, MAX);

                if (temp < closestDist) {
                    closestDist = temp;
                }
            }
        }

        // Find the closest solid object
        for (Terrain terrain : env.getImpassableTerrain()) {
            temp = location.distanceToPolygon(terrain, angle, MAX);

            if (temp < closestDist) {
                closestDist = temp;
            }
        }

        // Find the edge of the arena
        temp = location.distanceToPolygon(env, angle, MAX);

        if (temp < closestDist) {
            closestDist = temp;
        }

        // Determine if there is a cup closer than anything previous
        for (Cup thing : things) {
            temp = location.distanceToPolygon(thing, angle, MAX);

            if (temp < closestDist) {
                closestDist = temp;
                output      = true;
            }
        }
    }

    @Override
    public void setObject(SensorAble obj) {
        this.location = obj.getCom();
        this.angle    = obj.getAngle() + offsetAngle;
        this.obj      = obj;
    }

    @Override
    public String getDescription() {
        return "Detects inaniamates (detected)";
    }

    @Override
    public String getValue() {
        return String.valueOf(output);
    }

    /**
     * Returns the sensors result. Should only be called after <code>analyse</code>.
     * @return true if a cup is in line with the sensor, within range and no other objects are in the way. False otherwise.
     */
    public boolean getOutput() {
        return output;
    }

    @Override
    public Shape getRange() {
        return null;
    }
}



