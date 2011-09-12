package ux.display.designer;

import java.awt.event.MouseEvent;
import net.miginfocom.swing.MigLayout;

import simulation.Simulator;

import ux.usercontrol.SimulatorController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * This panel provides the abitily to edit the simulator via the simulator
 * builder tools and to "launch" the simulation.
 *
 * @author Simon
 */
public class DesignControl extends JPanel {
    private JButton             clearButton;
    private SimulatorController controller;
    private JButton             environmentButton;
    private JButton             inanimateButton;
    private JButton             launchButton;
    private JButton             robotButton;
    private Simulator           simulator;
    private JButton             terrainButton;

    /**
     * Creates a new DesignControl
     * @param simulator Simulator to connect to gui
     * @param controller Controller to run gui
     */
    public DesignControl(Simulator simulator, SimulatorController controller) {
        super(new MigLayout("wrap 1", "[grow]"));
        this.simulator  = simulator;
        this.controller = controller;

        ActionListener inanimateCreator    = new InanimateCreationDialog(simulator);
        ActionListener robotCreator        = new RobotCreationDialog(simulator);
        ActionListener terrainCreator      = new TerrainCreationDialog(simulator);
        ActionListener environmentDesigner = new EnvironmentDesignDialog(simulator);

        environmentButton = new JButton("Design Enviroment");
        inanimateButton   = new JButton("Add Inanimate");
        launchButton      = new JButton("Launch");
        terrainButton     = new JButton("Add Terrain");
        robotButton       = new JButton("Add Robot");
        clearButton       = new JButton("Clear All");
        launchButton.addActionListener(new LaunchListener());
        terrainButton.addActionListener(terrainCreator);
        inanimateButton.addActionListener(inanimateCreator);
        robotButton.addActionListener(robotCreator);
        environmentButton.addActionListener(environmentDesigner);
        clearButton.addActionListener(new SimulationClearer());
        launchButton.addActionListener(new DesignerStateListener());
        terrainButton.addActionListener(new DesignerStateListener());
        inanimateButton.addActionListener(new DesignerStateListener());
        robotButton.addActionListener(new DesignerStateListener());
        environmentButton.addActionListener(new DesignerStateListener());
        this.addMouseMotionListener(new DesignerStateListener());
        
        add(environmentButton, "grow");
        
        add(robotButton, "grow");
        add(inanimateButton, "grow");
        add(terrainButton, "grow");
        add(clearButton, "grow");
        add(launchButton, "grow");
        checkDesignerState();
    }

    /**
    *  Monitor "Add" buttons on the Panel. 
    */
    private class DesignerStateListener implements ActionListener, MouseMotionListener {
         /**
         * IF there are terrains, robots or inanimates
         * in the environment pressed the user loses the
         * ability to create a new environment.
         *
         * @param e unused
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            checkDesignerState();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            checkDesignerState();
        }
    }
   /**
    * disables environment design if simulator contains any Item
    */
    private void checkDesignerState(){
        if(simulator.isEmpty()){
            environmentButton.setEnabled(true);
        }
        else{
            environmentButton.setEnabled(false);
        }
    }
    
    

   /**
    *  Monitors a button press on "Launch" button.
    */
    private class LaunchListener implements ActionListener {
         /**
         * Set the simulation mode to SIMULATION.
         * @param e unused
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            controller.setMode(SimulatorController.Mode.SIMULATION);
        }
    }

    /**
    *  Monitors a button press on "Clear" button. Clears the current simulation
    *  setup.
    */
    private class SimulationClearer implements ActionListener {
        /**
         * Empties all elements of the simulator
         * @param e unused
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            simulator.clearAll();
            environmentButton.setEnabled(true);
            
        }
    }
}



