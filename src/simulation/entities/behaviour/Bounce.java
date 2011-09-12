
/**
 * Bounce.java
 *
 * @author Jermin Tiu
 * @version 18.07.2011
 */
package simulation.entities.behaviour;

import simulation.entities.RobotOutput;

import simulation.sensors.*;

/**
 * Bounce.java
 * Bounces between forward and back. Steering optional
 *
 * @author Jermin
 * @WalkedThrough
 * @DeskChecked
 */
public class Bounce implements Behaviour {
    private double               direction  = 1;    // Direction. Positive for forward
    private boolean              isFinished = false;
    private RobotOutput          output     = new RobotOutput();
    private final DistanceSensor backSense;
    private final DistanceSensor frontSense;
    private double               motorEffort;
    private final double         reactDist;
    private double               steeringEffort;

    /**
     * Creates a new bounce behaviour. Bounces between forward and back.
     * @param front front facing distance sensor
     * @param back rear facing distance sensor
     */
    public Bounce(DistanceSensor front, DistanceSensor back) {
        this(front, back, 200);
    }

    /**
     * Creates a new bounce behaviour. Bounces between forward and back.
     * @param front front facing distance sensor
     * @param back rear facing distance sensor
     * @param dist the value at which to change direction
     */
    public Bounce(DistanceSensor front, DistanceSensor back, double dist) {
        this(front, back, dist, 50, 0);
    }

    /**
     * Creates a new bounce behaviour. Bounces between forward and back.
     * @param front front facing distance sensor
     * @param back rear facing distance sensor
     * @param dist the value at which to change direction
     * @param motorEffort optional motor effort value
     * @param steeringEffort optional steering effort value
     */
    public Bounce(DistanceSensor front, DistanceSensor back, double dist, double motorEffort, double steeringEffort) {
        this.frontSense     = front;
        this.backSense      = back;
        this.reactDist      = dist;
        this.motorEffort    = motorEffort;
        this.steeringEffort = steeringEffort;
    }

    /**
     * @inheritdoc
     *
     * @return
     */
    @Override
    public RobotOutput update() {
        if (frontSense.getOutput() < reactDist) {
            direction = -1;
        }

        if (backSense.getOutput() < reactDist) {
            direction = 1;
        }

        output.setMotor(motorEffort * direction);
        output.setSteering(steeringEffort * direction);

        return output;
    }

    /**
     * @inheritdoc
     */
    @Override
    public boolean isFinished() {
        return isFinished;
    }

    /**
     * @inheritdoc
     */
    @Override
    public void reset() {
        isFinished = false;
    }
}



