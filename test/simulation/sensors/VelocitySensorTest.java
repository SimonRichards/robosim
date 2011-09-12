
/**
 * VelocitySensorTest.java
 *
 * @author Sam Sanson
 * @author Jermin Tiu
 * @version 1.1 29.05.2011
 */
package simulation.sensors;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * VelocitySensorTest.java
 * Performs testing on the VelocitySensor.java class
 *
 * @author Sam Sanson
 * @author Jermin Tiu
 *
 * @version 1.1 26/05/2011
 * @version 1.2 05.06.2011
 * @version 1.3 01.07.2011
 *
 * @since 1.0
 */
public class VelocitySensorTest {

    /**
     * Test of getOutput method, of class VelocitySensor.
     */
    @Test
    public void testGetOutput() {
        System.out.println("getOutput");

        double velocity = 42;    // Cause thats the answer :)

        // Set up test environment
        VelocitySensor     instance = new VelocitySensor();
        SensorTestingRobot robot    = new SensorTestingRobot(0, null, 42);

        instance.setObject(robot);
        instance.analyse(null, null, null);

        // Check the answer is correct
        double expResult = velocity;
        double result    = instance.getOutput();

        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getOutput method, of class VelocitySensor with noise.
     */
    @Test
    public void testNoisyOutput() {
        System.out.println("getOutput with noise");

        double velocity = 42;    // Cause thats the answer :)

        // Set up test environment
        VelocitySensor     instance = new VelocitySensor(0.2);
        SensorTestingRobot robot    = new SensorTestingRobot(0, null, 42);

        instance.setObject(robot);
        instance.analyse(null, null, null);

        // Check the answer is correct
        double expResult = velocity;
        double result    = instance.getOutput();

        assertEquals(expResult, result, 42 * 0.2);
    }
}



