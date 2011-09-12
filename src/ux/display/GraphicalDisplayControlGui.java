package ux.display;

import net.miginfocom.swing.MigLayout;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

/**
 * Defines the layout of the GUI control window for user interaction with the
 * simulator.
 *
 * @author
 */

/**
 * {@code GraphicalDisplayControlGui} is a simple GUI comprised of checkboxes
 * which enable and disable the painting of certain simulator features in a
 * {@link GraphicalDisplay}. The control GUI uses a
 * {@link GraphicalDisplayController} to actualise its commands.
 *
 * @author Kevin Doran
 *
 */
public class GraphicalDisplayControlGui extends JPanel {

    // Create the checkboxes.
    private JCheckBox robotPainter       = new JCheckBox("Robots", true);
    private JCheckBox sensorPainter      = new JCheckBox("Sensors", true);
    private JCheckBox environmentPainter = new JCheckBox("Environment", true);
    private JCheckBox terrainPainter     = new JCheckBox("Terrain", true);
    private JCheckBox selectedPainter    = new JCheckBox("Selections", true);
    private JCheckBox itemPainter        = new JCheckBox("Items", true);

    // The controller to use to control the GraphicalDisplay
    GraphicalDisplayController controller;

    /**
     * The orientation of the control GUI's checkboxes.
     */
    public static enum Orientation { HORIZONTAL, VERTICAL }

    /**
     * Constructor without specification of orientation.
     *
     * @param controller
     */

    /**
     * Creates a Control GUI
     *
     * @param controller    the controller to use to control the graphical
     *                      display.
     */
    public GraphicalDisplayControlGui(GraphicalDisplayController controller) {
        this(controller, Orientation.HORIZONTAL);
    }

    /**
     * Creates a Control GUI with the given orientation and using the given
     * controller.
     *
     * @param controller    the controller to use to control the graphical
     *                      display.
     * @param orientation   the orientation of the checkboxes.
     *
     */
    public GraphicalDisplayControlGui(GraphicalDisplayController controller, Orientation orientation) {
        this.controller = controller;
        setUpGui(orientation);
    }

    /**
     * Defines the layout of the GUI.
     *
     * @param orientation
     */

    /**
     * Configures the gui's layout.
     *
     * @param orientation   the orientation for the checkboxes to be layed out
     *                      in.
     */
    private void setUpGui(Orientation orientation) {
        if (orientation == Orientation.HORIZONTAL) {
            setLayout(new MigLayout("", "grow"));
        }

        if (orientation == Orientation.VERTICAL) {
            setLayout(new MigLayout("wrap 1", "grow"));
        }

        // Wrap the panel in a titled boarder.
        setBorder(BorderFactory.createTitledBorder("Display Filters"));

        // Add the checkboxes to the panel.
        add(robotPainter);
        add(sensorPainter);
        add(environmentPainter);
        add(terrainPainter);
        add(selectedPainter);
        add(itemPainter);

        // Create a listener for the checkboxes, and subscribe the listener to
        // each checkbox.
        CheckBoxListener listener = new CheckBoxListener();

        robotPainter.addItemListener(listener);
        sensorPainter.addItemListener(listener);
        environmentPainter.addItemListener(listener);
        terrainPainter.addItemListener(listener);
        selectedPainter.addItemListener(listener);
        itemPainter.addItemListener(listener);
    }

    /**
     * CheckBoxListener listens to the checkboxes and instructs the controller
     * to enable/disable the display filters. It determines which boxes have
     * been ticked/unticked in the DisplayFilter window and
     * activates/deactivates the appropriate filters.
     *
     *
     */
    private class CheckBoxListener implements ItemListener {

        /**
         * Activates/deactives filters depending on the source and state of the
         * checkbox event.
         *
         * @param event the event caused by the ticking/unticking of a checkbox.
         */
        @Override
        public void itemStateChanged(ItemEvent event) {
            Object  source = event.getSource();
            boolean isOn;

            if (event.getStateChange() == ItemEvent.SELECTED) {
                isOn = true;
            } else {
                isOn = false;
            }

            if (source == robotPainter) {
                controller.turnRobotPainterOn(isOn);
            }

            if (source == sensorPainter) {
                controller.turnSensorPainterOn(isOn);
            }

            if (source == environmentPainter) {
                controller.turnEnvironmentPainterOn(isOn);
            }

            if (source == terrainPainter) {
                controller.turnTerrainPainterOn(isOn);
            }

            if (source == selectedPainter) {
                controller.turnSelectedPainterOn(isOn);
            }

            if (source == itemPainter) {
                controller.turnItemPainterOn(isOn);
            }
        }
    }
}



