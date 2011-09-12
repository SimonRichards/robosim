package ux.usercontrol;

import publishersubscriber.SimulatorPublisher;
import publishersubscriber.SimulatorSubscriber;
import simulation.Scheduler;
import simulation.Simulator;
import java.util.ArrayList;
import java.util.Collection;
import javax.script.ScriptException;

/**
 * Top level stuff.
 * @author Simon
 */
public class SimulatorController implements SimulatorSubscriber {
    private Collection<SimulatorControlGui> controlGuis = new ArrayList<SimulatorControlGui>();
    private Mode                            mode        = Mode.EDIT;
    private DeflatedSimulator               backupSimulator;
    private ErrorOutput                     errorOutput;
    private Scheduler                       scheduler;
    private Simulator                       simulator;
    private boolean                         invalidReset = false;

    public static enum Mode { SIMULATION, EDIT }

    /**
     * Set's up the controller GUI.
     * @param simulator
     * @param errorOutput
     */
    public SimulatorController(Simulator simulator, ErrorOutput errorOutput) {
        this.scheduler   = simulator.getScheduler();
        this.simulator   = simulator;
        this.errorOutput = errorOutput;
    }

    public void startSimulator() {
        if (mode == Mode.SIMULATION) {
            setGuiStarted();
            scheduler.play();
        }
    }

    public void resetSimulator() {
        setGuiResetted();
        scheduler.stop();
        if (!invalidReset) {
            simulator.inflate(backupSimulator);
        }
        try {
            backupSimulator = new DeflatedSimulator(simulator);
        } catch (Exception e) {
            System.err.println(e);
            invalidReset = true;
        }
    }

    public void pauseSimulator() {
        if (mode == Mode.SIMULATION) {
            setGuiPaused();
            scheduler.pause();
        }
    }

    /**
     * sets the simulation mode to either simulate or active
     * @param mode 
     */
    public void setMode(Mode mode) {
        this.mode = mode;

        switch (mode) {
        case EDIT :
            setGuiMode(mode);
            scheduler.pause();

            break;

        case SIMULATION :
            setGuiMode(mode);
            invalidReset = false;
            try {
                backupSimulator = new DeflatedSimulator(simulator);
            } catch (Exception e) {
                System.err.println(e);
                invalidReset = true;
            }

            break;
        }
    }

    @Override
    public void update(SimulatorPublisher publisher) {
        ScriptException scriptIssue = publisher.getIssue();

        if (scriptIssue != null) {
            setGuiPaused();
            scheduler.pause();

            String message = scriptIssue.toString();

            if (scriptIssue.getLineNumber() >= 0) {
                message += " on line " + scriptIssue.getLineNumber();
            }

            errorOutput.displayMessage("Script Error", message);
        }
    }

    @Override
    public void simulatorResetted(SimulatorPublisher publisher) {

        // nothing needed to be done.
    }

    public void clearAllSimulatorObjects() {
        simulator.clearAll();
    }

    public void addControlGui(SimulatorControlGui controlGui) {
        controlGuis.add(controlGui);
    }

    public void setTimeCoefficient(Double timeFactor) {
        scheduler.setTimeCoefficient(timeFactor);
    }

    private void setGuiResetted() {
        for (SimulatorControlGui controlGui : controlGuis) {
            controlGui.setResetted();
        }
    }

    private void setGuiStarted() {
        for (SimulatorControlGui controlGui : controlGuis) {
            controlGui.setStarted();
        }
    }

    private void setGuiPaused() {
        for (SimulatorControlGui controlGui : controlGuis) {
            controlGui.setPaused();
        }
    }

    private void setGuiMode(Mode mode) {
        for (SimulatorControlGui controlGui : controlGuis) {
            controlGui.setMode(mode);
        }
    }
}



