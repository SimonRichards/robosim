
/**
 * GPSTest.java
 *
 * @author Sam Sanson
 * @version 1.0 29.05.2011
 * @version 1.1 05.06.2011
 * @version 1.2 01.07.2011
 */
package simulation.sensors;

import org.junit.Test;

import simulation.geometry.XPoint;

import static org.junit.Assert.*;

/**
 * GPSTest.java
 * Performs testing on the GPS.java class.
 *
 * @author Sam Sanson
 * @version 1.0 29.05.2011
 * @version 1.1 05.06.2011
 * @version 1.2 01.07.2011
 */
public class GPSTest {

    /**
     * Test of getOutput method, of class GPS.
     */
    @Test
    public void testGetOutput() {
        System.out.println("getOutput");

        // Set up test data
        GPS                instance = new GPS();
        XPoint             location = new XPoint(10, 20);
        SensorTestingRobot robot    = new SensorTestingRobot(0, location, 0);

        instance.setObject(robot);

        // Check the values are correct
        assertEquals(10, instance.getOutput().getX(), 0.0);
        assertEquals(20, instance.getOutput().getY(), 0.0);
    }

    /**
     * Test of getOutput method, of class GPS with noise.
     */
    @Test
    public void testNoisyOutput() {
        System.out.println("getOutput with noise");

        // Set up test data
        GPS                instance = new GPS(0.2);
        XPoint             location = new XPoint(10, 20);
        SensorTestingRobot robot    = new SensorTestingRobot(0, location, 0);

        instance.setObject(robot);

        // Check the values are correct
        assertEquals(10, instance.getOutput().getX(), 10 * 0.2);
        assertEquals(20, instance.getOutput().getY(), 20 * 0.2);
    }
}



