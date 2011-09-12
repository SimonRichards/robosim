package simulation.sensors;

import simulation.geometry.Environment;
import simulation.geometry.Entity;
import simulation.geometry.RigidBody;
import simulation.geometry.XPoint;
import simulation.entities.Cup;
import simulation.entities.Robot;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.Collection;

/**
 * Replicates the function of a web camera with basic filtering software to detect objects.
 * Like a real camera, if other things are in the way then the object isn't seen.
 * It scans a triangular area in front of the camera. Can detect cups, blocks and robots
 *
 * @author Sam Sanson, Ben Snalam, Jermin Tiu
 *
 * @version 1.0 05.07.2011
 * @version 1.1 07.07.2011
 * @version 1.2 18.07.2011
 * @version 2.0 20.07.2011
 */
public class Camera extends Entity implements Sensor {
    private final static double DEFAULT_DIST  = 500;
    private final static double DEFAULT_WIDTH = 1000;
    private CameraTarget        objToFind     = CameraTarget.CUPS;
    private double              angle;          // The current angle of the sensor
    private XPoint              location;       // The current location of the sensor
    private double              maxDist;        // Max distance of the scan area (triangle shape)
    private SensorAble          obj;            // The host of the sensor
    private double              objAngle;       // The angle to the cup relative to the host
    private double              objDist;        // The distance to the cup from the sensor
    private double              offsetAngle;    // The angle of the sensor relative to its host
    private RigidBody           scanArea;       // The shape representing the area seen by the camera
    private RigidBody           scanAreaOriginal;
    private XPoint              target;

    /**
     * Contains the objects that one can scan for. Current objects are CUPS,ROBOTS or BLOCKS.
     *
     * To make changes, need to edit obstruction method, switch case
     */
    public enum CameraTarget { CUPS, ROBOTS, UPRIGHT_CUPS, FALLEN_CUPS }

    ;
    public Camera() {
        this(0, DEFAULT_WIDTH, DEFAULT_DIST);
    }

    /**
     * Creates a camera positioned at the specified angle to the host.
     * @param offsetAngle the angle of the sensor relative to the host
     * @param maxWidth the maximum width that the camera can see
     * @param maxDist the maximum distance forward the camera can see
     */
    public Camera(double offsetAngle, double maxWidth, double maxDist) {
        super(0, 0, maxWidth, maxDist);
        this.offsetAngle = offsetAngle;
        this.maxDist     = maxDist;

        // Create ScanArea
        int[] xpoints = { 0, (int) (-maxWidth / 2), (int) (maxWidth / 2) };
        int[] ypoints = { 0, (int) (maxDist), (int) (maxDist) };
        int   npoints = 3;

        this.scanAreaOriginal = new RigidBody(new Polygon(xpoints, ypoints, npoints), new XPoint(0, 0));
        objDist               = maxDist;
        objAngle              = 0;
    }

    @Override
    public void analyse(Environment env, Collection<Robot> robots, Collection<Cup> things) {
        objDist  = maxDist;
        objAngle = 0;

        // Create the scan area triangle
        scanArea = new RigidBody(scanAreaOriginal, new XPoint(0, 0));
        scanArea.place(location.getX(), location.getY());
        scanArea.rotate(angle);

        Collection<? extends RigidBody> t;

        switch (objToFind) {
        case CUPS :
        case FALLEN_CUPS :
        case UPRIGHT_CUPS :
            t = things;

            break;

        case ROBOTS :
            t = robots;

            break;

        default :
            t = things;
        }

        RigidBody item = findNearestInCollection(t, scanArea, env, robots, things);

        if (item == null) {
            return;
        }

        objDist  = location.distance(item.getCom());
        objAngle = Math.atan2(item.getCom().getX() - location.getX(), item.getCom().getY() - location.getY()) + angle;
        target   = item.getCom();

        // Change distance to zero if no cup found
        if (objDist == maxDist) {
            objDist = 0;
        }

        // Convert angle to between -pi to pi
        while (objAngle < -Math.PI) {
            objAngle += 2 * Math.PI;
        }

        while (objAngle > Math.PI) {
            objAngle -= 2 * Math.PI;
        }
    }

    /**
     * Finds the nearest thing in the collection.
     * @param col The collection of items to be scanned.
     * @param scanArea The area to scan.
     * @param env The enviroment which contains the collection
     * @param robots The robots to check
     * @param things The things to check
     * @return RigidBody the nearest object.
     */
    public RigidBody findNearestInCollection(Collection<? extends RigidBody> col, Entity scanArea,
            Environment env, Collection<Robot> robots, Collection<Cup> things) {
        RigidBody item    = null;
        double    closest = maxDist;
        double    tempDist;    // The distance from the sensor to the cup


        for (RigidBody t : col) {
            if (t instanceof Cup) {                  // Special cases for inanimates
                Cup x = (Cup) t;

//                if (x.isCollectable() != collectable) {    // if what we've found is not what we're looking for
//                    continue;
//                } //TODO

                // Check for fallen or upright cups
                if (((objToFind == CameraTarget.UPRIGHT_CUPS) &&!x.isUpright())
                        || ((objToFind == CameraTarget.FALLEN_CUPS) && x.isUpright())) {
                    continue;
                }
            }

            if (t instanceof Robot) {    // skip if looking at self
                Robot x = (Robot) t;

                if (x.equals(obj)) {
                    continue;
                }
            }

            // Find if the cup intersects the scan area
            if (scanArea.intersects(t)) {
                XPoint objCom = t.getCom();

                tempDist = location.distance(objCom);

                boolean obstructed = isObstruction(env, robots, things, objCom);

                if ((!obstructed) && (tempDist < closest)) {
                    closest = tempDist;
                    item    = t;
                }
            }
        }

        return item;
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
        boolean findingCup = objToFind != objToFind.ROBOTS;

        // Find robots in the way
        if (objToFind != CameraTarget.ROBOTS) {
            for (Robot robot : robots) {
                if ((!robot.equals(obj)) && (location.intersectsPolygon(robot, cupPoint))) {
                    return true;
                }
            }
        }

        for (Cup cup : things) {
            if (location.intersectsPolygon(cup, cupPoint)) {

                if ((findingCup)
                        && ((cup.isUpright() && (objToFind == objToFind.FALLEN_CUPS))
                            ||(!cup.isUpright() && (objToFind == objToFind.UPRIGHT_CUPS)))) {
                    return true;
                }
            }
        }

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
     *
     *
     * @param obj
     */
    @Override
    public void setObject(SensorAble obj) {
        this.location = obj.getCom();
        this.angle    = obj.getAngle() + offsetAngle;
        this.obj      = obj;
    }

    /**
     * @inheritdoc
     *
     *
     * @return
     */
    @Override
    public String getDescription() {
        return "Object detection web cam (distance, angle)";
    }

    /**
     * @inheritdoc
     */
    @Override
    public String getValue() {
        String result;

        result = "(" + valueFormat.format(objDist) + "," + "Angle: " + valueFormat.format(objAngle) + ")";

        return result;
    }

    /**
     * Returns an array containing the distance and angle (relative to the host) of a cup.
     * @return the distance and angle to the closest cup (relative to the host).
     * If no cup is present then distance and angle is zero. Angle is always between -pi and pi.
     */
    public double[] getOutput() {
        double[] output = { objDist, objAngle };

        return output;
    }

    /**
     * Get the distance to the object in the camera.
     * @return distance to target on camera. Returns maxDist otherwise
     */
    public double getDistToTarget() {
        return objDist;
    }

    /**
     * Get the angle to the object in the camera.
     * @return angle to target in camera
     */
    public double getAngToTarget() {
        return objAngle;
    }

    /**
     * Returns true if a target of the right type of object is within unobstructed view of the camera.
     *
     *
     * @return true if there is a valid target in the camera's range
     */
    public boolean hasTarget() {
        if ((objDist == maxDist) && (objAngle == 0)) {
            return false;
        }

        return true;
    }

    /**
     * Gets the scan area of this cup.
     * @return the area that the camera is looking at
     */
    @Override
    public Shape getRange() {

        Path2D.Double shape = new Path2D.Double(scanArea);

        if (hasTarget()) {
            shape.append(new Line2D.Double(location.getX(),location.getX(), target.getX(), target.getY()), false);
        }

        return shape;
    }

    /**
     * Sets the target object for the scanner.
     * @param obj Sets the type of object to scan for.
     */
    public void setTargetObject(CameraTarget obj) {
        this.objToFind = obj;
    }
}



