package simulation.entities.behaviour;

import simulation.Simulator;

import simulation.entities.RobotOutput;

import simulation.sensors.VelocitySensor;

/**
 * Drive.java
 * Drives the robot at a given velocity for a given time or distance
 *
 * @author trc29
 * @Walkedthrough
 * @Deskchecked Ben
 */
public class Drive implements Behaviour {
    private static final double  DEFAULT_VELOCITY = 100;
    private double               desiredvelocity  = 100;
    private boolean              finished         = false;
    private final RobotOutput    output           = new RobotOutput();
    private boolean              distanceSetFlag  = false;
    private boolean              timeSetFlag      = false;
    private PID                  velocityPID      = new PID(5, 0.001, 0.01);
    private double               distanceToTravel;
    private double               time;
    private final VelocitySensor velocitySensor;

    /**
     * Move at a defined velocity for a defined time or until it has moved a defined distance
     * @param velocitySensor the Velocity sensor to read from
     */
    public Drive(VelocitySensor velocitySensor) {
        this(velocitySensor, DEFAULT_VELOCITY);
    }

    /**
     * Move at a defined velocity for a defined time or until it has moved a defined distance
     * @param velocitySensor the Velocity sensor to read from
     * @param velocity the velocity to travel at
     */
    public Drive(VelocitySensor velocitySensor, double velocity) {
        this.velocitySensor = velocitySensor;
        desiredvelocity     = velocity;
        velocityPID.setSetPoint(desiredvelocity);
    }

    /**
     * Move at a defined velocity for a defined time or until it has moved a defined distance
     * @param velocitySensor the Velocity sensor to read from
     * @param velocity the velocity to travel at
     * @param time the time for which to drive at this velocity in seconds.
     */
    public Drive(VelocitySensor velocitySensor, double velocity, double time) {
        this.velocitySensor = velocitySensor;
        desiredvelocity     = velocity;
        velocityPID.setSetPoint(desiredvelocity);
        timeSetFlag = true;
        this.time   = time;
    }

    /**
     * Move at a defined velocity for a defined time or until it has moved a defined distance
     * @param velocitySensor the Velocity sensor to read from
     * @param velocity the velocity to travel at
     * @param time the time for which to drive at this velocity in seconds.
     * @param distanceToTravel the distance to travel
     */
    public Drive(VelocitySensor velocitySensor, double velocity, double time, double distanceToTravel) {
        this.velocitySensor = velocitySensor;
        desiredvelocity     = velocity;
        velocityPID.setSetPoint(desiredvelocity);
        timeSetFlag           = true;
        distanceSetFlag       = true;
        this.distanceToTravel = distanceToTravel;
        this.time             = time;
    }

    /**
     * Sets the velocity at which the robot will move
     * @param velocity The velocity the robot will drive at.
     */
    public void setDesiredVelocity(double velocity) {
        desiredvelocity = velocity;
        velocityPID.setSetPoint(desiredvelocity);
        finished = false;
    }

    /**
     * Sets the time to drive for in seconds.
     * @param time the time for which to drive at this velocity in seconds.
     */
    public void setDesiredTime(double time) {
        timeSetFlag = true;
        this.time   = time;
        finished    = false;
    }

    /**
     * Sets the absolute distance to drive for. Negative distances will not work.
     * @param distanceToTravel the Distance for which to drive at this velocity
     */
    public void setDesiredDistance(double distanceToTravel) {
        distanceSetFlag       = true;
        this.distanceToTravel = distanceToTravel;
        finished              = false;
    }

    /**
     * @inheritdoc
     */
    @Override
    public RobotOutput update() {
        output.setMotor(0.0);
        output.setSteering(0.0);

        if (!finished) {
            if (timeSetFlag) {    // if exiting based on time or distance
                time -= Simulator.DT;

                if (time < 0) {
                    finished    = true;
                    timeSetFlag = false;
                    velocityPID.setSetPoint(0.0);
                }
            }

            if (distanceSetFlag) {
                distanceToTravel -= Math.abs(velocitySensor.getOutput()) * Simulator.DT;

                if (distanceToTravel < 0) {
                    finished        = true;
                    distanceSetFlag = false;
                    velocityPID.setSetPoint(0.0);
                }
            }
        }

        output.setMotor(velocityPID.updatePID(velocitySensor.getOutput()));

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
        finished = false;
    }
}



