package simulation.sensors;

import simulation.geometry.Environment;
import simulation.geometry.Entity;
import simulation.geometry.RigidBody;
import simulation.geometry.XPoint;
import simulation.entities.Cup;
import simulation.entities.Robot;
import java.awt.Shape;
import java.util.Collection;

/**
 * Creates a bumper with a rectangle, elliptical or circular shape. When the bumper is in contact
 * with another object it returns true.
 * @author Jermin
 * @version 1.0
 *
 * @since 1.0
 */
public class Bumper extends RigidBody implements Sensor {
    private boolean   output = false;
    private XPoint    location;
    private XPoint    myRobot;
    private double    objAngle;
    private double    offsetAngle;
    private XPoint    offsetLocation;
    private RigidBody scanArea;

    /**
     * Creates a circular bumper.
     * @param x x position relative to the center of the robot
     * @param y y position relative to the center of the robot
     * @param r radius of the bumper
     */
    public Bumper(double x, double y, double r) {
        super(new RigidBody(x, y, r));
        scanArea         = new RigidBody(this);
        offsetLocation   = new XPoint(x, y);
        this.offsetAngle = 0;
    }

    /**
     * Creates a bumper based on a shape.
     * @param x x position relative to the center of the robot
     * @param y y position relative to the center of the robot
     * @param angle orientation of the shape relative to the robot
     * @param s the shape to use for the bumper
     */
    public Bumper(double x, double y, double angle, Shape s) {
        super(s, new XPoint(x, y));
        scanArea         = new RigidBody(this);
        offsetLocation   = new XPoint(x, y);
        this.offsetAngle = angle;
    }

    /**
     * Creates a rectangular bumper.
     * @param x x position relative to the center of the robot
     * @param y y position relative to the center of the robot
     * @param width the width of the bumper
     * @param height the height of the bumper
     * @param angle the angle of the rectangle relative to the robot
     */
    public Bumper(double x, double y, double width, double height, double angle) {
        super(new RigidBody(x, y, width, height));
        scanArea         = new RigidBody(this);
        offsetLocation   = new XPoint(x, y);
        this.offsetAngle = angle;
    }

    /**
     * Creates a circular bumper.
     * @param x x position relative to the center of the robot
     * @param y y position relative to the center of the robot
     * @param h the height of the bumper
     * @param w the width of the bumper
     * @param angle the angle of the bumper relative to the robot
     */
    public Bumper(double x, double y, double h, double w, int angle) {
        super(new RigidBody(x, y, h, w, 1));
        scanArea         = new RigidBody(this);
        offsetLocation   = new XPoint(x, y);
        this.offsetAngle = angle;
    }

    @Override
    public void analyse(Environment env, Collection<Robot> robots, Collection<Cup> things) {
        output   = false;
        scanArea = new RigidBody(this);
        scanArea.place(location);
        scanArea.rotate(offsetAngle + objAngle);

        for (Robot r : robots) {
            if ((r.getCom() != myRobot) && (scanArea.intersects(r))) {
                output = true;
            }
        }

        for (Cup t : things) {
            if (scanArea.intersects(t)) {
                output = true;
            }
        }

        for (Entity terrain : env.getImpassableTerrain()) {
            if (scanArea.intersects(terrain) ||!env.contains(scanArea)) {
                output = true;
            }
        }
    }

    @Override
    public void setObject(SensorAble obj) {
        myRobot       = obj.getCom();
        objAngle      = obj.getAngle();
        this.location = new XPoint(obj.getCom());
        location.addVector(offsetLocation, obj.getAngle());
    }

    @Override
    public Shape getRange() {
        return scanArea;
    }

    @Override
    public String getValue() {
        return Boolean.toString(output);
    }

    @Override
    public String getDescription() {
        return "Bumper (Proximity)";
    }

    public boolean getOutput() {
        return output;
    }
}



