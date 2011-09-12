package simulation.sensors;

import org.junit.Test;

import simulation.geometry.Environment;
import simulation.geometry.RigidBody;
import simulation.geometry.XPoint;

import simulation.entities.Cup;

import simulation.entities.Robot;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Performs multiple tests on the Camera.java class.
 * @author Sam
 */
public class CameraTest {

    /**
     * Test of analyse method, of class Camera, with multiple cups.
     */
    @Test
    public void testAnalyse() {
        System.out.println("analyse (multiple cups)");

        Environment           env    = new Environment(new RigidBody(0, 0, 500, 500));
        Collection<Robot>     robots = new LinkedList<Robot>();
        Collection<Cup> things = new LinkedList<Cup>();

        // Put cup at 2,40 and 10,150
        // things.add(new Inanimate(2, 40, 5, false));
        // things.add(new Inanimate(10, 150, 5, false));
        Cup cup = new Cup(50, 60, false);

        // put cup at 10,20
        things.add(cup);

        // Make a robot to hold the sensor
        SensorTestingRobot robot = new SensorTestingRobot(0, new XPoint(50, 50), 0);

        // Setup the sensor
        Camera instance = new Camera(0, 500, 200);

        instance.setObject(robot);

        // Look for a cup
        instance.analyse(env, robots, things);

        double[] output = instance.getOutput();

        assertEquals(60, cup.getCom().getY(), 0.1);
        assertEquals(cup.getCom().getY() - robot.getCom().getY(), output[0], 0.5);
        assertEquals(0, output[1], 0.1);

        // assertEquals(31.0, output[0], 0.1);
        // assertEquals(-0.2606, output[1], 0.0001);
    }

    /**
     * Test of analyse method, of class Camera, with hidden cup.
     */
    @Test
    public void testAnalyseHidden() {
        System.out.println("analyse (hidden cup)");

        Environment           env    = new Environment(new RigidBody(0, 0, 500, 500));
        Collection<Robot>     robots = new LinkedList<Robot>();
        Collection<Cup> things = new LinkedList<Cup>();

        // Make an impassable rectangle
        RigidBody shape   = new RigidBody(0, 25, 20, 15);

        env.createNewImpassableTerrain(shape);

        // Put a cup on the far side of the rectangle
        things.add(new Cup(10, 60, false));

        // Make a robot to hold the sensor
        SensorTestingRobot robot = new SensorTestingRobot(0, new XPoint(10, 10), 0);

        // Setup the sensor
        Camera instance = new Camera(0, 100, 200);

        instance.setObject(robot);

        // Look for a cup
        instance.analyse(env, robots, things);

        // No cup should be found
        double[] output = instance.getOutput();

        assertEquals(200, output[0], 0.1);
        assertEquals(0, output[1], 0.0001);
    }

    /**
     * Test of analyse method, of class Camera, with turned robot.
     */
    @Test
    public void testAnalyseRotated() {
        System.out.println("analyse (rotated robot)");

        Environment           env    = new Environment(new RigidBody(0, 0, 500, 500));
        Collection<Robot>     robots = new LinkedList<Robot>();
        Collection<Cup> things = new LinkedList<Cup>();

        // Make a cup
        things.add(new Cup(20, 20, false));

        // Make a robot at -45 degrees (should be pointing at cup)
        SensorTestingRobot robot = new SensorTestingRobot(-Math.PI / 4, new XPoint(10, 10), 0);

        // Setup the sensor
        Camera instance = new Camera(0, 100, 200);

        instance.setObject(robot);

        // Look for a cup
        instance.analyse(env, robots, things);

        // Cup should be found straight ahead
        double[] output = instance.getOutput();

        assertEquals(14.1, output[0], 0.1);
        assertEquals(0, output[1], 0.0001);
    }

    /**
     * Test of analyse method, of class Camera, with turned robot other direction.
     */
    @Test
    public void testAnalyseMuckAround() {
        System.out.println("analyse (rotated robot)");

        Environment           env    = new Environment(new RigidBody(0, 0, 500, 500));
        Collection<Robot>     robots = new LinkedList<Robot>();
        Collection<Cup> things = new LinkedList<Cup>();

        // Make a cup
        things.add(new Cup(0, 100, false));

        // Make a robot at 45 degrees (should be pointing at cup)
        SensorTestingRobot robot = new SensorTestingRobot(1 * Math.PI / 4, new XPoint(50, 50), 0);

        // Setup the sensor
        Camera instance = new Camera(0, 200, 200);

        instance.setObject(robot);

        // Look for a cup
        instance.analyse(env, robots, things);

        // Cup should be found straight ahead
        double[] output = instance.getOutput();

        assertEquals(70.7, output[0], 0.1);
        assertEquals(0, output[1], 0.0001);
    }
}



