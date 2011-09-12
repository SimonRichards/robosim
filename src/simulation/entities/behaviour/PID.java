
/**
 * PID.java
 *
 * @author Tim Crow, Simon
 */
package simulation.entities.behaviour;

import simulation.Simulator;

/**
 * PID.java
 * A PID controller class
 *
 * @author Tim Crow, Simon
 * @WalkedThrough
 * @DeskChecked Ben
 */
public class PID {
    private double       intError;
    private final double kd;    // Differential gain.
    private final double ki;    // Integral gain.
    private final double kp;    // Proportional gain.
    private double       previousValue;
    private double       target;

    /**
     * PID controller with user defined gains
     * @param proportional Proportional Gain of the controller
     * @param integral Integral Gain of the controller
     * @param differential Differential Gain of the controller
     */
    public PID(double proportional, double integral, double differential) {
        kp = proportional;
        ki = integral;
        kd = differential;
    }

    /**
     * changes the setpoint of the controller.
     * @param targetValue Target value to PID towards
     */
    public void setSetPoint(double targetValue) {
        intError = 0;
        target   = targetValue;
    }

    /**
     * Calculates the control effort for the system
     * @param currentValue The current value of the controlled variable
     * @return output the output value of the controller
     */
    public double updatePID(double currentValue) {
        double error = target - currentValue;

        intError += error * Simulator.DT;

        double derError = ((currentValue - previousValue) / Simulator.DT);
        double output   = kp * error + ki * intError + kd * derError;

        previousValue = currentValue;

        return output;
    }
}



