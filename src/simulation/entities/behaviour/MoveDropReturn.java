
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
 * Supppose to turn around, drive a bit, and turn around again...incomplete
 *
 * @author Jermin
 * @WalkedThrough
 * @DeskChecked
 */
public class MoveDropReturn implements Behaviour {
    private boolean      isFinished = false;
    private boolean      dropped    = false;;
    private Drive        drive;
    private RobotOutput  output;
    private RelativeTurn turn;

    /**
     * Do a one 180, and drive, drop, then come back
     * @param compass CompassSensor
     * @param velocity VelocitySensor
     */
    public MoveDropReturn(CompassSensor compass, VelocitySensor velocity) {
        turn   = new RelativeTurn(compass, Math.PI, 0.5, -10);
        drive  = new Drive(velocity, 50, 50, 300);
        output = new RobotOutput();
    }

    /**
     * @inheritdoc
     *
     * @return
     */
    @Override
    public RobotOutput update() {
        output.setMotor(0);
        output.setSteering(0);

        if (!isFinished) {
            output = turn.update();

            if (turn.isFinished()) {
                output = drive.update();

                if (dropped) {
                    isFinished = true;
                }

                if (drive.isFinished()) {
                    output.setArm(false);
                    dropped = true;
                    turn.setTurnAngle(Math.PI);
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



