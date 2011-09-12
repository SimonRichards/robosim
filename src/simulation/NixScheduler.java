package simulation;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Scheduler provides a periodic call to the gameLoop function of a simulator.
 * The period may be changed or left at its default. The call period is guaranteed
 * to be constant as long as the gameLoop runtime period is shorter. If not
 * gameLoop will be called as fast as the host machine allows.
 *
 * This is the original model and is being kept as Swing Timers do not work as
 * advertised on the CAE2 Linux boxes.
 * @author Simon
 */
public class NixScheduler implements Scheduler {
    private static final double MAX_PERIOD = 10;
    private static final double MIN_PERIOD = 0.1;
    private long                period;
    private boolean             running;
    private final Simulator     sim;
    private TimerTask           tasker;
    private Timer               timer;

    /**
     * A scheduler object with the default frequency running the specified simulation
     * @param sim The simulation to run
     */
    public NixScheduler(final Simulator sim) {
        this.sim = sim;
        running  = false;
        period   = (long) (Simulator.DT * 1000);
        initialise();
    }

    /**
     * Checks to see whether simulation is running
     * @return true if running
     */
    @Override
    public boolean isRunning() {
        return running;
    }

    /**
     * For external control over the simulation's state. Go will resume a
     * paused simulation or set up and start a new one if previously stopped.
     */
    @Override
    public void play() {
        running = true;
    }

    /**
     * Freezes the simulation for later resumption.
     */
    @Override
    public void pause() {
        running = false;
    }

    /**
     * Halts the simulation, and resets the field.
     */
    @Override
    public void stop() {
        running = false;
        sim.reset();
    }

    /**
     * Destroy the timer object
     */
    public void cancel() {
        if (timer != null) {
            tasker.cancel();
        }
    }

    /**
     * (Re)create the scheduling timer
     */
    private void initialise() {
        tasker = new TimerTask() {
            @Override
            public void run() {
                if (running) {
                    sim.gameLoop();
                }

                sim.notifySubscribers();
            }
        };
        timer = new Timer("Game Loop");
        timer.scheduleAtFixedRate(tasker, 0, period);
    }

    /**
     * Adjusts the period, the maximum input is 10 and the minimum is 0.1
     * @param factor a number to be multiplied by the default period
     */
    @Override
    public void setTimeCoefficient(final Double factor) {
        if ((factor <= MAX_PERIOD) && (factor >= MIN_PERIOD)) {
            period = (long) (Simulator.DT * 1000 / factor);

            if (running) {
                cancel();
                initialise();
            }
        }
    }
}



