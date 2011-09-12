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
 * Test the TerrainSensor class
 * @version 1.0
 * @version 1.1 05.06.2011
 * @author Joshua Jordan
 */
public class TerrainSensorTest {

    /**
     * Test of analyse method, of class TerrainSensor.
     */
    @Test
    public void testGetOutput() {
        System.out.println("getOutput");

        TerrainSensor instance = new TerrainSensor();
        Environment   env      = new Environment(new RigidBody(0, 0, 500, 500));

        env.createNewPassableTerrain(new RigidBody(0, 0, 500, 500), 0.5);

        SensorTestingRobot robot = new SensorTestingRobot(0, new XPoint(10, 10), 0);

        instance.setObject(robot);
        instance.analyse(env, new LinkedList<Robot>(), new LinkedList<Cup>());
        assertEquals(0.6, instance.getOutput(), 0.0);
    }
}



