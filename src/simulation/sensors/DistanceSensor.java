package simulation.sensors;

import simulation.geometry.Environment;
import simulation.geometry.Entity;
import simulation.geometry.RigidBody;
import simulation.geometry.Terrain;
import simulation.geometry.XPoint;
import simulation.entities.Cup;
import simulation.entities.Robot;
import java.awt.Shape;
import java.util.Collection;
import java.util.Random;

/**
 * DistanceSensor.java
 * Detects walls and impassable objects and returns the shortest distance.
 *
 * @author Simon, Jermin
 * @Walkedthrough Ben
 * @Deskchecked
 */
public class DistanceSensor extends Entity implements Sensor {
    private final static double DEFAULT_MAX    = 800;               // The default range
    private final static double DEFAULT_NOISE  = 0.25;              // The default noise
    private XPoint              offsetLocation = new XPoint(0, 0);  // The location of the sensor relative to the robot
    private XPoint              endPoint       = new XPoint(0, 0);  // The end point of the range of the sensor
    private XPoint              endOffset      = new XPoint(0, 0);  // Offset of the end point for vectors
    private double              angle;                              // The current angle of the sensor
    private XPoint              location;                           // The current location of the sensor

    
    private double       maxRange;      // Sensor's maximum range.
    private Random       noise;         // The noise of the sensor
    private SensorAble   obj;           // Reference to the robot
    private final double offsetAngle;   // Offset relative to the robot
    private double       output;        // The result of after the sensor has analysed
    private double       uncertainty;   // Scalar for the nosie of the sensor

    /**
     *  A new distance sensor mounted on the robot facing forward
     */
    public DistanceSensor() {
        this(0);
    }

    /**
     * A new distance sensor mounted on the robot facing  at the specified angle
     *
     * @param offsetAngle the angle of the sensor relative to the robot
     */
    public DistanceSensor(final double offsetAngle) {
        this(offsetAngle, new XPoint(), DEFAULT_MAX, DEFAULT_NOISE);
    }

    /**
     * A new distance sensor mounted on the robot at the given angle.
     * @param offsetAngle The angle about which the sensor is offset from the robot
     * @param offsetLocation The sensor position on the robot
     */
    public DistanceSensor(final double offsetAngle, XPoint offsetLocation) {
        this(offsetAngle, offsetLocation, DEFAULT_MAX, 0);
    }

    /**
     * Constructs a distance sensor with the specified values. 
     * Default range is used and zero noise.
     *
     * @param offsetAngle The angle of the sensor relative to the robot
     * @param x the x position of the sensor
     * @param y the y position of the sensor
     */
    public DistanceSensor(final double offsetAngle, int x, int y) {
        this(offsetAngle, new XPoint(x, y), DEFAULT_MAX, 0);
    }

    /**
     * A new distance sensor mounted on the robot at the given angle.
     * @param offsetAngle The angle about which the sensor is offset from the robot.
     * @param offsetLocation The sensor position on the robot
     * @param range The range of the senor
     */
    public DistanceSensor(final double offsetAngle, XPoint offsetLocation, final double range) {
        this(offsetAngle, offsetLocation, range, 0);
    }

    /**
     * A new distance sensor mounted on the robot at the given angle.
     * @param offsetAngle The angle about which the sensor is offset from the robot.
     * @param offsetLocation The sensor position on the robot
     * @param range The range of the sensor
     * @param uncertainty The maximum percentage of error on this sensor
     */
    public DistanceSensor(final double offsetAngle, XPoint offsetLocation, final double range,
                          final double uncertainty) {
        super(new Entity(20, 20, 20));
        this.offsetAngle    = offsetAngle;
        this.offsetLocation = offsetLocation;
        this.maxRange       = range;
        this.uncertainty    = uncertainty;
        this.noise          = new Random();
        this.location       = new XPoint();
        this.endOffset      = new XPoint(offsetLocation.getX(), range);
    }

    @Override
    public Shape getRange() {
        return new RigidBody(location, endPoint);
    }

    @Override
    public void analyse(final Environment env, final Collection<Robot> robots, final Collection<Cup> things) {
        output = maxRange;

        double temp;

        for (Robot robot : robots) {
            if ((!robot.equals(obj)) && (location.intersectsPolygon(robot, endPoint))) {
                temp = location.distanceToPolygon(robot, angle, maxRange);

                if (temp < output) {
                    output = temp;
                }
            }
        }

        for (Cup thing : things) {
            if (location.intersectsPolygon(thing, endPoint)) {
                temp = location.distance(thing.getCom());

                // temp = location.distanceToPolygon(thing, angle, maxRange);
                if (temp < output) {
                    output = temp;
                }
            }
        }

        for (Terrain terrain : env.getImpassableTerrain()) {
            if (location.intersectsPolygon(terrain, endPoint)) {
                temp = location.distanceToPolygon(terrain, angle, maxRange);

                if (temp < output) {
                    output = temp;
                }
            }
        }

        temp = location.distanceToPolygon(env, angle, maxRange);

        if (temp < output) {
            output = temp;
        }

        // Add noise to the signal
        output += noise.nextGaussian() * uncertainty * output;
    }

    @Override
    public String getValue() {
        return valueFormat.format(this.getOutput());
    }

    /**
     * Returns the sensor's result.
     * @return The distance to the closest object.
     */
    public double getOutput() {
        return output;
    }

    /**
     * To be passed in before calling <code>analyse()</code>.
     *
     * @param obj
     */
    @Override
    public void setObject(final SensorAble obj) {
        location.setLocation(obj.getCom());
        location.addVector(offsetLocation, obj.getAngle());
        endPoint.setLocation(obj.getCom());
        endPoint.addVector(endOffset, obj.getAngle() + offsetAngle);
        this.angle = obj.getAngle() + offsetAngle;
        this.obj   = obj;
    }

    @Override
    public String getDescription() {
        return "Rangefinding sensor (distance)";
    }

    /**
     * Finds the maximum distance that an object can be detected at.
     * @return The maximum range of the DistanceSensor
     */
    public double getMaxRange() {
        return this.maxRange;
    }
}



