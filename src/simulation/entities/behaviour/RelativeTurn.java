package simulation.entities.behaviour;

import simulation.entities.RobotOutput;

import simulation.sensors.*;

/**
 * Behaviour for turning angle set angle. Can move forward or back, at any angle
 *
 * @author trc29, jermintiu
 *
 * @version 1.1
 * @version 1.2 18.07.2011
 */
public class RelativeTurn implements Behaviour {
    private final static double DEFAULT_TURN       = 0.0;
    private final static double DEFAULT_VELOCITY   = 20;
    private final static double DEFAULT_PRECISION  = Math.PI / 64;
    private boolean             finished           = false;
    private RobotOutput         output             = new RobotOutput();
    private PID                 turnPID            = new PID(7.5, 0.05, 0.0);
    private boolean             manualSteeringFlag = false;
    private CompassSensor       compass;
    private double              desiredheading;
    private double              initialheading;
    private boolean             initialized;
    private double              precision;        // When to register finished turn
    private double              steering;         // How much to apply to steering control
    private double              turnAngle;        // How much to turn by (radians)
    private double              turnDirection;    // Left(1) or Right(-1)
    private double              velocity;         // How much to apply to motor control

    /**
     * Create the ability to make relative turns
     * @param compass the Compass sensor to read from
     */
    public RelativeTurn(CompassSensor compass) {
        this(compass, DEFAULT_TURN);
    }

    /**
     * Turn through an angle to precision p while moving
     * @param compass the Compass sensor to read from
     * @param angle angle to turn. Positive angles for clockwise, negative for anti
     */
    public RelativeTurn(CompassSensor compass, double angle) {
        this(compass, angle, DEFAULT_PRECISION);
    }

    /**
     * Turn through an angle to precision p while moving forward
     * @param compass the Compass sensor to read from
     * @param angle angle to turn. Positive angles for clockwise, negative for anti
     * @param tprecision precision number of radians final heading is to be within from setpoint.
     */
    public RelativeTurn(CompassSensor compass, double angle, double tprecision) {
        this(compass, angle, tprecision, DEFAULT_VELOCITY);
    }

    /**
     * Turn through an angle to precision p while moving forward. This constructor NOT to be overwritten
     * @param compass the Compass sensor to read from
     * @param angle angle to turn. Positive angles for clockwise, negative for anti
     * @param tprecision precision number of radians final heading is to be within from setpoint.
     * @param tvelocity The velocity to turn at. If negative, the car will go backwards.
     */
    public RelativeTurn(CompassSensor compass, double angle, double tprecision, double tvelocity) {
        this.compass  = compass;
        turnAngle     = angle;
        velocity      = tvelocity;
        turnDirection = (angle > 0)
                        ? 1
                        : -1;
        precision     = tprecision;

        // manualSteeringFlag = false;
        finished    = false;
        initialized = false;
    }

    /**
     * Turn through an angle to precision p while moving forward
     * @param compass the Compass sensor to read from
     * @param angle angle to turn. Positive angles for clockwise, negative for anti (radians)
     * @param tprecision precision number of radians final heading is to be within from setpoint.
     * @param tvelocity The velocity to turn at. If negative, the car will go backwards.
     * @param tsteering The steering value to turn at
     */
    public RelativeTurn(CompassSensor compass, double angle, double tprecision, double tvelocity, double tsteering) {
        this.compass       = compass;
        turnAngle          = angle;
        velocity           = tvelocity;
        steering           = tsteering;
        turnDirection      = (angle > 0)
                             ? 1
                             : -1;
        precision          = tprecision;
        manualSteeringFlag = true;
        finished           = false;
        initialized        = false;
    }

    /**
     * Sets a new Absolute angle to turn to.
     * @param angle New relative turn angle. Positive turns anti-clockwise, Negative turns clockwise
     */
    public final void setTurnAngle(double angle) {
        initialheading = compass.getOutput();
        desiredheading = angle + initialheading;
        turnPID.setSetPoint(desiredheading);
        turnDirection = ((angle) > 0)
                        ? -1
                        : 1;
        finished      = false;
    }

    /**
     * @inheritdoc
     */
    @Override
    public RobotOutput update() {
        output.setMotor(0.0);
        output.setSteering(0.0);

        if ((!initialized) && (compass.isInitialized())) {
            initialized    = true;
            finished       = false;
            initialheading = compass.getOutput();
            desiredheading = initialheading + turnAngle;
            turnPID.setSetPoint(desiredheading);
        }

        if (!finished) {
            double currentHeading = compass.getOutput();

            if (Math.abs(currentHeading - desiredheading) < precision) {
                finished = true;
            } else {
                output.setMotor(velocity);

                if (!manualSteeringFlag) {
                    output.setSteering(turnPID.updatePID(currentHeading));
                } else {
                    output.setSteering(steering * turnDirection);
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
        velocity           = 20;
        manualSteeringFlag = false;
    }
}



