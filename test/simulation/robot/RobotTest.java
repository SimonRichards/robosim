package simulation.robot;

import simulation.entities.Robot;
import simulation.RobotBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.*;

import java.awt.geom.Area;

import java.util.Arrays;
import java.util.Collection;

import javax.script.ScriptException;

/**
 * Use this test file as a template when writing tests for more than one
 * implementation of the same interface.
 * @author Simon
 */
@RunWith(Parameterized.class)
public class RobotTest {
    final private Robot robot;

    /**
     * Constructs ...
     *
     *
     * @param robot
     */
    public RobotTest(Robot robot) {
        this.robot = robot;
    }

    /**
     * Method description
     *
     *
     * @return
     *
     * @throws ScriptException
     */
    @Parameters
    public static Collection<Object[]> getParameters() throws ScriptException {
        Robot robotA = RobotBuilder.interpretedRobotBuilder1();

        return Arrays.asList(new Object[][] {
            { robotA },
        });
    }

    /**
     * Testing that the robot provides a non-empty shape
     */
    @Test
    public void testSetShape() {
        assertFalse(new Area(robot).isEmpty());
    }

    /**
     * Sanity check
     */
    @Test
    public void testGetPower() {
        assertTrue(robot.getPower() > 0.0);
    }

    /**
     * Tests the geometric correctness of turn.
     */
    @Test
    public void testTurn() {
        double turnAngle = 0.5;
        double oldAngle  = robot.getAngle();

        robot.rotate(turnAngle);
        robot.rotate(-turnAngle);
        assertEquals(robot.getAngle(), oldAngle, 0.1);
    }
}



