
package simulation.geometry;

import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;


import java.util.LinkedList;

/**
 * The superest superclass of all in-game object. Provides hit-detection capability to these objects
 * @author Simon
 */
public class Entity extends Area {

    /**
     * Entity constructor which all in-game objects use
     *
     * @param s shape to create an entity from
     */
    public Entity(Shape s) {
        super(s);
    }

    /**
     * Creates a circle.
     * 
     * @param x x of circle centre
     * @param y y of circle centre
     * @param r radius of circle
     */
    public Entity(final double x, final double y, final double r) {
        super(new Ellipse2D.Double(x - r, y - r, 2 * r, 2 * r));
    }

    /**
     * Creates a rectangular XPolygon
     * 
     * @param x x translation from the origin.
     * @param y y translation from the origin.
     * @param width the rectangle's width
     * @param height the rectangle's height
     */
    public Entity(final double x, final double y, final double width, final double height) {
        super(new Rectangle2D.Double(x, y, width, height));
    }

    /**
     * Creates a line
     *
     * @param x1 x value of first point
     * @param y1 y value of first point
     * @param x2 x value of 2nd point
     * @param y2 y value of 2nd point
     * @param nothing used to distinguish between rectangle and line
     */
    public Entity(final double x1, final double y1, final double x2, final double y2, final double nothing) {
        super(new Line2D.Double(x1, y1, x2, y2));
    }

    /**
     * Creates an ellipse.
     * 
     * @param x x value of first point
     * @param y y value of first point
     * @param r1 radius of horizontal axis
     * @param r2 radius of vertical axis
     * @param ellipse used to distinguish between shapes 
     */
    public Entity(final double x, final double y, final double r1, final double r2, final int ellipse) {
        super(new Ellipse2D.Double(x - r1, y - r2, 2 * r1, 2 * r2));
    }

    /**
     * Creates a 2D arc shape.
     * 
     * @param x x value of first point
     * @param y y value of first point
     * @param w width of arc
     * @param h height of arc
     * @param start start of arc
     * @param extent extent of arc
     * @param type type of arc
     */
    public Entity(double x, double y, double w, double h, double start, double extent, int type) {
        super(new Arc2D.Double(x, y, w, h, start - 90, -extent, type));
    }

    /**
     * Get the description of a shape
     * @return The description of the shape
     */
    public String getDescription() {
        return "this object has not been described";
    }

    /**
     * Checks if a given shape is completely enclosed by this one
     * @param shape The shape to check
     * @return true if the shape is contained
     */
    public boolean contains(Entity shape) {
        Area thisArea  = new Area(this);
        Area shapeArea = new Area(shape);

        shapeArea.subtract(thisArea);

        if (shapeArea.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the shape of entity
     * @return This shape as a LinkedList of XPoints
     */
    public LinkedList<XPoint> getPolygon() {
        final PathIterator iter   = getPathIterator(null);
        double[]           coords = new double[6];
        LinkedList<XPoint> points = new LinkedList<XPoint>();

        while (!iter.isDone()) {
            iter.currentSegment(coords);
            points.add(new XPoint(coords[0], coords[1]));
            iter.next();
        }

        return points;
    }

    /**
     * Hit detection between two RigidBodies
     * @param shape The other shape to test against
     * @return A collision object representing a collision which may or may not
     * have occurred
     */
    public Collision incidenceAngleCollision(final RigidBody shape) {
        Collision  collision;
        final Area remainder = new Area(this);
        final Area otherArea = new Area(shape);

        remainder.intersect(otherArea);

        if (!remainder.isEmpty()) {
            double aoi = findAngleOfCoincidentLine(remainder);

            collision = new Collision(aoi + Math.PI / 2 - shape.getAngle());
        } else {
            collision = new Collision();
        }

        return collision;
    }

    /**
     * Hit detection between another shape and the complement of this shape
     * @param shape The other shape to test against
     * @return A collision object representing a collision which may or may not
     * have occurred
     */
    public Collision internalIncidenceAngleCollision(RigidBody shape) {
        Collision collision;
        Area      remainder = new Area(this);
        Area      object    = new Area(shape);

        remainder.intersect(object);
        remainder.exclusiveOr(object);

        if (remainder.isEmpty()) {
            collision = new Collision();
        } else {
            double aoi = findAngleOfCoincidentLine(remainder);

            collision = new Collision(aoi + Math.PI / 2 - shape.getAngle());
        }

        return collision;
    }

    /**
     * Hit detection between two RigidBodies
     * @param rhs The other shape to test against
     * @return True if the two shapes intersect, false otherwise
     */
    public boolean intersects(final Entity rhs) {
        final Area area1 = new Area(this);
        final Area area2 = new Area(rhs);

        area1.intersect(area2);

        return !area1.isEmpty();
    }

    /**
     * Given a shape with at least one point coincident with the outline of this
     * shape, the method will return the angle at which that line runs.
     * @param remainder A shape with at least one coincident point with this shape
     * @return The angle of the found line, or NaN if none found
     */
    private double findAngleOfCoincidentLine(Area remainder) {
        double             out             = java.lang.Double.NaN;
        LinkedList<XPoint> collisionPoints = new RigidBody(remainder, null).getPolygon();
        LinkedList<XPoint> outlinePoints   = this.getPolygon();

searchLoop:
        for (XPoint point : collisionPoints) {
            XPoint prev = outlinePoints.getLast();

            for (XPoint current : outlinePoints) {
                if (point.isOnLine(current, prev)) {
                    out = current.getAngleTo(prev);

                    break searchLoop;
                }

                prev = current;
            }
        }

        return out;
    }

    /**
     * Adjusts the location based on position of sensor on robot
     * @param location location of the robot
     * @param offset location of the sensor on robot
     * @param angle angle of the robot
     * @return new location of the sensor with respect to global coordinates
     */
    public static XPoint adjustOffsetLocation(XPoint location, XPoint offset, double angle) {
        double newX = location.getX() + offset.getX() * Math.cos(angle) - offset.getY() * Math.sin(angle);
        double newY = location.getY() - offset.getY() * Math.cos(angle) + offset.getX() * Math.sin(angle);

        return new XPoint(newX, newY);
    }

    /**
     * changes the entity shape to a new shape
     * 
     * @param newShape the new shape for the entity
     */
    void setShape(Entity newShape) {
        super.reset();
        super.add(newShape);
    }
}



