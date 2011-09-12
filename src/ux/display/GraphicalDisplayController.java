package ux.display;

import javax.swing.JPanel;

/**
 * (de)activates Painter switches based on arguments given.
 *
 */

/**
 * Graphicical Display Controller
 */
public class GraphicalDisplayController {
    private JPanel           controlGui;
    private GraphicalDisplay graphicalDisplay;

    /**
     * Constructor
     * @param graphicalDisplay
     */

    /**
     * Creates the controller
     * @param graphicalDisplay The graphical display to create a controller for.
     */
    public GraphicalDisplayController(GraphicalDisplay graphicalDisplay) {
        this.graphicalDisplay = graphicalDisplay;
        controlGui            = createControlGui();
    }

    /**
     * Activates paint switch for SensorPainter
     * @param isOn
     */

    /**
     * Turns on the sensor painter
     * @param isOn True to turn on.
     */
    public void turnSensorPainterOn(boolean isOn) {
        graphicalDisplay.setPaintSensorOn(isOn);
    }

    /**
     * Turns on the robot painter
     * @param isOn True to turn on.
     */
    public void turnRobotPainterOn(boolean isOn) {
        graphicalDisplay.setPaintRobotOn(isOn);
    }

    /**
     * Turns on the Environment painter
     * @param isOn True to turn on.
     */
    public void turnEnvironmentPainterOn(boolean isOn) {
        graphicalDisplay.setPaintEnvironmentOn(isOn);
    }

    /**
     * Turns on the Item painter
     * @param isOn True to turn on.
     */
    public void turnItemPainterOn(boolean isOn) {
        graphicalDisplay.setPaintItemOn(isOn);
    }

    /**
     * Turns on the Selected item painter
     * @param isOn True to turn on.
     */
    public void turnSelectedPainterOn(boolean isOn) {
        graphicalDisplay.setPaintSelectedOn(isOn);
    }

    /**
     * Turns on the terrain painter
     * @param isOn True to turn on.
     */
    public void turnTerrainPainterOn(boolean isOn) {
        graphicalDisplay.setPaintTerrainOn(isOn);
    }

    /**
     * ControlGui getter.
     */

    /**
     * @return The control gui
     */
    public JPanel getControlGui() {
        return controlGui;
    }

    /**
     * ControlGui creator.
     * @return
     */

    /**
     * @return the GraphicalDisplayControlGui created
     */
    protected JPanel createControlGui() {
        return new GraphicalDisplayControlGui(this);
    }
}



