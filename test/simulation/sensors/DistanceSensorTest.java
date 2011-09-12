package simulation.sensors;

import org.junit.Test;

import simulation.geometry.Environment;
import simulation.geometry.RigidBody;
import simulation.geometry.XPoint;

import simulation.entities.Cup;

import simulation.entities.Robot;

import static org.junit.Assert.*;

import java.util.LinkedList;

/**
 * Tests the DistanceSensor class
 *
 * @author Jermin
 * @version 1.0 22/05/2011
 * @version 1.1 05.06.2011
 * @version 1.2 29.06.2011
 * @version 1.3 17.07.2011
 *
 * @since 1.0
 */
public class DistanceSensorTest {

    /**
     * Test of analyse method, of class DistanceSensor. Test to detects the walls correctly.
     *
     */
    @Test
    public void testAnalyseWalls() {
        System.out.println("analyse walls");

        Environment env = new Environment(new RigidBody(0, 0, 500, 500));

        // set the robot parameters
        SensorTestingRobot robot = new SensorTestingRobot(0, new XPoint(10, 10), 0);

        // create the sensor
        DistanceSensor instance = new DistanceSensor(0, new XPoint(0, 0), 1000);

        instance.setObject(robot);
        instance.analyse(env, new LinkedList<Robot>(), new LinkedList<Cup>());
        assertEquals(490, instance.getOutput(), 0.0);
    }

    /**
     * Test of analyse method, of class DistanceSensor. Test to detect impassables
     */
    @Test
    public void testAnalyseImpassables() {
        System.out.println("analyse impassables");

        Environment env = new Environment(new RigidBody(0, 0, 500, 500));

        env.createNewImpassableTerrain(new RigidBody(0, 50, 50, 50));

        DistanceSensor instance = new DistanceSensor(Math.PI / 4, new XPoint(0, 0), 1000);

        // set the robot parameters
        SensorTestingRobot robot = new SensorTestingRobot(0, new XPoint(10, 10), 0);

        instance.setObject(robot);
        instance.analyse(env, new LinkedList<Robot>(), new LinkedList<Cup>());

        // System.out.println
        assertEquals(14.1, instance.getOutput(), 0.1);
        System.out.println(instance.getOutput());
    }
}



