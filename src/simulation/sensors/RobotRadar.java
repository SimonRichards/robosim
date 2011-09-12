
package simulation.sensors;

import simulation.Simulator;
import simulation.geometry.Environment;
import simulation.geometry.Entity;
import simulation.geometry.RigidBody;
import simulation.geometry.XPoint;
import simulation.entities.Cup;
import simulation.entities.Robot;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.Collection;

/**
 * DistanceSensor.java
 * Detects walls and impassable objects and returns the shortest distance.
 *
 * @author Simon, Jermin
 * @Walkedthrough Ben
 * @Deskchecked
 */
public class RobotRadar extends Entity implements Sensor {
    private final static double DEFAULT_MAX    = 300;
    private XPoint              offsetLocation = new XPoint(0, 0);
    private XPoint              endPoint       = new XPoint(0, 0);
    private final XPoint        origin         = new XPoint(0, 0);
    private double              radarAngle     = 0;
    private double              radarRing1     = 0;
    private XPoint              location;

    // Sensor's maximum range.
    private double     maxRange;
    private double     radarRing2     = maxRange / 3;
    private double     radarRing3     = maxRange / 3 * 2;
    private SensorAble obj;
    private double     offsetAngle;
    private double[]   output;
    private RigidBody  range;

    /**
     * Constructs ...
     */
    public RobotRadar() {
        this(new XPoint(), DEFAULT_MAX);
    }

    /**
     * A new distance sensor mounted on the robot at the given angle.
     * @param range
     */
    public RobotRadar(final double range) {
        this(new XPoint(0, 0), range);
    }

    /**
     * A new distance sensor mounted on the robot at the given angle.
     * @param offsetLocation The sensor position on the robot
     */
    public RobotRadar(XPoint offsetLocation) {
        this(offsetLocation, DEFAULT_MAX);
    }

    /**
     * Creates a new radar sensor
     * @param x x-coordinate location 
     * @param y y-coordinate location
     */
    public RobotRadar(int x, int y) {
        this(new XPoint(x, y), DEFAULT_MAX);
    }

    /**
     * A new distance sensor mounted on the robot at the given angle.
     * @param offsetLocation The sensor position on the robot
     * @param range
     */
    public RobotRadar(XPoint offsetLocation, final double range) {
        super(new Entity(20, 20, 20));
        this.offsetLocation = offsetLocation;
        this.maxRange       = range;
        this.location       = new XPoint();
        this.range          = new RigidBody(0, 0, maxRange);
        this.radarRing1     = 0;
        this.radarRing2     = maxRange / 3;
        this.radarRing3     = maxRange / 3 * 2;
        output              = new double[2];
    }

    /**
     * @inheritdoc 
     */
    @Override
    public Shape getRange() {
        XPoint endPoint2;


        // TODO: This is being run before it is set to an object
        if (obj == null) {
            endPoint2 = new XPoint(0, 0);
        } else {
            endPoint2 = new XPoint(obj.getCom().getX(), obj.getCom().getY());
        }

        // Do the turning thingy for radar
        endPoint2.addVector(new XPoint(0, maxRange), (radarAngle) - offsetAngle);

        Shape shape = new Line2D.Double(endPoint2.getX(), endPoint2.getY(), location.getX(), location.getY());

        Path2D.Double shape2 = new Path2D.Double(shape);
        //Shape shape2 = new Path2D.double(shape);

        // Do the circle thingy for radar
        shape2.append(new Ellipse2D.Double(location.getX()-radarRing1, location.getY()-radarRing1, 2*radarRing1,2*radarRing1), false);
        shape2.append(new Ellipse2D.Double(location.getX()-radarRing2, location.getY()-radarRing2, 2*radarRing2,2*radarRing2), false);
        shape2.append(new Ellipse2D.Double(location.getX()-radarRing3, location.getY()-radarRing3, 2*radarRing3,2*radarRing3), false);

        if (isRobot()) {
            shape2.append(new Line2D.Double(location.getX(),location.getY(), endPoint.getX(), endPoint.getY()), false);
        }

        return shape2;
    }

    /**
     * Tests the environment walls and impassables and saves output to a class variable.
     * to geometric checks.
     * @param env The environment to scan.
     * @param robots
     * @param things
     */
    @Override
    public void analyse(final Environment env, final Collection<Robot> robots, final Collection<Cup> things) {
        radarRing1 = (radarRing1 > maxRange)
                     ? 0
                     : (radarRing1 + 20 * Simulator.DT);
        radarRing2 = (radarRing2 > maxRange)
                     ? 0
                     : (radarRing2 + 20 * Simulator.DT);
        radarRing3 = (radarRing3 > maxRange)
                     ? 0
                     : (radarRing3 + 20 * Simulator.DT);
        radarAngle = (radarAngle > maxRange)
                     ? 0
                     : (radarAngle + 4 * Simulator.DT);

        double[] temp = { maxRange, 0 };

        output = temp;

        for (Robot robot : robots) {
            if ((!robot.equals(obj)) && (range.intersects(robot))) {
                temp[0] = location.distance(robot.getCom());
                temp[1] = location.getAngleTo(robot.getCom()) - offsetAngle;

                if (temp[0] <= output[0]) {
                    endPoint = robot.getCom();
                    output   = temp;
                }
            }
        }

        while (output[1] < -Math.PI) {
            output[1] += 2 * Math.PI;
        }

        while (output[1] > Math.PI) {
            output[1] -= 2 * Math.PI;
        }
    }

    /**
     * @inheritdoc
     */
    @Override
    public String getValue() {
        return "" + isRobot();
    }

    /**
     * Returns the sensor's result. [0] is the distance, [1] is the angle
     * @return The distance to the closest object.
     */
    public double[] getOutput() {
        return output;
    }

    /**
     * Get the distance to the robot on the radar
     * @return distance to target on radar. Returns maxDist otherwise
     */
    public double getDistToTarget() {
        return output[0];
    }

    /**
     * Get the angle to the robot on the radar
     * @return angle to target on radar
     */
    public double getAngToTarget() {
        return output[1];
    }

    /**
     * To be passed in before calling <code>analyse()</code>.
     * @param a object that is sensorable
     */
    @Override
    public void setObject(final SensorAble obj) {
        location.setLocation(obj.getCom());
        location.addVector(offsetLocation, obj.getAngle());
        offsetAngle = obj.getAngle();
        range.setCom(origin);
        range.place(obj.getCom());
        range.setCom(obj.getCom());
        this.obj = obj;
    }

    /**
     * @inheritdoc 
     */
    @Override
    public String getDescription() {
        return "Radar (distance)";
    }

    /**
     * @return The max range of the robot radar.
     */
    public double getMaxRange() {
        return this.maxRange;
    }

    /**
     * @return true if the sensor finds a robot
     */
    public boolean isRobot() {
        if ((output[0] >= maxRange) && (output[1] == 0)) {
            return false;
        }
        return true;
    }
}



