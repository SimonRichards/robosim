package simulation.sensors;

import simulation.geometry.Environment;
import simulation.geometry.Entity;
import simulation.geometry.RigidBody;
import simulation.geometry.XPoint;
import simulation.entities.Cup;
import simulation.entities.Robot;
import java.awt.Polygon;
import java.awt.Shape;
import java.util.Collection;

/**
 * Replicates the function of a web camera with basic filtering software to detect objects.
 * Like a real camera, if other things are in the way then the object isn't seen.
 * It scans a triangular area in front of the camera.
 * @author Sam Sanson, Ben
 *
 * @version 1.0 05.07.2011
 * @version 1.1 07.07.2011
 * @version 1.2 18.07.2011
 */
public class RobotCamera extends Entity implements Sensor {
    private double    angle;          // The current angle of the sensor
    private double    cupAngle;       // The angle to the cup relative to the host
    private double    cupDist;        // The distance to the cup from the sensor
    private XPoint    location;       // The current location of the sensor
    private double    maxDist;        // Max distance of the scan area (triangle shape)
    private double    offsetAngle;    // The angle of the sensor relative to its host
    private RigidBody scanArea;
    private RigidBody scanAreaOriginal;

    /**
     * Creates a camera positioned at the specified angle to the host.
     * @param offsetAngle the angle of the sensor relative to the host
     * @param maxWidth
     * @param maxDist
     */
    public RobotCamera(double offsetAngle, double maxWidth, double maxDist) {
        super(0, 0, maxWidth, maxDist);
        this.offsetAngle = offsetAngle;
        this.maxDist     = maxDist;

        // Create ScanArea
        int[] xpoints = { 0, (int) (-maxWidth / 2), (int) (maxWidth / 2) };
        int[] ypoints = { 0, (int) (maxDist), (int) (maxDist) };
        int   npoints = 3;

        this.scanAreaOriginal = new RigidBody(new Polygon(xpoints, ypoints, npoints), new XPoint(0, 0));
    }

    /**
     * Analyses the simulation in front of the robot to detect the closest 
     * 
     * @param env The environment to analyse
     * @param robots 
     * @param things
     */
    @Override
    public void analyse(Environment env, Collection<Robot> robots, Collection<Cup> things) {
        cupDist  = maxDist;
        cupAngle = 0;

        double tempDist;     // The distance from the sensor to the cup
        double tempAngle;    // The angle of tempDist relative to the environment

        // Create the scan area triangle
        scanArea = new RigidBody(scanAreaOriginal, new XPoint(0, 0));
        scanArea.place(location.getX(), location.getY());
        scanArea.rotate(angle);

        // Find if this intersects with any cups
        for (Robot robot : robots) {
            if (robot.getCom() != location) {
                if (scanArea.intersects(robot)) {
                    XPoint cupPoint = robot.getCom();

                    tempDist  = location.distance(cupPoint);
                    tempAngle = Math.atan2(cupPoint.getX() - location.getX(), cupPoint.getY() - location.getY())
                                + angle;

                    boolean obstruction = isObstruction(env, robots, things, cupPoint);

                    if (!obstruction) {
                        if (tempDist < cupDist) {
                            cupDist  = tempDist;
                            cupAngle = tempAngle;
                        }
                    }
                }
            }
        }

        // Change distance to zero if no cup found
        if (cupDist == maxDist) {
            cupDist = 0;
        }

        // Convert angle to between -pi to pi
        while (cupAngle < -Math.PI) {
            cupAngle += 2 * Math.PI;
        }

        while (cupAngle > Math.PI) {
            cupAngle -= 2 * Math.PI;
        }
    }

    /**
     * Checks if there is anything between the specified point and the sensor.
     * @param env the environment to check
     * @param robots the robots to check
     * @param things the things to check (NOTE: Currently not implemented)
     * @param cupPoint the point to check between
     * @return true if there is a shape in the way, false otherwise
     */
    private boolean isObstruction(Environment env, Collection<Robot> robots, Collection<Cup> things,
                                  XPoint cupPoint) {
        // Find structures in the way
        for (Entity terrain : env.getImpassableTerrain()) {
            if (location.intersectsPolygon(terrain, cupPoint)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * @inheritdoc 
     */
    @Override
    public void setObject(SensorAble obj) {
        this.location = obj.getCom();
        this.angle    = obj.getAngle() + offsetAngle;
    }

    /**
     * @inheritdoc 
     */
    public String getDescription() {
        return "Object detection web cam (distance, angle)";
    }

    /**
     * @inheritdoc
     */
    @Override
    public String getValue() {
        String result;

        result = "(" + valueFormat.format(cupDist) + "," + valueFormat.format(cupAngle) + ")";

        return result;
    }

    /**
     * Returns an array containing the distance and angle (relative to the host) of a cup.
     * @return the distance and angle to the closest cup (relative to the host).
     * If no cup is present then distance and angle is zero. Angle is always between -pi and pi.
     */
    public double[] getOutput() {
        double[] output = { cupDist, cupAngle };

        return output;
    }

    /**
     * @return true if the camera detects a cup
     */
    public boolean isCup() {
        if ((cupDist == 0) && (cupAngle == 0)) {
            return false;
        }

        return true;
    }

    /**
     * @inheritdoc 
     */
    @Override
    public Shape getRange() {
        return scanArea;
    }
}



