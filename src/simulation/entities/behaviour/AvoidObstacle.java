
/**
 * AvoidObstacle.java
 *
 * @author Samuel, Jermin Tiu
 * @version 18.07.2011
 */
package simulation.entities.behaviour;

import simulation.entities.RobotOutput;

import simulation.sensors.*;

/**
 * AvoidObstacle.java
 * Behaviour to make robot drive around, avoiding hitting Things or
 * impassable Terrain. Runs into corners
 *
 * @author Samuel
 * @WalkedThrough
 * @DeskChecked
 */
public class AvoidObstacle implements Behaviour {
    private static final double  DEFAULT_PROXIMITY = 50;
    private static final double  DEFAULT_VELOCITY  = 100;
    private static final double  WALL_PROXIMITY    = 250;
    private boolean              isFinished        = false;
    private RobotOutput          output            = new RobotOutput();
    private Drive                drive;
    private Behaviour            followWallAntiCLK;
    private Behaviour            followWallCLK;
    private final DistanceSensor leftdistancesensor;
    private final DistanceSensor rightdistancesensor;

    /**
     * drive around randomly while avoiding any obstacle
     * @param velocitysensor the Velocity sensor to read from
     * @param compass The compass to read from
     * @param leftdistancesensor left rangefinder
     * @param rightdistancesensor right rangefinder
     */
    public AvoidObstacle(VelocitySensor velocitysensor, CompassSensor compass, DistanceSensor leftdistancesensor,
                         DistanceSensor rightdistancesensor) {
        this(velocitysensor, compass, leftdistancesensor, rightdistancesensor, DEFAULT_PROXIMITY);
    }

    /**
     * drive around randomly while avoiding any obstacle
     * @param velocitysensor the Velocity sensor to read from
     * @param compass The compass to read from
     * @param leftdistancesensor left rangefinder
     * @param rightdistancesensor right rangefinder
     * @param proximity proximity to follow wall at
     */
    public AvoidObstacle(VelocitySensor velocitysensor, CompassSensor compass, DistanceSensor leftdistancesensor,
                         DistanceSensor rightdistancesensor, double proximity) {
        this(velocitysensor, compass, leftdistancesensor, rightdistancesensor, proximity, DEFAULT_VELOCITY);
    }

    /**
     * drive around randomly while avoiding any obstacle
     * @param velocitysensor the Velocity sensor to read from
     * @param compass The compass to read from
     * @param leftdistancesensor left rangefinder
     * @param rightdistancesensor right rangefinder
     * @param proximity proximity to follow wall at
     * @param velocity velocity to follow wall at
     */
    public AvoidObstacle(VelocitySensor velocitysensor, CompassSensor compass, DistanceSensor leftdistancesensor,
                         DistanceSensor rightdistancesensor, double proximity, double velocity) {
        this.rightdistancesensor = rightdistancesensor;
        this.leftdistancesensor  = leftdistancesensor;
        drive                    = new Drive(velocitysensor, velocity);
        followWallCLK            = new FollowWall(compass, velocitysensor, leftdistancesensor, proximity, velocity,
                true);
        followWallAntiCLK        = new FollowWall(compass, velocitysensor, rightdistancesensor, proximity, velocity,
                false);
    }

    /**
     * @param velocity The new velocity to avoid obstacles at.
     */
    public void setVelocity(double velocity) {
        drive.setDesiredVelocity(velocity);
    }

    /**
     * @inheritdoc
     */
    @Override
    public RobotOutput update() {
        output.setMotor(0.0);
        output.setSteering(0.0);

        if (leftdistancesensor.getOutput() < WALL_PROXIMITY) {
            output.setSteering(followWallCLK.update().getSteering());
        }

        if (rightdistancesensor.getOutput() < WALL_PROXIMITY) {
            output.setSteering(followWallAntiCLK.update().getSteering());
        }

        output.setMotor(drive.update().getMotor());

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



