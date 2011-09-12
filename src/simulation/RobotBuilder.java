package simulation;

import simulation.geometry.XPoint;

import simulation.entities.Robot;

import java.awt.Color;

import java.io.File;
import java.io.IOException;

import javax.script.ScriptException;

/**
 * RobotBuilder.java
 *
 * This is a testing class. Allows for methodology that will be supplied
 * by the gui.
 * @author Simon
 */
public class RobotBuilder {

    /**
     * Prevent instantiation
     */
    private RobotBuilder() {}

    /**
     * Method description
     *
     *
     * @return
     *
     * @throws ScriptException
     */
    public static Robot interpretedRobotBuilder1() throws ScriptException {
        String f = "scripts" + File.separatorChar
                   + "brain1-sensors.rb";

        try {
            return new Robot(f, "Jimmy", new XPoint(750, 50), Math.PI);
        } catch (IOException e) {
            throw new ScriptException(e);
        }
    }

    /**
     * Method description
     *
     *
     * @return
     *
     * @throws ScriptException
     */
    public static Robot interpretedRobotBuilder2() throws ScriptException {
        String f = "scripts" + File.separatorChar
                   + "brain2-behaviours.rb";

        try {
            return new Robot(f, "Sammy", new XPoint(900, 100), -(2 * Math.PI) / 4);
        } catch (IOException e) {
            throw new ScriptException(e);
        }
    }

    /**
     * Method description
     *
     *
     * @return
     *
     * @throws ScriptException
     */
    public static Robot interpretedRobotBuilder3() throws ScriptException {
        String f = "scripts" + File.separatorChar
                   + "brain3-plain.rb";

        try {
            return new Robot(f, "Simple", new XPoint(500, 500), Math.PI / 2);
        } catch (IOException e) {
            throw new ScriptException(e);
        }
    }

    /**
     * Method description
     *
     *
     * @return
     *
     * @throws ScriptException
     */
    public static Robot interpretedRobotBuilder4() throws ScriptException {
        String f = "scripts" + File.separatorChar
                   + "brain4-collector.rb";

        try {
            return new Robot(f, "Collector", new XPoint(100, 100), 0);
        } catch (IOException e) {
            throw new ScriptException(e);
        }
    }

    public static Robot interpretedRobotBuilder5() throws ScriptException {
        String f = "scripts" + File.separatorChar
                   + "brain5-wallFollower.rb";

        try {
            return new Robot(f, "Follower", new XPoint(100, 100), 0);
        } catch (IOException e) {
            throw new ScriptException(e);
        }
    }

    public static Robot interpretedRobotBuilder6() throws ScriptException {
        String f = "scripts" + File.separatorChar
                   + "brain6-Avoider.rb";

        try {
            return new Robot(f, "Scardy", new XPoint(500, 500), 0);
        } catch (IOException e) {
            throw new ScriptException(e);
        }
    }

    public static Robot interpretedRobotBuilder7() throws ScriptException {
        String f = "scripts" + File.separatorChar
                   + "brain7-robotChaser.rb";

        try {
            return new Robot(f, "Meany", new XPoint(100, 100), 0);
        } catch (IOException e) {
            throw new ScriptException(e);
        }
    }

    public static Robot interpretedRobotBuilder7V2() throws ScriptException {
        String f = "scripts" + File.separatorChar
                   + "brain7-robotChaserV2.rb";

        try {
            return new Robot(f, "MeanyV2", new XPoint(100, 100), 0);
        } catch (IOException e) {
            throw new ScriptException(e);
        }
    }

    public static Robot interpretedRobotBuilder8() throws ScriptException {
        String f = "scripts" + File.separatorChar
                   + "brain8-blockCollector.rb";

        try {
            return new Robot(f, "Block Collecter", new XPoint(700, 700), 0);
        } catch (IOException e) {
            throw new ScriptException(e);
        }
    }

    public static Robot interpretedRobotBuilder9() throws ScriptException {
        String f = "scripts" + File.separatorChar
                   + "brain9-bumperRobot.rb";

        try {
            return new Robot(f, "Bump", new XPoint(500, 500), Math.PI / 2);
        } catch (IOException e) {
            throw new ScriptException(e);
        }
    }

    public static Robot demo1() throws ScriptException {
        String f = "scripts" + File.separatorChar
                   + "brain0_demo_main.rb";

        try {
            return new Robot(f, "DemonstrationBot", new XPoint(50, 390), 0);
        } catch (IOException e) {
            throw new ScriptException(e);
        }
    }

    public static Robot demo2() throws ScriptException {
        String f = "scripts" + File.separatorChar
                   + "brain0_demo_deadend.rb";

        try {
            return new Robot(f, "I like to get stuck", new XPoint(690, 200), 0, 10, 20, Color.blue);
        } catch (IOException e) {
            throw new ScriptException(e);
        }
    }

    public static Robot demo3() throws ScriptException {
        String f = "scripts" + File.separatorChar
                   + "brain0_demo_racer.rb";

        try {
            return new Robot(f, "Racer", new XPoint(950, 500), -Math.PI);
        } catch (IOException e) {
            throw new ScriptException(e);
        }
    }
}



