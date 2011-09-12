package ux.display.designer;

import net.miginfocom.swing.MigLayout;

import simulation.Simulator;

import simulation.geometry.Environment;

import java.awt.Container;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Simon, Samuel Frei
 */
public class EnvironmentDesignDialog extends JFrame implements ActionListener {
    private final static String       extension = "env";
    private JButton                   cancelButton;
    private JFileChooser              chooser;
    private JButton                   clearButton;
    private JLabel                    envName;
    private TextField                 envSizeField;
    private JSlider                   envSizeSlider;
    private final EnvironmentCreation envirCreator;
    private Environment               environment;
    private JButton                   okButton;
    private JSeparator                seperator;

    /**
     * Constructs ...
     * @param simulator
     */
    public EnvironmentDesignDialog(Simulator simulator) {
        this.environment = simulator.getEnvironment();
        envirCreator     = new EnvironmentCreation();

        // Create slider
        envSizeSlider = new JSlider(1, 20, 19);
        envName       = new JLabel("ENVIRONMENT DESIGN WINDOW SIZE");
        seperator     = new JSeparator();

        // --- Create the buttons
        clearButton  = new JButton("Clear");
        envSizeField = new TextField(8);
        cancelButton = new JButton("Cancel");
        okButton     = new JButton("OK");

        // -- Add ActionListeners
        clearButton.addActionListener(this);
        envSizeField.addActionListener(this);
        envSizeField.setEditable(true);
        envSizeField.setText("current window size is: 1m x 1m");
        envSizeSlider.setValue(1);
        envSizeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider slider = (JSlider) e.getSource();

                if (!slider.getValueIsAdjusting()) {
                    int value = slider.getValue();

                    envSizeField.setText("current window size is:" + Integer.toString(value) + "m x "
                                         + Integer.toString(value) + "m");
                    envirCreator.setScaleSize(value);
                }
            }
        });
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
        envirCreator.editable(true);

        // --- layout the shape buttons
        JPanel canvasBtnPanel = new JPanel(new MigLayout());

        canvasBtnPanel.add(seperator, "wrap, growx");
        canvasBtnPanel.add(envName, "wrap, growx");
        canvasBtnPanel.add(envSizeSlider, "wrap, growx");
        canvasBtnPanel.add(envSizeField, "wrap, growx");
        canvasBtnPanel.add(clearButton, "wrap, growx");

        // --- Layout load/save/ok/cancel buttons
        JPanel controlBtnPanel = new JPanel(new MigLayout());

        controlBtnPanel.add(okButton, "right");
        controlBtnPanel.add(cancelButton, "right");

        // --- Canvas Panel
        JPanel canvasPanel = new JPanel(new MigLayout());

        canvasPanel.add(envirCreator);

        // --- layout the window
        JPanel windowlayout = new JPanel(new MigLayout());

        windowlayout.add(canvasBtnPanel, "cell 0 0, top");
        windowlayout.add(canvasPanel, "cell 1 0");
        windowlayout.add(controlBtnPanel, "cell 1 1, right");

        Container content = this.getContentPane();

        content.add(windowlayout);
        this.setTitle("Environment Designer");
        this.pack();

        // Setup file chooser
        chooser = new JFileChooser();

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Environment", extension);

        chooser.setAcceptAllFileFilterUsed(true);
        chooser.setFileFilter(filter);
    }

    /**
     * Monitor button press and set the appropriate action.
     * @param e
     */

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Clear")) {
            envirCreator.resetCanvas();
        } else if (e.getActionCommand().equals("Design Enviroment")) {
            this.setVisible(true);
            envirCreator.setInvertedColor();
        } else if (e.getActionCommand().equals("OK")) {
            try {
                environment.setShape(envirCreator.getEnvironmentOutline());
            } catch (NullPointerException err) {

                // Environment not set
            }

            this.setVisible(false);
        } else if (e.getActionCommand().equals("Cancel")) {
            this.dispose();
        } else {
            throw new RuntimeException("Unsupported command, most likely to do with text field!!");
        }
    }
}



