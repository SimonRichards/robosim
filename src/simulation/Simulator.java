package simulation;


import java.io.IOException;
import java.io.ObjectInputStream;
import publishersubscriber.SimulatorPublisher;
import publishersubscriber.SimulatorSubscriber;
import simulation.geometry.Environment;
import simulation.geometry.RigidBody;
import simulation.geometry.XPoint;
import simulation.entities.Cup;
import simulation.entities.Robot;
import simulation.entities.RobotOutput;
import simulation.sensors.Sensor;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.script.ScriptException;
import simulation.geometry.Collision;
import simulation.geometry.Entity;
import ux.usercontrol.DeflatedSimulator;


/**
 * Top level code for the simulation. Contains the game loops and all set up
 * information for objects, their locations and so on.
 *
 *
 * @author Simon, Andrew L, Michael
 */
public class Simulator implements SimulatorPublisher, Serializable {
       
    // Tweakable values
    public static final double                    FPS                   = 50;
    public static final double                    DT                    = 1 / FPS;
    private static final int                      MOTOR_NOISE_STDEV     = 1;
    private static final double                   SPILL_SIZE            = 150;
    private static final double                   DEADZONE              = RobotOutput.MAX_MOTOR / 20;
    private static final double                   ROTATE_INCREMENT      = Math.PI/180;
    
    // Simulation state containers and values
    private long                                  steps                 = 0;    // NB: this value will wrap around after 6 million millennia
    private Environment                           environment;
    private List<Cup>                             inanimates;
    private transient ScriptException             issue;
    private transient RobotOutput                 output;
    private Collection<Robot>                     robots;
    
    // Utilities
    private transient Random                      rand;
    private transient Scheduler                   scheduler;
    private transient Collection<SimulatorSubscriber> subscribers  = new CopyOnWriteArrayList<SimulatorSubscriber>();
    private static final long serialVersionUID = 1L; 

    /**
     * A new Simulator object sets up the field in a fresh state.
     */
    public Simulator() {
        rand        = new Random();
        scheduler   = chooseScheduler();

        inanimates  = new CopyOnWriteArrayList<Cup>();
        robots      = new CopyOnWriteArrayList<Robot>();
        environment = new Environment();
    }


    /**
     * Removes all robots, terrains and inanimates
     */
    public void clearAll() {
        robots.clear();
        inanimates.clear();
        environment.clear();
    }

    /**
     * @param robot An new robot to place
     *
     * @return True if the location was legal and the robot added
     */
    public boolean addRobot(Robot robot) {
        if (testShape(robot)) {
            robots.add(robot);
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param inanimate An new inanimate to place
     *
     * @return True if the location was legal and the inanimate added
     */
    public boolean addInanimate(Cup inanimate) {
        if (testShape(inanimate)) {
            inanimates.add(inanimate);
            return true;
        } else {
            return false;
        }
    }
    /**
     * @return True if no terrains, robots and Inanimates exist in simulator
     */
    public boolean isEmpty(){
            
        if(!this.getEnvironment().hasNoTerrain()){
            return false;
        }
        else if(!this.getRobots().isEmpty()){
            return false;
        }
        else if(!this.getThings().isEmpty()){
            return false;

        }
        else{
            return true;
        }
    }

    /**
     * Tests to see if a shape does not shape intersect or obstruct
     * the environment, robots or inanimate objects.
     * @param shape
     * @return boolean
     */
    public boolean testShape(Entity shape) {
        if (environment.obstructs(shape)) {
            return false;
        }
        for (Entity robot : robots) {
            if (robot.intersects(shape)) {
                return false;
            }
        }
        for (Entity inanimate : inanimates) {
            if (inanimate != shape && inanimate.intersects(shape)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the simulation to its initial state.
     */
    public void reset() {
        clearAll();
        steps = 0;
        notifySubscribers();
        subscriberReset();
    }

    /**
     * The main game loop, designed to be called at a fixed rate.
     */
    public void gameLoop() {
        for (Robot robot : robots) {

            // Update all the robot's sensors
            for (Sensor s : robot.getSensors()) {
                s.setObject(robot);
                s.analyse(environment, robots, inanimates);
            }

            try {
                output = robot.update();
            } catch (ScriptException e) {
                scheduler.pause();
                issue = e;

                return;
            }

            RigidBody newShape = findNewState(robot, output);

            if (resolve(robot, newShape)) {
                robot.setShape(newShape);
            } else {
                robot.setVelocity(0);
            }
            
            // collecting a cup or moving a block
            if (output.isArmActive()) {
                for (Cup cup : inanimates) {
                    if (robot.getCollectionArea().intersects(cup)) {
                        double progress = robot.getCollectionProgress();

                        if (progress >= 100) {
                            robot.setCollectionProgress(0);
                            robot.addHeldItem(cup);

                            inanimates.remove(cup);
                        } else {
                            robot.setCollectionProgress(progress + 1);
                        }
                    } 
                }
            } else {
                robot.setCollectionProgress(0);
            }
        }
        steps++;
    }

    /**
     * Calculates the potential new position of a robot given its current state and motor outputs. No collision 
     * detection is done here.
     * @param robot The robot to be updated
     * @param output The robot's output
     * @return [0] The robot's transformed shape, [1] The shape used for collision checking.
     */
    private RigidBody findNewState(final Robot robot, RobotOutput output) {
        RigidBody newShape = new RigidBody(robot);
        final double motorOut = output.getMotor();
        final double steering = output.getSteering();

        // TODO: Do this properly

        newShape.rotate(steering * robot.getVelocity() / 10000);
        
        double acceleration = robot.getPower() * motorOut / robot.getMass();

        // Some non-linear motor deadzoning
        if ((Math.abs(motorOut) < 10) && (Math.abs(robot.getVelocity()) < DEADZONE) && (motorOut * robot.getVelocity() > 0)) {
            acceleration = 0;
        }

        double direction      = ((robot.getVelocity() > 0) ? 1 : -1);
        double windResistance = robot.getVelocity() * 0.5;
        double groundFriction = direction * robot.getMass() * environment.getCoefficient(robot);
        double friction       = windResistance + groundFriction;

        acceleration -= friction;

        // Add in some gaussian noise
        acceleration += rand.nextGaussian() * MOTOR_NOISE_STDEV;

        double velocity = robot.getVelocity() + acceleration * DT;

        robot.setVelocity(velocity);
        newShape.translate(-velocity * DT * Math.sin(robot.getAngle()), (velocity * DT * Math.cos(robot.getAngle())));

        return newShape;
    }

    /**
     * Collision detection and responses
     * @param robot The robot which needs its newShape reconciled with the environment and other entities
     * @param newShape The Rigidbody representing the robot's potential new state
     * @return false if the Robot is not in a legal position, otherwise returns true.
     */
    private boolean resolve(Robot robot, RigidBody newShape) {      
        for (Robot otherRobot : robots) {
            if ((otherRobot != robot) && newShape.intersects(otherRobot)) {
                return false;
            }
        }

        Collision enviroShapeCol = environment.obstructs(newShape);
        int rotateCounter = 0;

        //Robot colliding with the walls
        while (enviroShapeCol.occurred()) {

            // If the robot is not in a legal position after rotating 45deg then
            // it is stuck
            if (rotateCounter >= 80) {
                return false;
            }

            boolean wallCollision = robotIntoEnvironment(robot, newShape, enviroShapeCol.getAngle());

            enviroShapeCol = environment.obstructs(newShape);
            rotateCounter++;
        }


        //Robot colliding with cups
        for (Cup cup : inanimates) {
            double robotVelocity;
            int infiniteLoopCheck = 0;
            double robotCriticalAngle = Math.atan2(robot.getWidth(), robot.getLength());
            Collision shapeOnInanimateCol = newShape.collideWith(cup, robotCriticalAngle);

            // If the robot hits a cup or block
            if (shapeOnInanimateCol.occurred()) {
                while (shapeOnInanimateCol.occurred()) {

                    // Making sure the code doesn't get stick in an infinite loop
                    if(infiniteLoopCheck > 100) {
                        return false;
                    }
                    
                    if (!robotIntoCup(robot, cup, shapeOnInanimateCol.getAngle())) {
                        return false;
                    }

                    shapeOnInanimateCol = newShape.collideWith(cup, robotCriticalAngle);
                    infiniteLoopCheck++;
                }

                // If the cup that has just been moved is now hitting
                // another robot, destroy the cup
                for(Robot otherRobot : robots) {
                    robotVelocity = robot.getVelocity();
                
                    if(otherRobot != robot) {
                        double criticalAngle = Math.atan2(otherRobot.getWidth(), otherRobot.getLength());
                        Collision cupOnRobotCol = otherRobot.collideWith(cup, criticalAngle);
                        if(cupOnRobotCol.occurred()) {
                            CupIntoRobot(cup);
                            return false;
                        }
                    }
                }

                Collision enviroOnCupCol = environment.obstructs(cup);

                // If a cup hits the environment, crush it
                if(enviroOnCupCol.occurred()) {
                    cupIntoEnvironment(cup);
                    
                    return false;
                }
            }
            

            for (Cup otherCup : inanimates) {
                robotVelocity = robot.getVelocity();
                Collision cupOnCupColl = otherCup.collideWith(cup, 0);

                // If an inanimate is hitting another inanimate
                if((otherCup != cup) && cupOnCupColl.occurred()) {
                    while ((otherCup != cup) && cupOnCupColl.occurred()) {
                        
                        cupIntoCup(robot, otherCup, cupOnCupColl.getAngle());
                        cupOnCupColl = otherCup.collideWith(cup, 0);
                    }


                    Collision enviroOnOtherCupCol = environment.obstructs(otherCup);

                    // If a cup hits the environment, crush it
                    if(enviroOnOtherCupCol.occurred()) {
                        cupIntoEnvironment(otherCup);
                        
                        return false;
                    }


                    // If the cup that has just been moved is now hitting
                    // another robot, destroy the cup
                    for(Robot otherRobot : robots) {
                        if(otherRobot != robot) {
                            double criticalAngle = Math.atan2(otherRobot.getWidth(), otherRobot.getLength());
                            Collision cupOnRobotCol = otherRobot.collideWith(otherCup, criticalAngle);
                            if(cupOnRobotCol.occurred()) {
                                CupIntoRobot(otherCup);
                                return false;
                            }
                        }
                    }
                }
                
            }
           
        }
        return true;
    }          

    //<editor-fold defaultstate="collapsed" desc="Special cases of collision response">
    /**
     * Special case collision response for a robot into a terrain or environment outline
     * @param robot The robot being resolved
     * @param newShape its new state
     * @param angle The angle at which the collision occurred
     * @return false if the collision may not be resolved nicely
     */
    private boolean robotIntoEnvironment(Robot robot, RigidBody newShape, double angle) {
        double angleOfIncidence = angle;
        double direction;

        if((robot.getAngle() >= -Math.PI/2) && (robot.getAngle() <= Math.PI/2)) {
            direction = 1;
        } else {
            direction = -1;
        }

        double newVelocityFactor = Math.abs(Math.sin(angleOfIncidence));
        double glancingAngle;
        
        
        if(angleOfIncidence > 2*Math.PI) {
            angleOfIncidence %= 2*Math.PI;
        }
        if(Math.abs(angleOfIncidence - 2*Math.PI) < Math.abs(angleOfIncidence)) {
            angleOfIncidence -= 2*Math.PI;
        }
        
        if(Math.abs(angleOfIncidence) > Math.PI/2) {
            if(Math.abs(angleOfIncidence - Math.PI) < Math.abs(angleOfIncidence)) {
                glancingAngle = angleOfIncidence - Math.PI;
            } else {
                glancingAngle = angleOfIncidence + Math.PI;
            }
        } else {
            glancingAngle = angleOfIncidence;
        }
        
        // If the robot is hitting a wall head on or rear on, stop it
        if(Math.abs(glancingAngle) < ROTATE_INCREMENT) {
            return false;
        }
        
        // If the com of the robot is before or past the line normal to the wall
        // glance or deflect the robot accordingly
        
        // Not glancing wall collision
        if(Math.abs(glancingAngle) < robot.getGlancingThreshold()){
            double incidenceFactor;
            if(glancingAngle < 0) {
                incidenceFactor = -1;
            } else {
                incidenceFactor = 1;
            }
            
            double XRotPoint;
            double YRotPoint;
            
            // Different calculations depending on if the robot is driving forwards
            // or backwards
            if(direction == 1) {
                XRotPoint = robot.getCom().getX() + incidenceFactor*Math.cos(newShape.getAngle())*robot.getWidth()/2 - Math.sin(newShape.getAngle())*robot.getLength()/2;
                YRotPoint = robot.getCom().getY() + incidenceFactor*Math.sin(newShape.getAngle())*robot.getWidth()/2 + Math.cos(newShape.getAngle())*robot.getLength()/2;
            } else {
                XRotPoint = robot.getCom().getX() + incidenceFactor*Math.cos(newShape.getAngle() + Math.PI)*robot.getWidth()/2 - Math.sin(newShape.getAngle() + Math.PI)*robot.getLength()/2;
                YRotPoint = robot.getCom().getY() + incidenceFactor*Math.sin(newShape.getAngle() + Math.PI)*robot.getWidth()/2 + Math.cos(newShape.getAngle() + Math.PI)*robot.getLength()/2;
                System.out.println("Rel X:"+(XRotPoint-robot.getCom().getX()));
                System.out.println("Rel Y:"+(YRotPoint-robot.getCom().getY()));
            }
            
            newShape.rotateAboutPoint(incidenceFactor*ROTATE_INCREMENT, new XPoint(XRotPoint, YRotPoint));
            
            // Glancing wall collision
        } else {
            double rotateAngle = 2*Math.sin(angleOfIncidence)*ROTATE_INCREMENT;
            robot.setVelocity(robot.getVelocity() * newVelocityFactor);
            newShape.rotate(rotateAngle);
        }
        
        return true;
        
    }
    
    
    /**
     * Special case collision response for a robot into an inanimate object
     * @param robot The robot being resolved
     * @param cup The cup that the robot hit
     * @param angle The angle of incidence for this collision
     */
    private boolean robotIntoCup(Robot robot, Cup cup, double angle) {
        if (cup.knockOver()) { environment.spillHotCoffeeEverywhere(cup.getCom(), SPILL_SIZE); }

        double xTrans = -robot.getVelocity() * DT * Math.sin(angle);
        double yTrans = robot.getVelocity() * DT * Math.cos(angle);

        // Move the cup
        cup.translate(xTrans, yTrans);

        return true;
    }
    
    
    /**
     * Special case collision response for an inanimate into the environment
     * @param cup The cup which has collided into an element of the environment
     */
    private void cupIntoEnvironment(Cup cup) {
        if (cup.knockOver()) {
            environment.spillHotCoffeeEverywhere(cup.getCom(), SPILL_SIZE);
        }
        inanimates.remove(cup);
        environment.createNewPassableTerrain(new RigidBody(cup), 0.9);
    }
        
    /**
     * A cup being pushed into another cup
     * @param robot The robot doing the pushing
     * @param cup The cup not being pushed but being collided with
     * @param angle the angle of incidence of the cup collision
     */
    private void cupIntoCup(Robot robot, Cup cup, double angle) {
        double direction;

        if((robot.getAngle() >= -Math.PI/2) && (robot.getAngle() <= Math.PI/2)) {
            direction = 1;
        } else {
            direction = -1;
        }


        double xTrans = direction * robot.getVelocity() * DT * Math.sin(angle);
        double yTrans = direction * robot.getVelocity() * DT * Math.cos(angle);

        // Move the cup
        cup.translate(xTrans, yTrans);
    }
    
       
    /**
     * A cup being pushed into a robot
     * @param cup The cup being pushed
     */
    private void CupIntoRobot(Cup cup){
        if (cup.knockOver()) { 
            environment.spillHotCoffeeEverywhere(cup.getCom(), SPILL_SIZE);
        }
        inanimates.remove(cup);
        environment.createNewPassableTerrain(new RigidBody(cup), 0.9);
    }
    
    //</editor-fold>

    /**
     * Object inflation routine. Default behaviour plus reinitialization of threaded fields
     * @param in
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        scheduler = chooseScheduler();
        rand = new Random();
        subscribers  = new CopyOnWriteArrayList<SimulatorSubscriber>();
    }

    private Scheduler chooseScheduler() {
        String os = System.getProperty("os.name").toLowerCase();

        if((os.indexOf( "win" ) >= 0)){
            //System.out.println("This is Windows");
            return new WinScheduler(this);

        }else if(os.indexOf( "mac" ) >= 0){
            //System.out.println("This is Mac");
            return new NixScheduler(this);

        }else if(os.indexOf( "nix") >=0 || os.indexOf( "nux") >=0){
            //System.out.println("This is Unix or Linux");
            return new NixScheduler(this);

        }else{
            //System.out.println("Your OS is not support!!");
            return new NixScheduler(this);
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="Accessors and mutators for subscribers">
    /**
     * @return The length in seconds of the current simulation
     */
    @Override
    public double getTimeElapsed() {
        return DT * steps;
    }
    
    /**
     * If this is null all is well. If a
     * ScriptException object is returned then
     * that issue is cleared and the sim reset.
     * @return The current issue.
     */
    @Override
    public ScriptException getIssue() {
        ScriptException temp = issue;
        
        issue = null;
        
        return temp;
    }
    
    /**
     * @param subscriber The subscriber to be added to the list of subscribers
     */
    @Override
    public void addSubscriber(final SimulatorSubscriber subscriber) {
        subscribers.add(subscriber);
    }
    
    /**
     * @param subscriber The subscriber to me removed from the list of subscribers
     */
    @Override
    public void removeSubscriber(final SimulatorSubscriber subscriber) {
        subscribers.remove(subscriber);
    }
    
    /**
     * Notifies all subscribers that the simulation is ready to be painted
     */
    public void notifySubscribers() {
        for (SimulatorSubscriber subscriber : subscribers) {
            subscriber.update(this);
        }
    }
    
    /**
     * Requests that all subscribers reset their state
     */
    public void subscriberReset(){
        for (SimulatorSubscriber subscriber : subscribers) {
            subscriber.simulatorResetted(this);
        }
    }
    
    /**
     * @return All robots currently in play
     */
    @Override
    public Collection<? extends Robot> getRobots() {
        return Collections.unmodifiableCollection(robots);
    }
    
    /**
     * @return The singular environment object with it's boundary and terrain descriptions
     */
    @Override
    public Environment getEnvironment() {
        return environment;
    }
    
    /**
     * @return All the things!
     */
    @Override
    public Collection<Cup> getThings() {
        return Collections.unmodifiableCollection(inanimates);
    }
    
    /**
     * @return Access to the scheduler running this simulation
     */
    public Scheduler getScheduler() {
        return scheduler;
    }
    /**
     * Delete the selected Entity.
     *
     * @param deletionEntity The Entity to be deleted.
     *
     */
    public void deleteEntity(Entity deletionEntity) {
        
        for (Entity entity : inanimates) {
            if (deletionEntity == entity) {
                inanimates.remove(deletionEntity);
            }
        }
        for (Entity entity : robots) {
            if (deletionEntity == entity) {
                robots.remove(deletionEntity);
            }
        }
        for (Entity entity : environment.getTerrain()) {
            if (deletionEntity == entity) {
                environment.deleteTerrain(deletionEntity);
            }
        }
    }
    
    @Override
    public boolean isInSimulator(RigidBody object) {
        Collection<RigidBody> simulationObjects = new LinkedList<RigidBody>();
        simulationObjects.addAll(robots);
        simulationObjects.addAll(inanimates);
        simulationObjects.addAll(environment.getTerrain());
        boolean isInSimulation = false;
        for(RigidBody simulationObject : simulationObjects ) {
            if(object == simulationObject){
                isInSimulation = true;
                break;
            }
        }
        return isInSimulation;
    }
    /**
     * Inflate a DeflatedSimulator
     *
     * @param backupSimulator The Simulator to be inflated.
     */
    public void inflate(DeflatedSimulator backupSimulator) {
        clearAll();
        environment = backupSimulator.environment;
        robots.addAll(backupSimulator.robots);
        inanimates.addAll(backupSimulator.inanimates);
    }

}
