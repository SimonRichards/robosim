package simulation;

/**
 * An interface that allows control over the simulator
 * 
 * @author amd140
 */
public interface Scheduler {

    /**
     * Freezes the simulation for later resumption.
     */
    void pause();

    /**
     * For external control over the simulation's state. Go will resume a
     * paused simulation or set up and start a new one if previously stopped.
     */
    void play();

    /**
     * Adjusts the period, the maximum input is 10 and the minimum is 0.1
     * @param factor a number to be multiplied by the default period
     */
    void setTimeCoefficient(final Double factor);

    /**
     * Halts the simulation, and resets the field.
     */
    void stop();

    /**
     * Discerns whether the simulation state is running
     * @return true if simulator running
     */
    boolean isRunning();
}



