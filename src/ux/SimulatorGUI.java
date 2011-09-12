package ux;

import net.miginfocom.swing.MigLayout;

import simulation.SimulationBuilder;
import simulation.Simulator;

import ux.display.EntityDeleter;
import ux.display.GraphicalDisplay;
import ux.display.GraphicalDisplayController;
import ux.display.designer.DesignControl;
import ux.display.infopanels.InfoPanel;

import ux.listeners.MinSizeComponentListener;

import ux.usercontrol.ButtonControlUI;
import ux.usercontrol.ErrorOutput;
import ux.usercontrol.ScrollTextArea;
import ux.usercontrol.SimulatorControlGui;
import ux.usercontrol.SimulatorController;
import ux.usercontrol.SimulatorController.Mode;

import java.awt.Image;
import java.awt.Point;

import java.net.URL;

import java.util.Collection;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * GUI from which the user controls the simulator. Simulations can be started,
 * paused and reset, with the option of editing a current simulation and
 * restarting it from pause with the alterations.
 *
 */
public class SimulatorGUI implements SimulatorControlGui, ErrorOutput {
    private static final String        ICON_PATH  = "icon.png";
    final int                          STARTING_X = 100;
    final int                          STARTING_Y = 100;
    private SimulatorController        controller;
    private GraphicalDisplay           graphicalDisplay;
    private GraphicalDisplayController graphicalDisplayController;
    private JPanel                     infoPanel;
    private Simulator                  simulator;
    
    private Collection<JFrame>         simulatorFrames = new LinkedList<JFrame>();
    private Collection<JFrame>         editorFrames = new LinkedList<JFrame>();

    /**
     * Constructor for the simulator
     *
     */
    public SimulatorGUI() {
        // Create a simulator and initialise it with a demo setup.
        simulator = new Simulator();
        SimulationBuilder.setUp(simulator);
        
        // Create a simulator controller and add the SimulatorGUI as a
        // controlling gui.
        controller = new SimulatorController(simulator, this);
        controller.addControlGui(this);

        // Create a button control gui with the simulator's controller.
        ButtonControlUI buttonControlGui = new ButtonControlUI(controller);

        // Create a graphical display along with a controller to control the 
        // displays painting filters.
        graphicalDisplay           = new GraphicalDisplay(simulator, controller);
        graphicalDisplayController = new GraphicalDisplayController(graphicalDisplay);
        
        // Create an information display. It will display information on items
        // in the simulation. It will display an item if it is selected in the
        // graphical display.
        infoPanel                  = new InfoPanel(simulator, graphicalDisplay);

        // Create an deleter- this deletes items from the simulation. When an
        // object selected in the graphical display and delete is pressed,
        // the selected item is deleted.
        EntityDeleter deleter = new EntityDeleter(graphicalDisplay, simulator);

        
        
        // Graphical Display frame.
        JFrame graphicalDisplayFrame = createFrame("Environment", graphicalDisplay, new Point(STARTING_X, STARTING_Y));

        // Graphical Display Filters frame.
        JFrame displayFilterFrame = createFrame("DisplayFilter", graphicalDisplayController.getControlGui(),
                                        new Point(20, 20));

        // Simulator Controls frame.
        int    controlsFrameX = STARTING_X;
        int    controlsFrameY = graphicalDisplayFrame.getY() + graphicalDisplayFrame.getHeight();
        JFrame controlsFrame  = createFrame("Controls", buttonControlGui, new Point(controlsFrameX, controlsFrameY));

        // Info frame.
        int    infoFrameX = graphicalDisplayFrame.getX() + graphicalDisplayFrame.getWidth();
        int    infoFrameY = STARTING_Y;
        JFrame infoFrame  = createFrame("Information", infoPanel, new Point(infoFrameX, infoFrameY));


        // Editor set up
        DesignControl designer           = new DesignControl(simulator, controller);
        JFrame        designControlFrame = createFrame("Simulation Designer", designer, new Point(650, 100));

        editorFrames.add(designControlFrame);    //
        editorFrames.add(graphicalDisplayFrame);
        
        // Add the frames to the simulation frame collection.
        simulatorFrames.add(graphicalDisplayFrame);
        simulatorFrames.add(displayFilterFrame);
        simulatorFrames.add(controlsFrame);
        simulatorFrames.add(infoFrame);
        

        for (JFrame frame : simulatorFrames) {
            frame.addKeyListener(deleter);
        }
    }
    
    /**
     * Starts the GUI in simulation mode.
     */
    public void start() {
        controller.setMode(SimulatorController.Mode.SIMULATION);
    }

    /**
     * Creates a frame with default configurations. The frame exits the program
     * on close, has a content pane with a miglayout and has a minimum size. 
     * 
     */ 
    private JFrame createFrame(String title, JPanel content, Point position) {
        JFrame frame = new JFrame(title);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new JPanel(new MigLayout("wrap 1", "grow", "grow")));
        frame.getContentPane().add(content, "grow");
        setFrameIcon(frame);
        frame.addComponentListener(new MinSizeComponentListener());
        frame.pack();
        frame.setMinimumSize(frame.getPreferredSize());
        frame.setLocation(position);

        return frame;
    }

    /**
     * Gives a frame a program icon.
     * 
     * @param frame the frame to give the icon. 
     */
    private void setFrameIcon(JFrame frame) {
        URL iconUrl = SimulatorGUI.class.getResource(ICON_PATH);

        try {
            ImageIcon icon  = new ImageIcon(iconUrl);
            Image     image = icon.getImage();

            frame.setIconImage(image);
        } catch (Exception e) {}
    }

    /**
     * This method is called by the simulator controller and instructs the GUI
     * that the mode of the simulator (edit or simulation) has changed. The
     * GUI chooses which frames to display depending on the new mode of the
     * simulator.
     * 
     * @param mode  the mode the simulator is currently in.
     */
    @Override
    public void setMode(Mode mode) {
        if (mode == SimulatorController.Mode.EDIT) {
            // Hide the simulation frames.
            for (JFrame frame : simulatorFrames) {
                frame.setVisible(false);
            }

            // Show the editing frames.
            for (JFrame frame : editorFrames) {
                frame.setVisible(true);
            }
        }

        if (mode == SimulatorController.Mode.SIMULATION) {
            // Hide the editing frames.
            for (JFrame frame : editorFrames) {
                frame.setVisible(false);
            }

            // Show the simulation frames.
            for (JFrame frame : simulatorFrames) {
                frame.setVisible(true);
            }
        }
    }
    
    /**
     * Displays a error message by showing a dialog box.
     * 
     * @param title         a title explaining the error- it will be the title
     *                      of the dialog box.
     * @param errorMessage  the message to display.
     */
    @Override
    public void displayMessage(String title, String errorMessage) {
        ScrollTextArea errorMessageArea = new ScrollTextArea(errorMessage);

        // Set the frame the error message will appear on top of.
        JPanel rootPanel = graphicalDisplay;

        JOptionPane.showMessageDialog(rootPanel, errorMessageArea, title, JOptionPane.WARNING_MESSAGE);
    }
    
    /**
     * The GUI does not need to change when the simulator resets.
     */
    @Override
    public void setResetted() {    // Nothing needs to be done.
    }

    /**
     * The GUI does not need to change when the simulator starts.
     */
    @Override
    public void setStarted() {     // Nothing needs to be done.
    }

    /**
     * The GUI does not need to change when the simulator pauses.
     */
    @Override
    public void setPaused() {      // Nothing needs to be done.
    }
    
    
//----------------------------------------------------------------------------
    /**
     * Main
     * Creates and starts a SimulationGui.
     * 
     * @param args  no arguments are used.
     */
    public static void main(String[] args) {

        // Set the look and feel to the systems default. If one cannot be found,
        // use the Java look and feel.
        try {

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {

            // Do nothing- allow the default java look and feel to be used.
        }

        // Create and start the gui.
        SimulatorGUI gui = new SimulatorGUI();
        gui.start();
    }
//----------------------------------------------------------------------------
}
