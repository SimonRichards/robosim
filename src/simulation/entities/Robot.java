package simulation.entities;

import java.awt.Color;
import org.jruby.embed.LocalContextScope;
import org.jruby.embed.ScriptingContainer;
import org.jruby.exceptions.RaiseException;
import simulation.geometry.Entity;
import simulation.geometry.RigidBody;
import simulation.geometry.XPoint;
import simulation.sensors.Sensor;
import simulation.sensors.SensorAble;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import javax.script.ScriptException;

/**
 * Robot class, has a shape and sensors associated with it.
 *
 * @author Simon
 */
public class Robot extends RigidBody implements SensorAble {
    public static final int DEFAULT_LENGTH = 60;
    public static final int DEFAULT_MASS = 100;
    public static final int DEFAULT_POWER = 200;
    public static final int DEFAULT_WIDTH = 30;
    
    private static final String         NULL_REFERENCE_FOUND = "Null reference found";
    public static final String          UNKNOWN_ISSUE_CAUGHT = "Unknown Issue caught.";
    private final XPoint                collectionOffset   = new XPoint(0, -60);
    private double                      collectionProgress   = 0;
    private RigidBody                   cupCollectionArea    = new RigidBody(0, 0, 50);
    final private Collection<Cup>       heldItems            = new LinkedList<Cup>();
    private String                      filePath;
    final double                        power;
    private final double                mass;
    private transient RubyRobot         brain;
    private ScriptingContainer          container;
    private String                      name;
    private Object                      receiver;
    private transient Collection<Sensor>sensors;
    private double                      velocity;
    private double                      width;
    private double                      length;
    private transient StringWriter      writer;
    private Color                       colour;

    /**
     * Creates a new robot object which runs on ruby script
     * 
     * @param robot                 A robot object
     * @throws IOException          When the file does not exist or the application does not have read access
     * @throws ScriptException      When there are errors in the Ruby syntax or required methods are not implemented
     */
    public Robot(Robot robot) throws IOException, ScriptException{
        this(robot.filePath, robot.name, new XPoint(robot.getCom()), robot.getAngle(), robot.width, robot.length, robot.colour, robot.power, robot.mass);
        for (Cup inanimate : robot.heldItems) { // TODO: removeme? probably not necessary
            heldItems.add(new Cup(inanimate));
        }
    }
    
    /**
     * Creates a new Robot object which runs on a Ruby script
     *
     * @param filePath                  Path to the ruby script to use
     * @param name                      A name for the robot
     * @param location                  Location of the robot
     * @param angle                     The initial angle
     * @throws IOException              When the file does not exist or the application does not have read access
     * @throws ScriptException          When there are errors in the Ruby syntax or required methods are not implemented
     */
    @SuppressWarnings("unchecked")
    public Robot(String filePath, String name, XPoint location, final double angle)
            throws ScriptException, IOException {
        this(filePath, name, location, angle, DEFAULT_WIDTH, DEFAULT_LENGTH, Color.GREEN, DEFAULT_POWER, DEFAULT_MASS);
    }
    
    
    /**
     * Creates a new Robot object which runs on a Ruby script.
     *
     * @param filePath                  Path to the ruby script to use.
     * @param name                      A name for the robot
     * @param location                  Location of the robot
     * @param angle                     The initial angle
     * @param width 
     * @param length 
     * @param colour 
     * @throws IOException              When the file does not exist or the application does not have read access
     * @throws ScriptException          When there are errors in the Ruby syntax or required methods are not implemented
     */
    public Robot(
            String filePath, 
            String name, 
            XPoint location, 
            final double angle, 
            final double width, 
            final double length, 
            Color colour)
            throws IOException, ScriptException {
        this(filePath, name, location, angle, width, length, colour, DEFAULT_POWER, DEFAULT_MASS);
    }
        
    
    /**
     * Creates a new Robot object which runs on a Ruby script.
     *
     * @param filePath                  Path to the ruby script to use.
     * @param name                      A name for the robot
     * @param location                  Location of the robot
     * @param angle                     The initial angle
     * @param width 
     * @param length 
     * @param colour 
     * @param power 
     * @param mass 
     * @throws IOException              When the file does not exist or the application does not have read access
     * @throws ScriptException          When there are errors in the Ruby syntax or required methods are not implemented
     */
    public Robot(
            String filePath, 
            String name, 
            XPoint location, 
            final double angle, 
            final double width, 
            final double length, 
            Color colour, 
            final double power, 
            final double mass)
            throws IOException, ScriptException {
        
        super(new Entity(location.getX() - width/2, location.getY() - length/2, width, length), location);
        
        this.length = length;
        this.width = width;
        this.colour = colour;
        this.power = power;
        this.mass = mass;
        this.filePath = filePath;
        writer = new StringWriter();
        container = new ScriptingContainer(LocalContextScope.THREADSAFE);
        container.setOutput(writer);

        String         mixinPath   = "scripts" + File.separator + "SimRobot.rb";
        BufferedReader mixinReader = new BufferedReader(new FileReader(mixinPath));
        BufferedReader brainReader = new BufferedReader(new FileReader(filePath));

        
        // Retrieve the name of the user's class
        // The first line should look something like: class MyRobot < SimRobot
        String className;

        // Retrieve the class name
        do {
            brainReader.mark(50);
            className = brainReader.readLine();
            if (className == null) {
                throw new ScriptException("No class data found");
            }
        } while ((className.trim().length() == 0) || className.trim().startsWith("#"));

        try {
            brainReader.reset();
        } catch (IOException e) {
            throw new ScriptException("Class name too long");
        }

        try {
            className = className.substring("class".length(), className.indexOf('<')).trim();
        } catch (StringIndexOutOfBoundsException e) {
            throw new ScriptException("Please extend the SimRobot class");
        }

        // Parse the scripts
        container.runScriptlet(mixinReader, mixinPath);
        container.runScriptlet(brainReader, filePath);
        receiver  = container.runScriptlet(className + ".new");
        updateBrain();
        this.name = name;
        rotate(angle);

        // Place the cup collection area in front of the robot
        XPoint collectionLocation = adjustOffsetLocation(this.getCom(), collectionOffset, -this.getAngle());

        cupCollectionArea.place(collectionLocation);
    }
    
    /**
     * Abstracted for dryness
     */
    private void updateBrain() {
        brain     = container.getInstance(receiver, RubyRobot.class);
        sensors   = brain.getSensors();        
    }

    
    /**
     * @return the Robot's absolute velocity
     */
    @Override
    public double getVelocity() {
        return velocity;
    }

    /**
     * @param velocity The new velocity
     */
    public void setVelocity(final double velocity) {
        this.velocity = velocity;
    }

    /**
     * @return All items that the robot has collected
     */
    @Override
    public Collection<? extends Entity> getHeldItems() {
        return Collections.unmodifiableCollection(heldItems);
    }

    /**
     * Adds an inanimate into the robot's collection bucket
     * @param thing The inanimate to add
     */
    public void addHeldItem(Cup thing) {
        heldItems.add(thing);
    }

    /**
     * @return The engine size, where 200.0 is typical.
     */
    public double getPower() {
        return power;
    }
     
    /**
     * @return The Robots mass
     */
    public double getMass() {
        return mass;
    }

    /**
     * @return The RigidBocy representing the robot's cup collection area
     */    
    public Entity getCollectionArea() {
        return cupCollectionArea;
    }

    /**
     * @param newShape Sets the robots body to a new shape
     */
    @Override
    public void setShape(RigidBody newShape) {
        super.setShape(newShape);

        XPoint collectionLocation = adjustOffsetLocation(newShape.getCom(), collectionOffset, -newShape.getAngle());

        cupCollectionArea.place(collectionLocation);
    }

    /**
     * Place the cupCollectionArea at a location
     * @param x x-coordinate of robot
     * @param y y-coordinate of robot
     */
    @Override
    public void place(double x, double y) {
        super.place(x, y);
        XPoint collectionLocation = adjustOffsetLocation(new XPoint(x,y), collectionOffset, -getAngle());
        
        cupCollectionArea.place(collectionLocation);
    }

    /**
     * @return The percentage complete that arm actuation is at
     */    
    public double getCollectionProgress() {
        return collectionProgress;
    }

    /**
     * @param collectionProgress The percentage complete that arm actuation is at
     */
    public void setCollectionProgress(double collectionProgress) {
        this.collectionProgress = collectionProgress;
    }

    /**
     * @return A collection of the robot's sensors
     */    
    public Collection<Sensor> getSensors() {
        return Collections.unmodifiableCollection(sensors);
    }

    /**
     * Calls the script's update method and updates the String buffer.
     * @return The robot's output
     * @throws ScriptException If there is a syntactic error in the script
     */
    @SuppressWarnings("unchecked")
    public RobotOutput update() throws ScriptException {
        try {
            brain.update();
        } catch (NullPointerException e) {
            throw new ScriptException(NULL_REFERENCE_FOUND);
        } catch (RaiseException e) {
            throw new ScriptException(e.getMessage());
        } catch (Exception e) {
            throw new ScriptException(UNKNOWN_ISSUE_CAUGHT + ": " + e.getMessage());
        }

        return brain.getOutput();
    }
    //<editor-fold defaultstate="collapsed" desc="Boring getters">
    /**
     * @inheritDoc
     *
     * @return
     */
    
    @Override
    public String getDescription() {
        return name;
    }
    
    /**
     * Method description
     *
     *
     * @return
     */
    
    public StringWriter getWriter() {
        return writer;
    }
    
    /**
     * Method description
     *
     *
     * @return
     */
    
    public double getWidth() {
        return width;
    }
    
    /**
     * Method description
     *
     *
     * @param width
     */
    public void setWidth(double width) {
        this.width = width;
    }
    
    /**
     * Method description
     *
     *
     * @return
     */
    
    public double getLength() {
        return length;
    }
    
    /**
     * Method description
     *
     *
     * @param height
     */
    public void setHeight(double height) {
        this.length = height;
    }
    
    /**
     *
     * @return The colour of the robot.
     */
    public Color getColour() {
        return colour;
    }
    //</editor-fold>
    /**
     * Calculates the angle at which a collision will change between glancing and
     * non-glancing
     * @return The critical angle for glancing vs. non-glancing collisions
     */
    public double getGlancingThreshold() {
        return Math.atan2(width, length);
    }
    

    /**
     * Place the robot at a particular location
     * 
     * @param newLocation the new location to place the robot at
     */
    @Override
    public void place(XPoint newLocation) {
        place(newLocation.getX(), newLocation.getY());
    }
}



