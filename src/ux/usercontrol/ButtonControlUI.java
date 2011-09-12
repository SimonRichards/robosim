package ux.usercontrol;

import net.miginfocom.swing.MigLayout;

import simulation.Simulator;

import ux.usercontrol.SimulatorController.Mode;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * An example controller.
 * @author Simon
 */
public class ButtonControlUI extends JPanel implements SimulatorControlGui {
    private SimulatorController controller;
    private JButton             editButton;
    private JButton             pauseButton;
    private JButton             resetButton;
    private JButton             startButton;
    private JLabel              timeFactorLabel;

    // private JSlider timeFactorSlider;
    private JSpinner       timeFactorSpinner;
    private ChangeListener velocityListener;

    /**
     * Constructs a new gui element
     * @param controller The controller to defer user input to
     */
    public ButtonControlUI(SimulatorController controller) {
        this.controller = controller;
        controller.addControlGui(this);
        createGui();
    }

    final protected void createGui() {
        setLayout(new MigLayout("", "[][grow][]"));
        startButton = new JButton("Start");
        pauseButton = new JButton("Pause");
        resetButton = new JButton("Reset");
        editButton  = new JButton("Edit");

        ButtonListener buttonListener = new ButtonListener();

        startButton.addActionListener(buttonListener);
        pauseButton.addActionListener(buttonListener);
        resetButton.addActionListener(buttonListener);
        editButton.addActionListener(buttonListener);
        timeFactorLabel   = new JLabel("Running Velocity Factor");
        timeFactorSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 1000, 0.1));

        JComponent editor = new JSpinner.NumberEditor(timeFactorSpinner);

        timeFactorSpinner.setEditor(editor);
        timeFactorSpinner.setValue(1.0);
        timeFactorSpinner.addChangeListener(new TimeFactorListener());
        add(timeFactorLabel);
        add(timeFactorSpinner, "grow, gapright 20");
        add(editButton);
        add(resetButton);
        add(startButton);
    }

    /**
     * Sets the Reset button
     *
     */
    public void setResetted() {
        setButtonStates(true, false, false);
        remove(pauseButton);
        remove(startButton);
        add(startButton);
        validate();
        repaint();
    }

    /**
     * Sets the Pause button
     *
     */
    public void setPaused() {
        setButtonStates(true, false, true);
        remove(pauseButton);
        add(startButton);
        validate();
        repaint();
    }

    /**
     * Sets the Start button
     *
     */
    public void setStarted() {
        setButtonStates(false, true, true);
        remove(startButton);
        add(pauseButton);
        validate();
        repaint();
    }
    /**
     * Sets the state of the buttons based on the variables.
     *
     * @param start Start button
     * @param pause Pause button
     * @param stop Stop button
     */
    private void setButtonStates(boolean start, boolean pause, boolean stop) {
        startButton.setEnabled(start);
        pauseButton.setEnabled(pause);
        resetButton.setEnabled(stop);
    }

    /**
     * Method description
     *
     * 
     *   
     *
     * @return
     */
    public Double getTimeFactor() {
        return (Double) timeFactorSpinner.getValue();
    }

    @Override
    public void setMode(Mode mode) {    // Nothing need to be done here
    }

    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            Object source = event.getSource();

            if (source == startButton) {
                controller.startSimulator();
            } else if (source == resetButton) {
                controller.resetSimulator();
            } else if (source == pauseButton) {
                controller.pauseSimulator();
            } else if (source == editButton) {
                setPaused();
                controller.setMode(SimulatorController.Mode.EDIT);
            }
        }
    }


    private class TimeFactorListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            controller.setTimeCoefficient(getTimeFactor());
        }
    }
    
    
}



