package ux.display.designer;

import net.miginfocom.swing.MigLayout;

import simulation.Simulator;

import simulation.geometry.Environment;
import simulation.geometry.Entity;
import simulation.geometry.Terrain;


import java.awt.Container;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Simon
 */
public class TerrainCreationDialog extends JFrame implements ActionListener {
    private final static String   extension = "ter";
    private JButton               cancelButton;
    private final TerrainCreation canvas;
    private JFileChooser          chooser;
    private JButton               clearButton;
    private JSlider               coefficient;
    private TextField             coefficientField;
    private Environment           environment;
    private JButton               fillButton;
    private JLabel                gridName;
    private JSlider               gridSize;
    private TextField             gridSizeField;
    private JRadioButton          impassable;
    private JButton               okButton;
    private JRadioButton          passable;
    private JSeparator            seperator;
    private Entity        terrainOutline;

    /**
     * Constructs ...
     *
     *
     * @param simulator
     */
    public TerrainCreationDialog(Simulator simulator) {
        this.environment = simulator.getEnvironment();
        terrainOutline   = new Entity(0, 0, 0, 0);
        canvas           = new TerrainCreation();

        // Create slider
        gridSize    = new JSlider(1, 100, 99);
        coefficient = new JSlider(10, 90, 90);
        coefficient.setValue(10);
        gridName  = new JLabel("Terrain's Relative Size");
        seperator = new JSeparator();

        // --- Create the buttons
        fillButton       = new JButton("Fill Colour");
        clearButton      = new JButton("Clear");
        gridSizeField    = new TextField(8);
        cancelButton     = new JButton("Cancel");
        okButton         = new JButton("OK");
        impassable       = new JRadioButton("Impassable", true);
        passable         = new JRadioButton("Passable", false);
        coefficientField = new TextField(8);
        canvas.editable(true);

        // -- Add ActionListeners
        fillButton.addActionListener(this);
        clearButton.addActionListener(this);
        gridSizeField.addActionListener(this);
        gridSizeField.setEditable(false);
        gridSizeField.setText("100" + "% of enviroment size");
        gridSize.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider slider = (JSlider) e.getSource();

                if (!slider.getValueIsAdjusting()) {

                    // Get new value
                    int value = slider.getValue();

                    gridSizeField.setText(Integer.toString(value) + "% of enviroment size");
                }
            }
        });
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
        passable.addActionListener(this);
        impassable.addActionListener(this);
        coefficientField.addActionListener(this);
        coefficientField.setEditable(false);
        coefficientField.setText("0.1");
        coefficient.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider slider = (JSlider) e.getSource();

                if (!slider.getValueIsAdjusting()) {

                    // Get new value
                    float value = (float) slider.getValue() / 100;

                    coefficientField.setText(Float.toString(value));
                    canvas.setCoefficent(value);
                }
            }
        });

        // --- layout the shape buttons
        JPanel canvasBtnPanel = new JPanel(new MigLayout());

        canvasBtnPanel.add(seperator, "wrap, growx");
        canvasBtnPanel.add(gridName, "wrap, growx");
        canvasBtnPanel.add(gridSize, "wrap, growx");
        canvasBtnPanel.add(gridSizeField, "wrap, growx");
        canvasBtnPanel.add(clearButton, "wrap, growx");

        // --- Layout load/save/ok/cancel buttons
        JPanel controlBtnPanel = new JPanel(new MigLayout());

        controlBtnPanel.add(okButton, "right");
        controlBtnPanel.add(cancelButton, "right");

        ButtonGroup bgroup = new ButtonGroup();

        bgroup.add(passable);
        bgroup.add(impassable);

        JPanel radioPanel = new JPanel(new MigLayout());

        radioPanel.setLayout(new GridLayout(4, 1));
        radioPanel.add(passable);
        radioPanel.add(impassable);
        radioPanel.add(coefficient);
        radioPanel.add(coefficientField);
        radioPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Terrain Status"));

        // --- Canvas Panel
        JPanel canvasPanel = new JPanel(new MigLayout());

        canvasPanel.add(canvas);

        // --- layout the window
        JPanel windowlayout = new JPanel(new MigLayout());

        windowlayout.add(canvasBtnPanel, "cell 0 0, top");
        windowlayout.add(canvasPanel, "cell 1 0");
        windowlayout.add(controlBtnPanel, "cell 1 1, right");
        windowlayout.add(radioPanel, "cell 0 1, left");

        Container content = this.getContentPane();

        // content.setLayout(new BorderLayout());
        content.add(windowlayout);
        this.setTitle("Shape Designer");
        this.pack();

        // Setup the file chooser
        chooser = new JFileChooser();

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Terrain files", extension);

        chooser.setFileFilter(filter);
    }

    /**
     * Method description
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Clear")) {
            canvas.resetCanvas();
        } else if (e.getActionCommand().equals("Add Terrain")) {
            this.setVisible(true);
            canvas.setStandardColor();
        } else if (e.getActionCommand().equals("OK")) {
            canvas.setScaleSize(gridSize.getValue(), environment.getBounds());

            if (canvas.getPassable()) {
                environment.createNewPassableTerrain(canvas.getTerrainOutline(), canvas.getCoefficent());
            } else {
                environment.createNewImpassableTerrain(canvas.getTerrainOutline());
            }

            this.setVisible(false);
        } else if (e.getActionCommand().equals("Cancel")) {
            this.dispose();
        } else if (e.getActionCommand().equals("Passable")) {
            canvas.setPassable(true);
            coefficient.setEnabled(true);
        } else if (e.getActionCommand().equals("Impassable")) {
            canvas.setPassable(false);
            coefficient.setEnabled(false);
        } else {
            throw new RuntimeException("Unsupported command, most likely to do with text field!!");
        }
    }

    private Terrain buildTerrain() {
        Entity body = new Entity(terrainOutline);
        Terrain        result;

        if (canvas.getPassable()) {
            result = new Terrain(body, canvas.getCoefficent());
        } else {
            result = new Terrain(body);
        }

        return result;
    }
}



