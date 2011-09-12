package simulation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

/**
 * Scheduler provides a periodic call to the gameLoop function of a simulator.
 * The period may be changed or left at its default. The call period is guaranteed
 * to be constant as long as the gameLoop runtime period is shorter. If not
 * gameLoop will be called as fast as the host machine allows.
 *
 * Class now utilizes the swing timer as opposed to the original util timer, which
 * would over-compensate for lag by ramping velocity up past the desired velocity to
 * 'catch up' to the elapsed time which should have passed. Swing timer allows
 * time to 'drift' so does not have this overshoot problem.
 *
 * Does not operate correctly on CAE2 linux boxes
 *
 * @author Simon, Andrew Dutton
 */
public class WinScheduler implements Scheduler {
    public static final double MAX_FACTOR = 10;
    public static final double MIN_FACTOR = 0.1;
    private long               period;
    private boolean            running;
    private final Simulator    sim;
    private ActionListener     tasker;
    private Timer              timer;

    /**
     * A scheduler object with the default frequency running the specified simulation
     * @param sim The simulation to run
     */
    public WinScheduler(final Simulator sim) {
        this.sim = sim;
        running  = false;
        period   = (long) (Simulator.DT * 1000);
        initialise();
        timer.start();
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
    public void play() {
        running = true;
        timer.start();
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
     * (Re)create the scheduling timer
     */
    private void initialise() {
        tasker = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (running) {
                    sim.gameLoop();
                }

                sim.notifySubscribers();
            }
        };
        timer = new Timer((int) period, tasker);
    }

    /**
     * Adjusts the period, the maximum input is 10 and the minimum is 0.1
     * @param factor a number to be multiplied by the default period
     */
    @Override
    public void setTimeCoefficient(final Double factor) {
        if ((factor <= MAX_FACTOR) && (factor >= MIN_FACTOR)) {
            period = (long) (Simulator.DT * 1000 / factor);
            timer.setDelay((int) period);
            timer.start();
        }
    }
}



