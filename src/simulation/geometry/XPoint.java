package simulation.geometry;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;

/**
 * This class represents an objects position
 *
 * @author Sam Sanson
 * @author Joshua Jordan, Simon Richards
 */
public class XPoint extends Point2D {
    private final static double EPS              = 0.001;
    private static final long   serialVersionUID = 1L;
    private double              x;
    private double              y;

    /**
     * Creates a new XPoint at the origin
     */
    public XPoint() {
        this(0, 0);
    }

    /**
     * Creates a new XPoint Copying the provided one.
     * @param toCopy The point to copy.
     */
    public XPoint(XPoint toCopy) {
        this(toCopy.x, toCopy.y);
    }

    /**
     * Creates a new XPoint at the specified coordinates
     * @param x The x-coord
     * @param y The y-coord
     */
    public XPoint(double x, double y) {
        super();
        this.x = x;
        this.y = y;
    }

    /**
     * @return The x-coord
     */
    @Override
    public double getX() {
        return x;
    }

    /**
     * @return The y-coord
     */
    @Override
    public double getY() {
        return y;
    }

    /**
     * Relocates the x point
     * @param x The new x-coord
     * @param y The new y-coord
     */
    @Override
    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Tests the ray on polygon intersection within a maximum range.
     * Ray is defined as (this.x, this.y) + g * (dx, dy), where g >=0
     * Line segment is defined as (ax, ay) + h * ( bx - ax, by - ay ) where 0<=h<= 1
     * @param shape The shape to test against
     * @param angle The angle of the ray
     * @param max The maximum distance to find an object at
     * @return  The minimum distance from this point to an edge of the polygon
     * or max if no polygon found inside range.
     */
    public double distanceToPolygon(Entity shape, double angle, double max) {
        double             min = max;
        double             distance;
        double             dx, dy, ax, ay, bx, by, g, h;
        LinkedList<XPoint> polygon      = shape.getPolygon();
        XPoint             intersection = new XPoint();
        XPoint             last         = polygon.getLast();

        // Find the vector (dx,dy)
        dy = Math.cos(angle);
        dx = -Math.sin(angle);

        // For each line segment in the polygon
        for (XPoint point : polygon) {
            ax = point.getX();
            ay = point.getY();
            bx = last.getX();
            by = last.getY();

            // Find scalars h and g as defined above
            h = (dx * (ay - y) + dy * (x - ax)) / (dy * (bx - ax) - dx * (by - ay));

            // Use the correct form of g in case dx==0 or dy ==0
            g = (Math.abs(dx) < EPS)
                ? (ay + h * (by - ay) - y) / dy
                : (ax + h * (bx - ax) - x) / dx;

            // If h and g meet the constraints
            if ((0 <= h) && (h <= 1.0) && (g >= 0)) {

                // Then find the distance between this and the intersection
                intersection.setLocation(x + g * dx, y + g * dy);
                distance = distance(intersection);

                // And save if it is smaller than the max range
                if (distance < min) {
                    min = distance;
                }
            }

            last = point;
        }

        return min;
    }

    /**
     * Returns a string representing this XPoint
     *
     * @return string representing this XPoint
     */
    @Override
    public String toString() {
        return "[" + String.valueOf(x) + "," + String.valueOf(y) + "]";
    }

    /**
     * Checks if this point is on a line defined by to points. Will return true
     * even if this point is outside of the endpoints
     * @param endA One end of the line segment
     * @param endB The other end of the line segment
     * @return True if the point is on the line, false otherwise
     */
    public boolean isOnLine(XPoint endA, XPoint endB) {
        final double epsilon = 0.0001;
        double       m       = (endA.getY() - endB.getY()) / (endA.getX() - endB.getX());

        if (new java.lang.Double(m).isInfinite()) {
            if (Math.abs(x - endA.getX()) < epsilon) {
                return true;
            } else {
                return false;
            }
        } else {
            double c = endA.getY() - m * endA.getX();

            if (Math.abs(y - (m * x + c)) < epsilon) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Calculates the angle from this to a given point
     * @param point The other point
     * @return The angle between the two points
     */
    public double getAngleTo(XPoint point) {
        return Math.atan2(x - point.getX(), point.getY() - y);
    }

    /**
     * Creates a vector at a defined angle.
     *
     * @param offset the offset of the vector from this
     * @param angle the angle relative to 'north'
     */
    public void addVector(XPoint offset, double angle) {
        setLocation(x + offset.getX() * Math.cos(angle) - offset.getY() * Math.sin(angle),
                    y + offset.getY() * Math.cos(angle) + offset.getX() * Math.sin(angle));
    }

    /**
     * Determines if there is a shape between this point and the specified end point
     * @param shape the shape to check for intersection
     * @param endPoint the end of the line
     * @return true if the line intersects the polygon, false otherwise
     */
    public boolean intersectsPolygon(Entity shape, XPoint endPoint) {
        double x1, y1, x2, y2;    // Polygon points

        // Create ray
        Line2D.Double      ray     = new Line2D.Double(this.x, this.y, endPoint.getX(), endPoint.getY());
        LinkedList<XPoint> polygon = shape.getPolygon();
        XPoint             last    = polygon.getLast();

        // For each line segment in the polygon
        for (XPoint point : shape.getPolygon()) {

            // Start of line
            x1 = point.getX();
            y1 = point.getY();
            x2 = last.getX();
            y2 = last.getY();

            // Create line of polygon
            Line2D.Double line = new Line2D.Double(x1, y1, x2, y2);

            // Check if the ray and line intersect
            if (ray.intersectsLine(line)) {
                return true;
            }

            last = point;
        }

        return false;
    }
}



