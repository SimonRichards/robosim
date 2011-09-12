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
 * Performs a variety of tests on the CupSensor class
 * @author Sam Sanson
 * @version 1.0 05.07.2011
 */
public class CupSensorTest {

    /**
     * Test of analyse method, of class CupSensor, for a simple case.
     */
    @Test
    public void testAnalyse() {
        System.out.println("analyse simple case");

        // Setup test objects
        Environment           env    = new Environment(new RigidBody(0, 0, 500, 500));
        Collection<Robot>     robots = new LinkedList<Robot>();
        Collection<Cup> things = new LinkedList<Cup>();

        things.add(new Cup(10, 40, false));

        // Make a robot to hold the sensor
        SensorTestingRobot robot = new SensorTestingRobot(0, new XPoint(10, 10), 0);

        // Set the parameters of the sensor
        double offsetAngle = 0;
        double max         = 500;

        // Make the sensor
        CupSensor instance = new CupSensor(offsetAngle, max);

        instance.setObject(robot);

        // Look for cup
        instance.analyse(env, robots, things);
        assertEquals(true, instance.getOutput());
    }

    /**
     * Test of analyse method, of class CupSensor, for rotated robot and sensors.
     */
    @Test
    public void testAnalyseRotated() {
        System.out.println("analyse (rotated case)");

        // Setup test objects
        Environment           env    = new Environment(new RigidBody(0, 0, 500, 500));
        Collection<Robot>     robots = new LinkedList<Robot>();
        Collection<Cup> things = new LinkedList<Cup>();

        things.add(new Cup(2, 40, false));

        // Make a robot to hold the sensor
        SensorTestingRobot robot = new SensorTestingRobot(-0.26052044, new XPoint(10, 10), 0);

        // Set the parameters of the sensor
        double offsetAngle = 0;
        double max         = 500;

        // Make the sensor
        CupSensor instance = new CupSensor(offsetAngle, max);

        instance.setObject(robot);

        // Look for cup
        instance.analyse(env, robots, things);
        assertEquals(false, instance.getOutput());
    }

    /**
     * Test of analyse method, of class CupSensor, for rotated sensor and moved cup.
     */
    @Test
    public void testAnalyseRotated2() {
        System.out.println("analyse (rotated sensor, translated cup)");

        // Setup test objects
        Environment           env    = new Environment(new RigidBody(0, 0, 500, 500));
        Collection<Robot>     robots = new LinkedList<Robot>();
        Collection<Cup> things = new LinkedList<Cup>();

        things.add(new Cup(20, 50, false));

        // Make a robot to hold the sensor
        SensorTestingRobot robot = new SensorTestingRobot(0, new XPoint(10, 10), 0);

        // Set the parameters of the sensor
        double offsetAngle = -0.24491866;
        double max         = 500;

        // Make the sensor
        CupSensor instance = new CupSensor(offsetAngle, max);

        instance.setObject(robot);

        // Look for cup
        instance.analyse(env, robots, things);
        assertEquals(true, instance.getOutput());
    }

    /**
     * Test of analyse method, of class CupSensor, for hidden cup.
     */
    @Test
    public void testAnalyseConcealed() {
        System.out.println("analyse (hidden case)");

        // Setup test objects
        Environment           env    = new Environment(new RigidBody(0, 0, 500, 500));
        Collection<Robot>     robots = new LinkedList<Robot>();
        Collection<Cup> things = new LinkedList<Cup>();

        // Make an impassable rectangle
        RigidBody shape = new RigidBody(0, 25, 20, 15);

        env.createNewImpassableTerrain(shape);

        // Put a cup on the far side of the rectangle
        things.add(new Cup(10, 60, false));

        // Make a robot to hold the sensor and put it on the close side of the rectangle
        SensorTestingRobot robot = new SensorTestingRobot(0, new XPoint(10, 10), 0);

        // Set the parameters of the sensor
        double offsetAngle = 0;
        double max         = 500;

        // Make the sensor
        CupSensor instance = new CupSensor(offsetAngle, max);

        instance.setObject(robot);

        // Look for cup
        instance.analyse(env, robots, things);

        // The cup should not be seen
        assertEquals(false, instance.getOutput());
    }
}



