/*
 * Simulator test class
 * Tests collisions between cups, robots and the environment
 *
 * @author mjh190
 */

package simulation.robot;

import simulation.entities.Robot;
import simulation.entities.Cup;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import simulation.geometry.Environment;
import java.util.concurrent.CopyOnWriteArrayList;
import java.awt.Polygon;
import simulation.geometry.RigidBody;
import simulation.geometry.XPoint;
import java.io.File;
import java.util.List;
import simulation.Simulator;
import simulation.geometry.Entity;



public class SimulatorTest {
    private Collection<Robot>       robots;
    private Environment             environment;
    private List<Cup>         inanimates;
    

    public SimulatorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * This function is used to initialise and setup robots, cups, impassable objects
     * and the environment
     * @param xPoint the x point of the robot
     * @param yPoint the y point of the robot
     * @param angle  the angle of the robot
     * @param file   the brain file to use with the associated robot
     * @throws IOException
     */
    private void initilise(double xPoint, double yPoint, double angle, String file) throws IOException{
        //Add robot
        robots = new CopyOnWriteArrayList<Robot>();
        inanimates  = new CopyOnWriteArrayList<Cup>();
        robots.clear();
        inanimates.clear();
        try {
                robots.add(interpretedRobotBuilder(xPoint, yPoint, angle, file));
            }
        catch (ScriptException ex) {
            Logger.getLogger(SimulatorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Setup environment
        int[] envXPoints          = {0, 500, 500, 500, 0};
        int[] envYPoints          = {0, 0, 200, 500, 500};
        Polygon  environmentShape = new Polygon(envXPoints, envYPoints, envXPoints.length);
        environment = new Environment(new RigidBody(environmentShape, new XPoint(400, 400)));
        environment.createNewPassableTerrain(new RigidBody(250,250,10,10), 0.4);

        //Add cluster of cups        
        inanimates.add(new Cup(200, 200, true));
        inanimates.add(new Cup(200, 230, true));
        inanimates.add(new Cup(200, 260, true));
        inanimates.add(new Cup(200, 290, true));
        inanimates.add(new Cup(200, 320, true));
        inanimates.add(new Cup(200, 350, true));

        //Add an impassable object
        int[] xpoints             = { 300, 300, 450 };
        int[] ypoints             = { 270, 300, 300 };
        Polygon  impassableShape  = new Polygon(xpoints, ypoints, xpoints.length);
        environment.createNewImpassableTerrain(new Entity(impassableShape));
    }

    /**
     *
     * @param xPoint the x point of the robot
     * @param yPoint the y point of the robot
     * @param angle  the angle of the robot
     * @param file   the brain file to use with the associated robot
     * @return returns a Robot object
     * @throws ScriptException
     * @throws IOException
     */
    public static Robot interpretedRobotBuilder(double xPoint, double yPoint, double angle, String file) throws ScriptException, IOException {
        String f = "test" + File.separatorChar + "simulation" + File.separatorChar + "robot" + File.separatorChar + file;

        try {
            return new Robot(f, "Simple", new XPoint(xPoint, yPoint), angle);
        } catch (IOException e) {
            throw new ScriptException(e);
        }
    }
    

    /**
     * Test Robot against wall collision, of class Simulator.
     */
    @Test
    public void testRobotIntoWall() throws IOException {
        System.out.println("RobotIntoWall");
        Simulator instance;
        double epsilon = 0.01;

        //Robot moving forwards
        initilise(400,400,0,"brain3-plain_fwd.rb");
        instance = new Simulator();
        instance.gameLoop();        
        for (Robot robot : robots) {
            assertEquals(0, robot.getVelocity(),epsilon);
            assertEquals(0,robot.getAngle(),epsilon);
        }

        //Robot moving in Reverse
        initilise(400,400,Math.PI,"brain3-plain_Reverse.rb");
        instance = new Simulator();
        instance.gameLoop();
        for (Robot robot : robots) {
            assertEquals(0, robot.getVelocity(),epsilon);
            assertEquals(Math.PI,robot.getAngle(),epsilon);
        }
    }


    /**
     * Test Robot into a line of cups then into the wall, of class Simulator.
     */
    @Test
    public void testRobotIntoCups() throws IOException, ScriptException{
        System.out.println("RobotIntoCups");
        Simulator instance;
        double epsilon = 0.01;

        //Robot moving forwards
        initilise(200,50,0,"brain3-plain_fwd.rb");
        instance = new Simulator();
        instance.gameLoop();
        for (Robot robot : robots) {
            assertEquals(0, robot.getVelocity(),epsilon);
            assertEquals(0,robot.getAngle(),epsilon);
        }

        //Robot moving in Reverse
        initilise(200,50,Math.PI,"brain3-plain_Reverse.rb");
        instance = new Simulator();
        instance.gameLoop();
        for (Robot robot : robots) {
            assertEquals(0, robot.getVelocity(),epsilon);
            assertEquals(Math.PI,robot.getAngle(),epsilon);
        }
    }
   
    /**
     *  Test robot into an impassible object
     */
    @Test
    public void testRobotIntoInanimate() throws IOException, ScriptException{
        System.out.println("RobotIntoInanimate");
        Simulator instance;
        double epsilon = 0.01;

        //Set robot driving forward then test if it rotates then stops
        initilise(400, 50, 0, "brain3-plain_fwd.rb");
        instance = new Simulator();
        instance.gameLoop();
        for (Robot robot : robots) {
            assertEquals(0, robot.getVelocity(),epsilon);
            assertEquals(0,robot.getAngle(),epsilon);
        }
    }

    /**
     *  Test robot into a wall collision
     */
    @Test
    public void testRobotDeflection() throws IOException, ScriptException{
        System.out.println("RobotIntoRobot");
        Simulator instance;
        double epsilon = 0.01;

        //Set robot driving forward then test if it deflects off the wall and
        //comes to a stop
        initilise(100, 50, Math.PI/4, "brain3-plain_fwd.rb");
        instance = new Simulator();
        instance.gameLoop();
        for (Robot robot : robots) {
            assertEquals(0, robot.getVelocity(),epsilon);
            assertEquals(0.78,robot.getAngle(),epsilon);
        }
    }
}