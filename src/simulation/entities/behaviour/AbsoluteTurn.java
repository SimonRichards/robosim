package simulation.entities.behaviour;

import simulation.entities.RobotOutput;

import simulation.sensors.*;

/**
 * Behaviour for turning angle set angle. Can move forward or back, at any angle
 * @author trc29
 */
public class AbsoluteTurn implements Behaviour {
    private final static double DEFAULT_TURN       = 0.0;
    private final static double DEFAULT_VELOCITY   = 20;
    private final static double DEFAULT_PRECISION  = Math.PI / 64;
    private boolean             finished           = false;
    private RobotOutput         output             = new RobotOutput();
    private PID                 turnPID            = new PID(7.5, 0.05, 0.0);
    private boolean             manualVelocityFlag = false;
    private final CompassSensor compass;
    private double              desiredheading;
    private double              initialheading;
    private double              motorVelocity;
    private double              precision;
    private double              turnAngle;
    private double              turnDirection;
    private double              turnVelocity;

    /**
     * Turns robot to defined absolute angle.
     * @param compass the compass sensor to read from
     */
    public AbsoluteTurn(CompassSensor compass) {
        this(compass, DEFAULT_TURN);
    }

    /**
     * Turns robot to defined absolute angle.
     * @param compass the compass sensor to read from
     * @param angle the angle to turn to.
     */
    public AbsoluteTurn(CompassSensor compass, double angle) {
        this(compass, angle, DEFAULT_PRECISION);
    }

    /**
     * Turns robot to defined absolute angle.
     * @param compass the compass sensor to read from
     * @param angle the angle to turn to.
     * @param precision the precision with which to make the turn
     */
    public AbsoluteTurn(CompassSensor compass, double angle, double precision) {
        this(compass, angle, precision, DEFAULT_VELOCITY);
    }

    /**
     * Turns robot to defined absolute angle. This constructor NOT to be overwritten
     * @param compass the compass sensor to read from
     * @param angle the angle to turn to.
     * @param precision the precision with which to make the turn
     * @param motorVelocity the velocity for the motor
     */
    public AbsoluteTurn(CompassSensor compass, double angle, double precision, double motorVelocity) {
        this.compass = compass;
        this.setTurnAngle(angle);
        this.precision          = precision;
        this.manualVelocityFlag = false;
        this.motorVelocity      = motorVelocity;
    }

    /**
     * Turns robot to defined absolute angle.
     * @param compass the compass sensor to read from
     * @param angle the angle to turn to.
     * @param precision the precision with which to make the turn
     * @param motorVelocity the velocity for the motor
     * @param turnVelocity the velocity for the steering control. Note, no overshoot control. Make sure precision is sufficiently large
     */
    public AbsoluteTurn(CompassSensor compass, double angle, double precision, double motorVelocity,
                        double turnVelocity) {
        this.compass = compass;
        this.setTurnAngle(angle);
        this.precision          = precision;
        this.manualVelocityFlag = true;
        this.motorVelocity      = motorVelocity;
        this.turnVelocity       = turnVelocity;
    }

    /**
     * Sets a new Absolute angle to turn to.
     * @param angle New relative turn angle. Positive turns anti-clockwise, Negative turns clockwise
     */
    public final void setTurnAngle(double angle) {
        initialheading = compass.getOutput();
        turnAngle      = angle;
        desiredheading = turnAngle;
        turnPID.setSetPoint(desiredheading);
        turnDirection = ((turnAngle - initialheading) > 0)
                        ? -1
                        : 1;
        finished      = false;
    }

    /**
     * @inheritdoc
     */
    @Override
    public RobotOutput update() {
        output.setMotor(motorVelocity);
        output.setSteering(0.0);

        if (!finished) {
            double currentHeading = compass.getOutput();

            if (Math.abs(desiredheading - currentHeading) < precision) {
                finished = true;
            } else {
                if (manualVelocityFlag) {
                    output.setSteering(turnDirection * turnVelocity);
                } else {
                    output.setSteering(turnPID.updatePID(currentHeading));
                }
            }
        }

        return output;
    }

    /**
     * @inheritdoc
     */
    @Override
    public boolean isFinished() {
        return finished;
    }

    /**
     * @inheritdoc
     */
    @Override
    public void reset() {
        finished           = false;
        output             = new RobotOutput();
        precision          = Math.PI / 32;
        turnPID            = new PID(7.5, 0.05, 0.0);
        motorVelocity      = 20;
        manualVelocityFlag = false;
    }
}



