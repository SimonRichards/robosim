
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package simulation.sensors;

import java.util.Collection;
import simulation.geometry.Entity;
import simulation.geometry.XPoint;
import simulation.entities.Cup;

/**
 * Class used for testing sensors. A concrete class that implements SensorAble
 * @author Jermin
 * @version 1.0 05.06.2011
 *
 * @since 1.0
 */
public class SensorTestingRobot implements SensorAble {
    private double angle;
    private XPoint location;
    private double velocity;

    /**
     * Creates a new SensorTestingRobot with blank parameters
     */
    public SensorTestingRobot() {
        this.angle    = 0;
        this.location = new XPoint(0, 0);
        this.velocity = 0;
    }

    /**
     * Creates a new SensorTestingRobot
     * @param angle angle the robot is facing
     * @param location Point containing the location of the robot
     * @param velocity current velocity of the robot
     */
    public SensorTestingRobot(double angle, XPoint location, double velocity) {
        this.angle    = angle;
        this.location = location;
        this.velocity = velocity;
    }

    /**
     * Sets the angle of the robot
     *
     * @param a angle to assign to the robot
     */
    public void setAngle(double a) {
        this.angle = a;
    }

    /**
     * Sets the location of the robot
     *
     * @param x the x position of the robot
     * @param y the y position of the robot
     */
    public void setLocation(int x, int y) {
        this.location = new XPoint(x, y);
    }

    /**
     * Sets the velocity of the robot
     *
     * @param v velocity to assign to the robot
     */
    public void setVelocity(double v) {
        this.velocity = v;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    @Override
    public double getAngle() {
        return angle;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    @Override
    public XPoint getCom() {
        return location;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    @Override
    public double getVelocity() {
        return velocity;
    }


    @Override
    public Collection<? extends Entity> getHeldItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}



