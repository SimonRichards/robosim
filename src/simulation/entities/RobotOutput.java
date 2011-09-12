package simulation.entities;

/**
 * This class includes the outputs for a particular robot
 * @author Simon
 */
public class RobotOutput {
    public final static double MAX_MOTOR = 100;
    public final static double MAX_TURN  = 15;
    private boolean            armActive;
    private double             motor;
    private double             steering;

    /**
     * @return true if robot arm is active
     */
    public boolean isArmActive() {
        return armActive;
    }

    /**
     * @param arm set true to activate the robot arm
     */
    public void setArm(boolean arm) {
        this.armActive = arm;
    }

    /**
     * @return motor The motor speed of the robot
     */
    public double getMotor() {
        return motor;
    }

    /**
     * @param motor The motor speed of the robot
     */
    public void setMotor(double motor) {
        this.motor = (motor < MAX_MOTOR)
                     ? ((motor > -MAX_MOTOR)
                        ? motor
                        : -MAX_MOTOR)
                     : MAX_MOTOR;
    }

    /**
     * @return steering A double that represents the robots current steering angle
     */
    public double getSteering() {
        return steering;
    }

    /**
     * @param steering Set the robots current steering angle
     */
    public void setSteering(double steering) {
        this.steering = (steering < MAX_TURN)
                        ? ((steering > -MAX_TURN)
                           ? steering
                           : -MAX_TURN)
                        : MAX_TURN;
    }
}



