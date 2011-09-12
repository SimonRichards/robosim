package simulation.geometry;

import java.awt.Robot;
import simulation.Simulator;

import simulation.entities.Cup;

import java.awt.Shape;
import java.awt.geom.AffineTransform;


/**
 *
 * @author Sam Sanson
 * @author Simon
 *
 * @version 1.1 08.07.2011
 */
public class RigidBody extends Entity {
    private double angle            = 0;
    private XPoint com              = new XPoint();

    /**
     * Constructor for RigidBody.
     *
     * @param shape shape of the rigid body
     */
    public RigidBody(final RigidBody shape) {
        super(shape);
        com   = new XPoint(shape.getCom());
        angle = shape.getAngle();
    }

    /**
     * Creates a RigidBody from a shape and a point.
     * 
     * @param shape The shape used to create RigidBody
     * @param com The centre of mass of the RigidBody
     */
    public RigidBody(final Shape shape, final XPoint com) {
        super(shape);
        this.com = com;
    }

    /**
     * Creates a line.
     *
     * @param pt1 first point which is start of line.
     * @param pt2 second point which is end of line.
     */
    public RigidBody(final XPoint pt1, final XPoint pt2) {
        super(pt1.getX(), pt1.getY(), pt2.getX(), pt2.getY(), 0.0);
        this.com = pt1;
    }

    /**
     * Creates a circle at the specified coordinates.
     * 
     * @param x the x center of the circle
     * @param y the y center of the circle
     * @param radius the radius
     */
    public RigidBody(final double x, final double y, final double radius) {
        super(x, y, radius);
        this.com = new XPoint(x, y);
    }

    /**
     * Creates a RigidBody from a shape and a COM location.
     * 
     * @param shape The shape to copy
     * @param x the x-coordinate of the COM.
     * @param y the x-coordinate of the COM.
     */
    private RigidBody(Shape shape, final double x, final double y) {
        super(shape);
        com = new XPoint(x, y);
    }

    /**
     * Creates a rectangular RigidBody.
     *
     * @param x start x coordinate
     * @param y start y coordinate
     * @param width width of rectangle
     * @param height height of rectangle
     */
    public RigidBody(final double x, final double y, final double width, final double height) {
        super(x, y, width, height);
        this.com = new XPoint(x + width / 2, y + height / 2);
    }

    /**
     * Creates a circle at the specified coordinates.
     * 
     * @param x the x center of the circle
     * @param y the y center of the circle
     * @param rWidth radial width of ellipse
     * @param rHeight radial height of ellipse
     * @param ellipse
     */
    public RigidBody(final double x, final double y, final double rWidth, final double rHeight, final int ellipse) {
        super(x, y, rWidth, rHeight, ellipse);
        this.com = new XPoint(x, y);
    }

    /**
     * @inheritdoc
     */
    public RigidBody(double x, double y, double w, double h, double start, double extent, int type) {
        super(x, y, w, h, start, extent, type);
        this.com = new XPoint(x, y);
    }

    /**
     * Returns the COM of the RigidBody
     *
     * @return COM
     */
    public XPoint getCom() {
        return com;
    }

    /**
     * Sets the COM of the RigidBody
     *
     * @param XPoint Com
     */
    public void setCom(XPoint Com) {
        this.com = Com;
    }

    /**
     * Rotates the polygon about its com by the angle specified.
     * @param angle The required angle of rotation.
     */
    public void rotate(final double angle) {
        transform(AffineTransform.getRotateInstance(angle, getX(), getY()));
        this.angle += angle;
    }

    /**
     * Rotates the polygon by a specified angle about a specified point
     * @param angle The required angle of rotation
     * @param point The point to rotate the polygon about
     */
    public void rotateAboutPoint(final double angle, final XPoint point) {
        transform(AffineTransform.getRotateInstance(angle, point.getX(), point.getY()));
        this.angle += angle;
    }

    /**
     * Places the polygon the specified distance from the origin.
     * @param newLocation the new location
     */
    public void place(final XPoint newLocation) {
        place(newLocation.getX(), newLocation.getY());
    }

    /**
     * Places the polygon the specified distance from the origin.
     * @param x X distance from the origin.
     * @param y Y distance from the origin.
     */
    public void place(final double x, final double y) {
        transform(AffineTransform.getTranslateInstance(x - getX(), y - getY()));
        com.setLocation(x, y);
    }

    /**
     * Relocates this shape relative to its current position.
     * @param x The x translation.
     * @param y The y translation.
     */
    public void translate(final double x, final double y) {
        transform(AffineTransform.getTranslateInstance(x, y));
        com.setLocation(com.getX() + x, com.getY() + y);
    }

    /**
     * The current angle of this shape.
     * @return angle of this shape.
     */
    public double getAngle() {
        return angle;
    }

    /**
     * @return The x-coordinate of the COM.
     */
    public double getX() {
        return com.getX();
    }

    /**
     * @return The x-coordinate of the COM.
     */
    public double getY() {
        return com.getY();
    }

    /**
     * Translates the shape in the x direction
     * @param x x-coordinate to translate to
     */
    public void setX(final double x) {
        place(x, getY());
    }

    /**
     * Translates the shape in the y direction
     * @param y y-coordinate to translate to
     */
    public void setY(final double y) {
        place(getX(), y);
    }

    /**
     * Sets the RigidBody to a shape.
     *
     * @param newShape the shape of the RigidBody
     */
    public void setShape(RigidBody newShape) {
        super.setShape(newShape);
        angle = newShape.getAngle();
        com   = newShape.getCom();
    }


    /**
     * Returns the translation required by a inanimate if it has collided with 
     * another inanimate.
     *
     * @param velocity velocity of inanimate.
     * @param angle angle of robot.
     * @return The translation required by the inanimate.
     */
    public XPoint cupCollision(double velocity, double angle) {
        double direction = ((velocity > 0)
                            ? 1
                            : -1);

        double xTrans = direction * velocity * Simulator.DT * Math.sin(angle);
        double yTrans = direction * velocity * Simulator.DT * Math.cos(angle);

        return new XPoint(xTrans, yTrans);
    }

    /**
     * Returns the translation required by a inanimate if it has collided with a shape.
     *
     * @param velocity velocity of inanimate.
     * @param robotAngle angle of robot.
     * @return The translation required by the inanimate.
     */
    public XPoint shapeCollision(double velocity, double robotAngle) {
        double direction = ((velocity > 0)
                            ? 1
                            : -1);
        double xTrans;
        double yTrans;

        if (direction == 1) {
            xTrans = -velocity * Simulator.DT * Math.sin(robotAngle);
            yTrans = velocity * Simulator.DT * Math.cos(robotAngle);
        } else {
            xTrans = velocity * Simulator.DT * Math.sin(robotAngle + Math.PI);
            yTrans = -velocity * Simulator.DT * Math.cos(robotAngle + Math.PI);
        }

        return new XPoint(xTrans, yTrans);
    }


    /**
     *
     * @param cup What is being collided into
     * @param criticalAngle If a cup is causing the collision this not needed so
     * make 0, but if a robot is causing the collision this is the critical angle
     * equal to Math.atan2(robot.getWidth(), robot.getLength())
     * @return a collision containing a boolean of if it occurred, and the angle
     * the target will react at
     */
    public Collision collideWith(final Cup cup, double criticalAngle) {
        Collision collision;
        double xDif = cup.getX() - getX();
        double yDif = cup.getY() - getY();
        double comAngle = Math.atan(xDif / yDif);
        double angleToNextObject;

        if (this.intersects(cup)) {

            if(Math.abs(comAngle) <= Math.abs(criticalAngle)) {
                angleToNextObject = this.getAngle();
            } else {
                if(comAngle < 0) {
                    angleToNextObject = getAngle() + Math.PI/2;
                } else {
                    angleToNextObject = getAngle() - Math.PI/2;
                }
            }

            collision = new Collision(angleToNextObject);
        } else {
            collision = new Collision();
        }

        return collision;
    }
}



