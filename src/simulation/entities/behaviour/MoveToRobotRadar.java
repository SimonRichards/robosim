package simulation.entities.behaviour;

import simulation.entities.RobotOutput;

import simulation.sensors.*;

/**
 * Drives the robot towards a cup and stops a specified distance away.
 * @author Sam Sanson
 *
 * @verion 1.0 06.07.2011
 * @version 1.1
 */
public class MoveToRobotRadar implements Behaviour {
    private boolean           isFinished = false;            // The current status of the behaviour
    private double            stopDist   = 20;               // The distance from the cup to end the behaviour
    private double            velocity   = 60;               // The velocity the robot approaches a cup
    private double            turn       = -Math.PI / 10;    // The default turning angle when no cup detected
    private final RobotOutput output     = new RobotOutput();
    private Drive             drive;
    private RobotRadar        radar;
    private RelativeTurn      steer;

    /**
     * Creates a behaviour where the robot will drive in a circle until a cup is
     * spotted and then will drive up to the cup.
     * @param radar the radar that will be used to detect robot
     * @param velocityo Velocity Sensor used for maintaining velocity
     * @param compass Compass Sensor used for turning.
     *
     */
    public MoveToRobotRadar(RobotRadar radar, VelocitySensor velocityo, CompassSensor compass) {
        this(radar, velocityo, compass, 20);
    }

    /**
     * Creates a behaviour where the robot will drive in a circle until a cup is
     * spotted and then will drive up to the cup.
     * @param radar the radar that will be used to detect robot
     * @param velocityo Velocity Sensor used for maintaining velocity
     * @param compass Compass Sensor used for turning.
     * @param stopDist The distance to stop from the robot being followed
     */
    public MoveToRobotRadar(RobotRadar radar, VelocitySensor velocityo, CompassSensor compass, double stopDist) {
        this(radar, velocityo, compass, stopDist, 60);
    }

    /**
     * Creates a behaviour where the robot will drive in a circle until a cup is
     * spotted and then will drive up to the cup.
     * @param radar the radar that will be used to detect robot
     * @param velocityo Velocity Sensor used for maintaining velocity
     * @param compass Compass Sensor used for turning.
     * @param stopDist The distance to stop from the robot being followed
     * @param velocity The velocity at which to move to a robot.
     */
    public MoveToRobotRadar(RobotRadar radar, VelocitySensor velocityo, CompassSensor compass, double stopDist,
                            double velocity) {
        this.radar    = radar;
        this.stopDist = stopDist;
        this.velocity = velocity;
        drive         = new Drive(velocityo, velocity);
        steer         = new RelativeTurn(compass);
    }

    /**
     * @inheritdoc
     */
    public RobotOutput update() {
        output.setMotor(0.0);
        output.setSteering(0.0);

        if (!isFinished) {

            // Get the result from the camera
            double[] robotData  = radar.getOutput();
            double   robotDist  = robotData[0];
            double   robotAngle = robotData[1];

            // Check for no cup detected
            if (!radar.isRobot()) {
                drive.setDesiredVelocity(velocity);
                steer.setTurnAngle(turn);
            } else if (robotDist < stopDist) {    // Cup in correct position
                isFinished = true;
                drive.setDesiredVelocity(0.0);
            } else {                              // Move towards cup
                output.setMotor(velocity);
                steer.setTurnAngle(robotAngle);
            }
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



