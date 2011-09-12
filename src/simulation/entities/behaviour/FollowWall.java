
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package simulation.entities.behaviour;

import simulation.entities.RobotOutput;

import simulation.sensors.*;

/**
 *
 * @author timcrow
 */
public class FollowWall implements Behaviour {
    private static final double  DEFAULT_PROXIMITY      = 50;
    private static final double  DEFAULT_VELOCITY       = 100;
    private static final double  HISTERESIS_LOW_BOUND   = 0.95;
    private static final double  HISTERESIS_UPPER_BOUND = 1.05;
    private double               direction              = 0.0;
    private RobotOutput          output                 = new RobotOutput();
    private boolean              finished               = false;
    private final DistanceSensor distancesensor;
    private Drive                drive;
    private double               proximity;
    private RelativeTurn         steer;

    /**
     * Travels around the wall on one side
     * @param compass             Compass sensor
     * @param distancesensor        DistanceSensor at angle on side you want it at. Should be at angle
     * @param velocitysensor        VelocitySensor
     * @param antiClockwise true for LEFT sensor, false for RIGHT sensor
     */
    public FollowWall(CompassSensor compass, VelocitySensor velocitysensor, DistanceSensor distancesensor,
                      boolean antiClockwise) {
        this(compass, velocitysensor, distancesensor, DEFAULT_PROXIMITY, antiClockwise);
    }

    /**
     * Travels around the wall on one side
     * @param compass             Compass sensor
     * @param distancesensor        DistanceSensor at angle on side you want it at. Should be at angle
     * @param velocitysensor        VelocitySensor
     * @param proximity             the distance detected at which to react
     * @param antiClockwise true for LEFT sensor, false for RIGHT sensor
     */
    public FollowWall(CompassSensor compass, VelocitySensor velocitysensor, DistanceSensor distancesensor,
                      double proximity, boolean antiClockwise) {
        this(compass, velocitysensor, distancesensor, DEFAULT_VELOCITY, proximity, antiClockwise);
    }

    /**
     * Travels around the wall on one side
     * @param compass             Compass sensor
     * @param distancesensor        DistanceSensor at angle on side you want it at. Should be at angle
     * @param velocitysensor        VelocitySensor
     * @param velocity              the velocity at which to follow the wall
     * @param proximity             the distance detected at which to react
     * @param antiClockwise true for LEFT sensor, false for RIGHT sensor
     */
    public FollowWall(CompassSensor compass, VelocitySensor velocitysensor, DistanceSensor distancesensor,
                      double velocity, double proximity, boolean antiClockwise) {
        this.distancesensor = distancesensor;
        direction           = (antiClockwise)
                              ? -1.0
                              : 1.0;
        this.proximity      = proximity;
        drive               = new Drive(velocitysensor, velocity);
        steer               = new RelativeTurn(compass);
    }

    /**
     * @inheritdoc
     */
    @Override
    public RobotOutput update() {
        double distancesensorOutput = distancesensor.getOutput();

        if (distancesensor.getOutput() < HISTERESIS_LOW_BOUND * proximity) {             // move away from wall
            steer.setTurnAngle(direction * Math.PI / 11);
        } else if (distancesensor.getOutput() > HISTERESIS_UPPER_BOUND * proximity) {    // otherwise move toward wall
            steer.setTurnAngle(-direction * Math.PI / 11);
        } else {
            steer.setTurnAngle(0.0);
        }

        if (distancesensorOutput > distancesensor.getMaxRange()) {
            steer.setTurnAngle(0.0);
        }

        output.setMotor(drive.update().getMotor());
        output.setSteering(steer.update().getSteering());

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
        throw new UnsupportedOperationException("Not supported yet.");
    }
}



