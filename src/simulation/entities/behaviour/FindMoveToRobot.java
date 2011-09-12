package simulation.entities.behaviour;

import simulation.entities.RobotOutput;

import simulation.sensors.*;
import simulation.sensors.Camera.CameraTarget;

/**
 * Finds a block, picks it up, then searches for more
 * @author  Jermin Tiu
 *
 * @verion 1.0 20.07.2011
 */
public class FindMoveToRobot implements Behaviour {
    private double       velocity = 200;
    private Behaviour    altBehaviour;
    private Camera       cam;
    private MoveToObject mover;
    private RobotOutput  output;

    public FindMoveToRobot(Camera camera, VelocitySensor velocityo, CompassSensor compass, Behaviour searchBehaviour) {
        mover        = new MoveToObject(camera, velocityo, compass, CameraTarget.ROBOTS, 60, velocity);
        output       = new RobotOutput();
        cam          = camera;
        altBehaviour = searchBehaviour;
    }

    @Override
    public RobotOutput update() {
        if (cam.hasTarget()) {
            output = mover.update();
        } else {
            output = altBehaviour.update();
        }

        return output;
    }

    @Override
    public boolean isFinished() {
        return mover.isFinished();
    }

    @Override
    public void reset() {

        // do nothing
    }
}



