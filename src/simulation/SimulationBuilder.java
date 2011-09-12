package simulation;

import simulation.geometry.Environment;
import simulation.geometry.Entity;
import simulation.geometry.RigidBody;
import simulation.geometry.XPoint;

import simulation.entities.Cup;

import java.awt.Polygon;
import java.awt.geom.Path2D;

import javax.script.ScriptException;

/**
 * This class sets up the default configuration of the simulator. This class is 
 * not used in this program and is used for demonstration purposes only.
 * 
 * @author Simon
 */
public class SimulationBuilder {
    private SimulationBuilder() {}

    public static void setUp(Simulator simulator) {

        // demoSetUp(simulator);
        demoSetUp(simulator);
    }

    public static void setUp1(Simulator sim) {
        sim.clearAll();

        try {

            /*
             * inanimates.add(new Inanimate(500, 300, 10, true));
             * inanimates.add(new Inanimate(500, 400, 10, true));
             * inanimates.add(new Inanimate(500, 600, 10, true));
             * inanimates.add(new Inanimate(500, 700, 10, true));
             * inanimates.add(new Inanimate(500, 800, 10, true));
             */
            sim.addInanimate(new Cup(500, 200, true));
            sim.addInanimate(new Cup(500, 220, true));
            sim.addInanimate(new Cup(500, 230, true));
            sim.addInanimate(new Cup(500, 240, true));
            sim.addInanimate(new Cup(500, 250, true));
            sim.addInanimate(new Cup(500, 260, true));
            sim.addInanimate(new Cup(500, 270, true));
            sim.addInanimate(new Cup(500, 280, true));
            sim.addInanimate(new Cup(100, 300, true));
            sim.addInanimate(new Cup(200, 400, true));
            sim.addInanimate(new Cup(300, 500, true));
            sim.addInanimate(new Cup(500, 210, true));

            // inanimates.add(new Inanimate(500, 250, 10, true));
            // inanimates.add(new Inanimate(500, 260, 10, true));
//          inanimates.add(new Inanimate(500, 225, 10, true));
//          inanimates.add(new Inanimate(500, 230, 10, true));

            /*
             * int[] xpoints             = { 120, 200, 300, 300, 100 };
             * int[] ypoints             = { 80, 100, 100, 50, 0 };
             * int[] envXPoints          = {0, 800, 800, 150, 0};
             * int[] envYPoints          = {0, 0, 800, 800, 800};
             * Polygon  environmentShape = new Polygon(envXPoints, envYPoints, envXPoints.length);
             * Polygon  impassableShape  = new Polygon(xpoints, ypoints, xpoints.length);
             *
             * environment = new Environment(new RigidBody(environmentShape, new XPoint(400, 400)));
             * environment.createNewImpassableTerrain(new Entity(impassableShape));
             * environment.createNewPassableTerrain(new RigidBody(250,250,10,10), 0.4);
             */

//          /*
//           * int[] xpoints             = { 120, 200, 300, 300, 100 };
//           * int[] ypoints             = { 80, 100, 100, 50, 0 };
//           * int[] envXPoints          = {0, 800, 800, 150, 0};
//           * int[] envYPoints          = {0, 0, 800, 800, 800};
//           * Polygon  environmentShape = new Polygon(envXPoints, envYPoints, envXPoints.length);
//           * Polygon  impassableShape  = new Polygon(xpoints, ypoints, xpoints.length);
//           *
//           * environment = new Environment(new RigidBody(environmentShape, new XPoint(400, 400)));
//           * environment.createNewImpassableTerrain(new Entity(impassableShape));
//           * environment.createNewPassableTerrain(new RigidBody(250,250,10,10), 0.4);
//           */

            /*
             * Robot r = RobotBuilder.interpretedRobotBuilder1();
             *
             * r.place(new XPoint(200, 200));
             *
             * Robot r2 = RobotBuilder.interpretedRobotBuilder1();
             *
             * r2.place(new XPoint(300, 200));
             * robots.add(r);
             * robots.add(r2);
             * robots.add(RobotBuilder.interpretedRobotBuilder1());
             * robots.add(RobotBuilder.interpretedRobotBuilder2());
             */

            // robots.add(RobotBuilder.interpretedRobotBuilder1());
            // robots.add(RobotBuilder.interpretedRobotBuilder3());
            // robots.add(RobotBuilder.interpretedRobotBuilder1());
            sim.addRobot(RobotBuilder.interpretedRobotBuilder5());

            // robots.add(RobotBuilder.interpretedRobotBuilder6());
            sim.addRobot(RobotBuilder.interpretedRobotBuilder7V2());
            sim.addRobot(RobotBuilder.interpretedRobotBuilder6());

            // sim.addRobot(RobotBuilder.interpretedRobotBuilder8());
        } catch (ScriptException e) {}
    }

    public static void demoSetUp(Simulator sim) {
        Environment env = sim.getEnvironment();

        // Environment outline
        int[]   xpoints = {
            1, 1, 150, 650, 500, 700, 700, 1000, 1000, 950, 850, 750, 700
        };
        int[]   ypoints = {
            0, 500, 650, 650, 850, 850, 550, 550, 250, 150, 100, 50, 0
        };
        Polygon outline = new Polygon(xpoints, ypoints, xpoints.length);

        env.setShape(new RigidBody(outline, new XPoint()));

        // Add new terrains
        int[] insertXpoints = {
            1, 1, 100, 100, 200, 350, 350, 700, 700, 740, 740, 720, 100, 100
        };
        int[] insertYpoints = {
            300, 350, 350, 450, 550, 550, 150, 150, 500, 500, 120, 100, 100, 300
        };

        outline = new Polygon(insertXpoints, insertYpoints, insertXpoints.length);
        env.createNewImpassableTerrain(new Entity(outline));

        int[] insertXpoints2 = { 640, 640, 655, 655 };
        int[] insertYpoints2 = { 200, 590, 590, 200 };

        outline = new Polygon(insertXpoints2, insertYpoints2, insertXpoints2.length);
        env.createNewImpassableTerrain(new Entity(outline));

        int[] insertXpoints3 = {
            500, 530, 560, 590, 620, 610, 580, 550, 520, 490
        };
        int[] insertYpoints3 = {
            450, 480, 450, 480, 450, 420, 350, 380, 380, 380
        };

        outline = new Polygon(insertXpoints3, insertYpoints3, insertXpoints3.length);
        env.createNewImpassableTerrain(new Entity(outline));

        Path2D roughPatch = new Path2D.Double();

        roughPatch.moveTo(880, 400);
        roughPatch.curveTo(880, 450, 900, 480, 980, 440);
        roughPatch.curveTo(950, 440, 990, 350, 970, 320);
        roughPatch.curveTo(950, 300, 920, 280, 880, 320);
        env.createNewPassableTerrain(new Entity(roughPatch), 0.9);

        // Add inanimates
        sim.addInanimate(new Cup(50, 450, false));
        sim.addInanimate(new Cup(90, 490, false));
        sim.addInanimate(new Cup(130, 550, false));
        sim.addInanimate(new Cup(240, 630, false));

        // Add robots
        try {
            sim.addRobot(RobotBuilder.demo1());
            sim.addRobot(RobotBuilder.demo2());
            sim.addRobot(RobotBuilder.demo3());
        } catch (ScriptException e) {
            System.err.println(e);
        }
    }

    public static void joshSetUp(Simulator sim) {}
}



