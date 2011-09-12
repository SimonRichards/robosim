
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package simulation.sensors;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import simulation.geometry.Environment;
import simulation.geometry.RigidBody;
import simulation.geometry.XPoint;

import simulation.entities.Cup;

import simulation.entities.Robot;

import static org.junit.Assert.*;

import java.awt.Shape;

import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author Jermin
 */
public class EncoderTest {

    /**
     * Constructs ...
     *
     */
    public EncoderTest() {}

    /**
     * Method description
     *
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {}

    /**
     * Method description
     *
     *
     * @throws Exception
     */
    @AfterClass
    public static void tearDownClass() throws Exception {}

    /**
     * Method description
     *
     */
    @Before
    public void setUp() {}

    /**
     * Method description
     *
     */
    @After
    public void tearDown() {}

    /**
     * Test of analyse method, of class Encoder.
     */
    @Test
    public void testGetOutput() {
        System.out.println("test getOutput");

        // Setup test objects
        Collection<Cup> things = new LinkedList<Cup>();

        // Make a robot to hold the sensor
        SensorTestingRobot robot = new SensorTestingRobot(0, new XPoint(0, 10), 0);

        // Set the parameters of the sensor
        // Start position of the robot
        Encoder instance = new Encoder();

        instance.setObject(robot);

        // move robot 90
        robot.setLocation(0, 100);
        instance.setObject(robot);

        // Check encoder value
        assertEquals(100, instance.getOutput(), 1);

        // move robot 90
        for (int i = 0; i <= 100; i++) {
            robot.setLocation(10 + i, 100);
        }

        instance.setObject(robot);

        // Check encoder value
        assertEquals(210, instance.getOutput(), 1);
    }
}



