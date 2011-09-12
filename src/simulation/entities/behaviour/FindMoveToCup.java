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
public class FindMoveToCup implements Behaviour {
    private boolean          isFinished   = false;
    private int              numCollected = 0;
    private Behaviour        altBehaviour;
    private Camera           cam;
    private CollectionSensor collect;
    private MoveToObject     mover;
    private RobotOutput      output;

    /**
     * Finds, moves to then searches for another cups.
     * @param camera Camera used for finding cups
     * @param velocitysensor the Velocity sensor to read from
     * @param compass the compass to read
     * @param collector the collector to use to check if the cup can be picked up
     * @param searchBehaviour The behaviour used for searching for cups
     */
    public FindMoveToCup(Camera camera, VelocitySensor velocitysensor, CompassSensor compass,
                         CollectionSensor collector, Behaviour searchBehaviour) {
        mover        = new MoveToObject(camera, velocitysensor, compass, CameraTarget.CUPS);
        output       = new RobotOutput();
        cam          = camera;
        collect      = collector;
        altBehaviour = searchBehaviour;
    }

    /**
     * @inheritdoc
     */
    @Override
    public RobotOutput update() {
        if (collect.getCollectionCount() > numCollected) {    // if you've collected something, move on
            mover.reset();
            numCollected = collect.getCollectionCount();
        }

        if (cam.hasTarget()) {
            output = mover.update();

            if (mover.isFinished()) {
                output.setArm(true);
            }
        } else {
            output = altBehaviour.update();
        }

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
        numCollected = 0;
    }

    /**
     * @return numCollected The number of cups collected so far
     */
    public int getNumCollected() {
        return numCollected;
    }
}



