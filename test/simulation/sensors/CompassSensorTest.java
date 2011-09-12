package simulation.sensors;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests CompassSensor class
 * @version 1.0
 * @version 1.1 5.6.2011
 * @version 1.2 7.7.2011
 * @author Sam Sanson
 * @author Joshua Jordan
 */
public class CompassSensorTest {

    /**
     * Test of getOutput method, of class CompassSensor.
     */
    @Test
    public void testGetOutput() {
        System.out.println("getOutput");

        CompassSensor      instance = new CompassSensor();
        double             angle    = 1.56;
        SensorTestingRobot robot    = new SensorTestingRobot();

        robot.setAngle(angle);
        instance.setObject(robot);
        instance.analyse(null, null, null);
        assertEquals(1.56, instance.getOutput(), 0.3);
    }

    /**
     * Test of getOutput method, of class CompassSensor with noise.
     */
    @Test
    public void testNoisySensor() {
        System.out.println("getOutput with noise");

        CompassSensor      instance = new CompassSensor(0.2);
        double             angle    = 1.56;
        SensorTestingRobot robot    = new SensorTestingRobot();

        robot.setAngle(angle);
        instance.setObject(robot);
        instance.analyse(null, null, null);
        assertEquals(1.56, instance.getOutput(), 1.0);
    }
}



