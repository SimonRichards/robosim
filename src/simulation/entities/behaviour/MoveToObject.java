package simulation.entities.behaviour;

import simulation.entities.RobotOutput;

import simulation.sensors.*;
import simulation.sensors.Camera.CameraTarget;

/**
 * Drives towards a given object type.
 * @author Sam Sanson, Jermin Tiu
 *
 * @verion 1.0 06.07.2011
 * @version 1.1
 * @version 2.0 20.07.2011
 */
public class MoveToObject implements Behaviour {
    private static final double DEFAULT_STOP_DIST = 70;
    private static final double DEFAULT_VELOCITY  = 80;
    private boolean             isFinished        = false;    // The current status of the behaviour
    private final RobotOutput   output            = new RobotOutput();
    private Camera              camera;                       // The camera detecting the cup
    private Drive               drive;
    private RelativeTurn        steer;
    private double              stopDist;                     // The distance from the cup to end the behaviour
    private double              velocity;                     // The velocity the robot approaches a cup

    /**
     * Will drive to the nearest specified object within range.
     * @param camera the camera that will be used to detect objects
     * @param velocitysensor the Velocity sensor to read from
     * @param compass The compass to read from
     */
    public MoveToObject(Camera camera, VelocitySensor velocitysensor, CompassSensor compass) {
        this(camera, velocitysensor, compass, CameraTarget.CUPS, DEFAULT_STOP_DIST);
    }

    /**
     * Will drive to the nearest specified object within range.
     * @param camera the camera that will be used to detect objects
     * @param velocitysensor the Velocity sensor to read from
     * @param compass The compass to read from
     * @param target What type of object to move towards
     */
    public MoveToObject(Camera camera, VelocitySensor velocitysensor, CompassSensor compass, CameraTarget target) {
        this(camera, velocitysensor, compass, target, DEFAULT_STOP_DIST);
    }

    /**
     * Will drive to the nearest specified object within range.
     * @param camera the camera that will be used to detect objects
     * @param velocitysensor the Velocity sensor to read from
     * @param compass The compass to read from
     * @param target What type of object to move towards
     * @param stopDist the distance from the object to stop at
     */
    public MoveToObject(Camera camera, VelocitySensor velocitysensor, CompassSensor compass,
                        Camera.CameraTarget target, double stopDist) {
        this(camera, velocitysensor, compass, target, stopDist, DEFAULT_VELOCITY);
    }

    /**
     * Will drive to the nearest specified object within range.
     * @param camera the camera that will be used to detect objects
     * @param velocitysensor the Velocity sensor to read from
     * @param compass The compass to read from
     * @param target What type of object to move towards
     * @param stopDist the distance from the object to stop at
     * @param velocity the velocity the robot approaches the object
     */
    public MoveToObject(Camera camera, VelocitySensor velocitysensor, CompassSensor compass,
                        Camera.CameraTarget target, double stopDist, double velocity) {
        this.camera   = camera;
        steer         = new RelativeTurn(compass);
        this.stopDist = stopDist;
        this.velocity = velocity;
        this.camera.setTargetObject(target);
        drive = new Drive(velocitysensor, velocity);
    }

    /**
     * @inheritdoc
     */
    @Override
    public RobotOutput update() {
        output.setMotor(0.0);
        output.setSteering(0.0);
        isFinished = false;

        // Get the result from the camera
        double objDist  = camera.getDistToTarget();
        double objAngle = camera.getAngToTarget();

        // Check for no object detected
        if ((objDist == 0) && (objAngle == 0)) {
            isFinished = true;
        } else if (objDist < stopDist) {    // object in correct position
            isFinished = true;
            drive.setDesiredVelocity(0.0);
        } else {                            // Move towards object
            drive.setDesiredVelocity(velocity);
            steer.setTurnAngle(-objAngle);
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



