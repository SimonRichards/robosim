package ux.display.designer;

import net.miginfocom.swing.MigLayout;

import simulation.Simulator;

import simulation.entities.Cup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * The dialog box for creating inanimate objects (cups and blocks) and inserting them into a simulator.
 * @author Sam Sanson
 */
public class InanimateCreationDialog extends JFrame implements ActionListener, PropertyChangeListener {
    private int FIELD_WIDTH = 10;

    // Suggested robot
    private double       START_X = 100;
    private double       START_Y = 100;
    private JButton      cancelButton;
    private JRadioButton cupEmptyButton;
    private JRadioButton cupFullButton;
    private JButton      okButton;
    private Simulator    simulator;

    // Inanimate parameters
    private double              xPos;
    private JFormattedTextField xPosField;
    private JLabel              xPosLabel;
    private double              yPos;
    private JFormattedTextField yPosField;
    private JLabel              yPosLabel;

    /**
     * Constructs a dialog box that allows selection of the inanimate object (cup or block)
     * and associated values.
     *
     *
     * @param simulator the simulator to add the inanimate to
     */
    public InanimateCreationDialog(Simulator simulator) {
        this.simulator = simulator;

        // Create number fields
        xPosField = new JFormattedTextField(NumberFormat.getInstance());
        yPosField = new JFormattedTextField(NumberFormat.getInstance());

        // Create number field labels
        xPosLabel = new JLabel("x: ");
        yPosLabel = new JLabel("y: ");

        // Setup the size of the fields
        xPosField.setColumns(FIELD_WIDTH);
        yPosField.setColumns(FIELD_WIDTH);

        // Set the values of the fields
        xPosField.setValue(START_X);
        yPosField.setValue(START_Y);

        cupFullButton  = new JRadioButton("Full");
        cupEmptyButton = new JRadioButton("Empty");

        // Make some buttons
        okButton     = new JButton("OK");
        cancelButton = new JButton("Cancel");

        // Make the buttons and fields smart
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
        cupFullButton.addActionListener(this);
        cupEmptyButton.addActionListener(this);
        xPosField.addPropertyChangeListener("value", this);
        yPosField.addPropertyChangeListener("value", this);

        // Group together into smaller panels
        JPanel positionPanel = new JPanel(new MigLayout());

        positionPanel.add(xPosLabel, "left");
        positionPanel.add(xPosField, "left, wrap");
        positionPanel.add(yPosLabel, "left");
        positionPanel.add(yPosField, "left");
        positionPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Position"));

        JPanel controlPanel = new JPanel(new MigLayout());

        controlPanel.add(okButton, "right");
        controlPanel.add(cancelButton, "right");

        JPanel typePanel = new JPanel(new MigLayout());

        typePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Type"));

        JPanel cupPanel = new JPanel(new MigLayout());

        cupPanel.add(cupFullButton, "left");
        cupPanel.add(cupEmptyButton, "left");
        cupPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Variables"));

        // Group radio buttons so they switch
        ButtonGroup typeGroup = new ButtonGroup();

        ButtonGroup cupGroup = new ButtonGroup();

        cupGroup.add(cupFullButton);
        cupGroup.add(cupEmptyButton);

        // Make initial radio settings
        cupFullButton.setSelected(true);

        // Layout the window
        JPanel windowLayout = new JPanel(new MigLayout("wrap", "grow"));

        windowLayout.add(typePanel, "grow");
        windowLayout.add(cupPanel, "grow");
        windowLayout.add(positionPanel, "grow");
        windowLayout.add(controlPanel, "grow");

        // Add the window to the main panel
        this.add(windowLayout);
        this.setTitle("Inanimate Designer");
        this.pack();
    }

    /**
     * Method description
     *
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("OK")) {
            setX();
            setY();

            boolean addInanimate;

            addInanimate = simulator.addInanimate(new Cup(xPos, yPos, cupFullButton.isSelected()));

            if (addInanimate) {
                this.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "Could not add to simulation. Check location.", "Inane error",
                                              JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getActionCommand().equals("Cancel")) {
            this.setVisible(false);
        } else if (e.getActionCommand().equals("Add Inanimate")) {
            this.setVisible(true);
        } else if (e.getActionCommand().equals("Cup")) {
            setCupPanelEnabled(true);
        } else if (e.getActionCommand().equals("Block")) {
            setCupPanelEnabled(false);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object source = evt.getSource();

        if (source == yPosField) {
            setY();
        } else if (source == xPosField) {
            setX();
        }
    }

    private void setCupPanelEnabled(boolean state) {
        cupFullButton.setEnabled(state);
        cupEmptyButton.setEnabled(state);
    }

    private void setY() {
        yPos = ((Number) yPosField.getValue()).doubleValue();

        if (yPos > simulator.getEnvironment().getHeight()) {
            yPos = simulator.getEnvironment().getHeight();
            yPosField.setValue(yPos);
        }
    }

    private void setX() {
        xPos = ((Number) xPosField.getValue()).doubleValue();

        if (xPos > simulator.getEnvironment().getWidth()) {
            xPos = simulator.getEnvironment().getWidth();
            xPosField.setValue(xPos);
        }
    }
}



