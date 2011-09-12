package simulation.geometry;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the XPoint Class
 *
 * @author Joshua
 * @version 1.1 06/06/2011
 *
 * @author Jermin
 * @version 1.0 22/05/2011
 *
 * @since 1.0
 */
public class XPointTest {

    /**
     * Tests testDistanceToPolygon method, in many ways.
     * <p>
     *
     */
    @Test
    public void testDistanceToPolygon() {
        System.out.println("distanceToPolygonOnLinePart1");

        /*
         *  Puts in a Rectangle with side length of 100. Puts an observer at (20,10)
         * Checks that the correct lengths are seen in at 0,90,180,270 degrees. Angles
         * are done in a CCW direction
         */

        // Play around with these values
        XPoint         location = new XPoint(20, 10);
        Entity shape    = new Entity(0, 0, 100, 100);
        double         MAX      = 1000.0;

        // Angle 0 or 2 PI
        double angle     = 0;
        double expResult = 90;
        double result    = location.distanceToPolygon(shape, angle, MAX);

        assertEquals(expResult, result, 0.5);

        // Angle Pi/2
        angle     = Math.PI / 2;
        expResult = 20;
        result    = location.distanceToPolygon(shape, angle, MAX);
        assertEquals(expResult, result, 0.5);

        // Angle PI
        angle     = Math.PI;
        expResult = 10;
        result    = location.distanceToPolygon(shape, angle, MAX);
        assertEquals(expResult, result, 0.5);

        // Angle 3 * PI/2 or 270degrees
        angle     = Math.PI / 2 * 3;
        expResult = 80;
        result    = location.distanceToPolygon(shape, angle, MAX);
        assertEquals(expResult, result, 0.5);

        /**
         * Test2 of distanceToPolygonOnLine method, of class XPoint.
         * <p>
         *
         * Puts a 20x20 box at (40,40)
         * Puts observer at (30,50),(70,50),(50,30),(50,70) (outside the box, looking in)
         *  Angles
         * are done in a CCW direction
         */
        shape     = new Entity(40, 40, 20, 20);
        expResult = 10;

        // Looking bottom up
        location = new XPoint(50, 30);
        angle    = 0;
        result   = location.distanceToPolygon(shape, angle, MAX);
        assertEquals(expResult, result, 0.5);

        // Looking to the left. Observer on the right of the box
        location = new XPoint(70, 50);
        angle    = Math.PI / 2;
        result   = location.distanceToPolygon(shape, angle, MAX);
        assertEquals(expResult, result, 0.5);

        // Observer on top, looking down
        location = new XPoint(50, 70);
        angle    = Math.PI;
        result   = location.distanceToPolygon(shape, angle, MAX);
        assertEquals(expResult, result, 0.5);

        // Observer to the left of box, looking right
        location = new XPoint(30, 50);
        angle    = Math.PI / 2 * 3;
        result   = location.distanceToPolygon(shape, angle, MAX);
        assertEquals(expResult, result, 0.5);
        shape = new Entity(600, 600, 100, 200);

        XPoint point = new XPoint(30, 30);

        angle = -Math.PI / 4;

        double max     = 1000;
        double timer1  = System.nanoTime();
        double answer1 = point.distanceToPolygon(shape, angle, max);

        timer1 = System.nanoTime() - timer1;
        assertEquals(806.1, answer1, 0.5);
        System.out.println("distanceToPolygon() took " + timer1 + "ns");
    }

    /**
     * Test of isOnLine method, of class XPoint.
     */
    @Test
    public void testIsOnLine() {

        // Test for true positive
        System.out.println("isOnLine");

        XPoint  endA      = new XPoint(1, 1);
        XPoint  endB      = new XPoint(5, 5);
        XPoint  instance  = new XPoint(2, 2);
        boolean expResult = true;
        boolean result    = instance.isOnLine(endA, endB);

        assertEquals(expResult, result);

        // Test for true negative
        endA      = new XPoint(0, 1);
        endB      = new XPoint(13, 5);
        instance  = new XPoint(2, 22);
        expResult = false;
        result    = instance.isOnLine(endA, endB);
        assertEquals(expResult, result);
    }

    /**
     * Test of getAngleTo method, of class XPoint.
     */
    @Test
    public void testGetAngleTo() {
        System.out.println("getAngleTo");

        XPoint point     = new XPoint(-5, 0);
        XPoint instance  = new XPoint(0, 0);
        double expResult = Math.PI / 2;
        double result    = instance.getAngleTo(point);

        assertEquals(expResult, result, 0.0);
        point     = new XPoint(5, 0);
        instance  = new XPoint(0, 0);
        expResult = -Math.PI / 2;
        result    = instance.getAngleTo(point);
        assertEquals(expResult, result, 0.0);
        point     = new XPoint(-5, 5);
        instance  = new XPoint(0, 0);
        expResult = Math.PI / 4;
        result    = instance.getAngleTo(point);
        assertEquals(expResult, result, 0.0);
    }
}



