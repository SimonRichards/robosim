package ux.display.designer;

import net.miginfocom.swing.MigLayout;

import simulation.Simulator;

import simulation.geometry.XPoint;

import simulation.entities.Robot;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.io.File;
import java.io.IOException;

import java.text.NumberFormat;

import javax.script.ScriptException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Simon
 */
public class RobotCreationDialog extends JFrame implements ActionListener, PropertyChangeListener {
    private static final int  FIELD_WIDTH      = 10;
    private static final int  MAX_HEIGHT       = 300;
    private static final int  MAX_WIDTH        = 300;
    private static final int  MIN_HEIGHT       = 10;
    private static final int  MIN_WIDTH        = 10;
    private static final long serialVersionUID = 1L;
    private double            ROBOT_ANGLE      = 0;
    private double            ROBOT_HEIGHT     = 60;

    // Suggested robot
    private double              ROBOT_WIDTH  = 30;
    private double              START_X      = 100;
    private double              START_Y      = 100;
    private Color               ROBOT_COLOUR = Color.GREEN;
    private double              angle;
    private JFormattedTextField angleField;
    private JButton             brainButton;
    private String              brainPath;
    private JButton             cancelButton;
    private JButton             colourButton;
    private ColourChooserPanel  colourChooser;
    private double              height;
    private JFormattedTextField heightField;
    private JLabel              heightLabel;
    private JTextField          nameField;
    private JButton             okButton;
    private Color               robotColour;
    private String              robotName;
    private Simulator           simulator;
    private double              width;
    private JFormattedTextField widthField;
    private JLabel              widthLabel;

    // Robot parameters
    private double              xPos;
    private JFormattedTextField xPosField;
    private JLabel              xPosLabel;
    private double              yPos;
    private JFormattedTextField yPosField;
    private JLabel              yPosLabel;

    /**
     * Constructs a window to make a robot.
     *
     *
     * @param simulator the simulator to add the robot to
     */
    public RobotCreationDialog(Simulator simulator) {
        this.simulator = simulator;
        colourChooser  = new ColourChooserPanel();

        // Create number fields
        xPosField   = new JFormattedTextField(NumberFormat.getInstance());
        yPosField   = new JFormattedTextField(NumberFormat.getInstance());
        heightField = new JFormattedTextField(NumberFormat.getInstance());
        widthField  = new JFormattedTextField(NumberFormat.getInstance());
        angleField  = new JFormattedTextField(NumberFormat.getInstance());
        nameField   = new JTextField();

        // Create number field labels
        xPosLabel   = new JLabel("x: ");
        yPosLabel   = new JLabel("y: ");
        heightLabel = new JLabel("height: ");
        widthLabel  = new JLabel("width: ");

        // Setup the size of the fields
        xPosField.setColumns(FIELD_WIDTH);
        yPosField.setColumns(FIELD_WIDTH);
        heightField.setColumns(FIELD_WIDTH);
        widthField.setColumns(FIELD_WIDTH);
        angleField.setColumns(FIELD_WIDTH);
        nameField.setColumns(FIELD_WIDTH);

        // Set the values of the fields
        xPosField.setValue(START_X);
        yPosField.setValue(START_Y);
        heightField.setValue(ROBOT_HEIGHT);
        widthField.setValue(ROBOT_WIDTH);
        angleField.setValue(ROBOT_ANGLE);
        nameField.setText("My Robot");
        robotColour = ROBOT_COLOUR;

        // Make some buttons
        colourButton = new JButton("Colour");
        okButton     = new JButton("OK");
        cancelButton = new JButton("Cancel");
        brainButton  = new JButton("Brain");

        // Make the buttons and fields smart
        colourButton.addActionListener(this);
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
        brainButton.addActionListener(this);
        xPosField.addPropertyChangeListener("value", this);
        yPosField.addPropertyChangeListener("value", this);
        heightField.addPropertyChangeListener("value", this);
        widthField.addPropertyChangeListener("value", this);
        angleField.addPropertyChangeListener("value", this);
        nameField.addPropertyChangeListener("value", this);

        // Group together into smaller panels
        JPanel positionPanel = new JPanel(new MigLayout());

        positionPanel.add(xPosLabel, "left");
        positionPanel.add(xPosField, "left, wrap");
        positionPanel.add(yPosLabel, "left");
        positionPanel.add(yPosField, "left");
        positionPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Position"));

        JPanel sizePanel = new JPanel(new MigLayout());

        sizePanel.add(heightLabel, "left");
        sizePanel.add(heightField, "left, wrap");
        sizePanel.add(widthLabel, "left");
        sizePanel.add(widthField, "left");
        sizePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Size"));

        JPanel anglePanel = new JPanel(new MigLayout("wrap", "grow"));

        anglePanel.add(angleField, "grow");
        anglePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Angle"));

        JPanel namePanel = new JPanel(new MigLayout("wrap", "grow"));

        namePanel.add(nameField, "grow");
        namePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Name"));

        JPanel colourPanel = new JPanel(new MigLayout());

        colourPanel.add(colourButton, "left");
        colourPanel.add(brainButton, "left");

        JPanel controlPanel = new JPanel(new MigLayout());

        controlPanel.add(okButton, "right");
        controlPanel.add(cancelButton, "right");

        // Layout the window
        JPanel windowLayout = new JPanel(new MigLayout("wrap 2", "grow"));

        windowLayout.add(positionPanel, "grow");
        windowLayout.add(sizePanel, "grow");
        windowLayout.add(anglePanel, "grow");
        windowLayout.add(namePanel, "grow");
        windowLayout.add(colourPanel, "grow");
        windowLayout.add(controlPanel, "left");

        // Add the window to the main panel
        this.add(windowLayout);
        this.setTitle("Robot Designer");
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
            setRobot();

            boolean addRobot = false;

            try {
                addRobot = simulator.addRobot(new Robot(brainPath, robotName, new XPoint(xPos, yPos), angle, width,
                        height, robotColour));

                if (addRobot == false) {
                    JOptionPane.showMessageDialog(this, "Robot could not be added. Check robot position.", "Error",
                                                  JOptionPane.ERROR_MESSAGE);
                    System.out.println(brainPath);
                    System.out.println(robotName);
                    System.out.println(xPos);
                    System.out.println(yPos);
                    System.out.println(angle);
                    System.out.println(width);
                    System.out.println(height);
                } else {
                    this.setVisible(false);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "File error. Check file location.");
            } catch (ScriptException ex) {
                JOptionPane.showMessageDialog(null, "Script error. Check script file syntax.");
            } catch (NullPointerException ex) {
                JOptionPane.showMessageDialog(null, "Please select a brain for the robot.");
            }
        } else if (e.getActionCommand().equals("Cancel")) {
            this.setVisible(false);
        } else if (e.getActionCommand().equals("Colour")) {
            robotColour = colourChooser.getFillColour();
        } else if (e.getActionCommand().equals("Add Robot")) {
            this.setVisible(true);
        } else if (e.getActionCommand().equals("Brain")) {
            loadBrain();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object source = evt.getSource();

        if (source == yPosField) {
            setY();
        } else if (source == xPosField) {
            setX();
        } else if (source == heightField) {
            setHeight();
        } else if (source == widthField) {
            setWidth();
        } else if (source == nameField) {
            setName();
        }
    }

    private void loadBrain() {
        final JFileChooser      fc     = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JRuby Files", "rb");

        fc.setAcceptAllFileFilterUsed(true);
        fc.setFileFilter(filter);

        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            brainPath = fc.getSelectedFile().getAbsolutePath();
        }
    }

    private void setRobot() {
        setX();
        setY();
        setHeight();
        setWidth();
        setAngle();
        setName();
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

    private void setHeight() {
        height = ((Number) heightField.getValue()).doubleValue();

        if (height > MAX_HEIGHT) {
            height = MAX_HEIGHT;
        }

        if (height < MIN_HEIGHT) {
            height = MIN_HEIGHT;
        }

        heightField.setValue(height);
    }

    private void setWidth() {
        width = ((Number) widthField.getValue()).doubleValue();

        if (width > MAX_WIDTH) {
            width = MAX_WIDTH;
        }

        if (width < MIN_WIDTH) {
            width = MIN_WIDTH;
        }

        widthField.setValue(width);
    }

    private void setAngle() {
        angle = Math.toRadians(((Number) angleField.getValue()).doubleValue());
    }

    private void setName() {
        robotName = nameField.getText();
    }
}



