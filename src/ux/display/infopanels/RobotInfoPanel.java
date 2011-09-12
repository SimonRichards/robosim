package ux.display.infopanels;

import net.miginfocom.swing.MigLayout;

import publishersubscriber.SimulatorPublisher;
import publishersubscriber.SimulatorSubscriber;

import simulation.entities.Robot;

import java.io.StringWriter;

import java.text.DecimalFormat;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

/**
 * Class description
 *
 * @author         Enter your name here...
 */
public class RobotInfoPanel extends ItemInfoPanel implements SimulatorSubscriber {
    private static final long              UPDATE_PERIOD      = 500;
    private long                           previousUpdateTime = 0;
    private Map<Robot, SensorInfoPanel>    robotAndSensorMap  = new HashMap<Robot, SensorInfoPanel>();
    private JPanel                         sensorInfoPanel    = new JPanel();
    private JPanel                         robotConsolePanel  = new JPanel();
    private Map<Robot, ConsoleOutputPanel> robotAndConsoleMap = new HashMap<Robot, ConsoleOutputPanel>();
    private JLabel                         VelocityLabel;
    private Robot                          currentRobot;
    private boolean                        isRobotValid;
    private SimulatorPublisher             simulatorPublisher;

    /**
     * Create the panel.
     * @param simulatorPublisher
     */
    public RobotInfoPanel(SimulatorPublisher simulatorPublisher) {
        this.simulatorPublisher = simulatorPublisher;
        simulatorPublisher.addSubscriber(this);

        for (Robot robot : simulatorPublisher.getRobots()) {
            addRobot(robot);
        }
    }

    private void addRobot(Robot robot) {
        StringWriter       writer       = robot.getWriter();
        ConsoleOutputPanel consolepanel = new ConsoleOutputPanel(simulatorPublisher, writer);

        robotAndConsoleMap.put(robot, consolepanel);

        SensorInfoPanel sensorPanel = new SensorInfoPanel(simulatorPublisher, robot);

        robotAndSensorMap.put(robot, sensorPanel);
    }

    /**
     * @inheritdoc
     */
    @Override
    protected void createDefaultContents() {
        super.createDefaultContents();
        add(new JLabel("Velocity:"));
        VelocityLabel = new JLabel("-----");
        add(VelocityLabel, "grow");
        add(new JLabel("Sensors"), "split, span, gaptop 10px");
        add(new JSeparator(), "span, growx, gaptop 10px, wrap");
    }

    /**
     * Method description
     *
     *
     * @param currentRobot
     */
    public void setRobot(Robot currentRobot) {

        // This next if statement facilitates the addition of robots
        // mid-simulation. This could be used as the only way to add robots.
        // However, this would result in robots who were added at the start not
        // having all of their script console output recorded, simply because
        // they weren't clicked on immediately.
        if (robotAndConsoleMap.containsKey(currentRobot) == false) {
            addRobot(currentRobot);
        }

        this.currentRobot = currentRobot;
        super.setItem(currentRobot);

        double robotVelocity = currentRobot.getVelocity();

        setVelocityLabel(robotVelocity);
        remove(sensorInfoPanel);
        remove(robotConsolePanel);
        sensorInfoPanel = robotAndSensorMap.get(currentRobot);
        add(sensorInfoPanel, "grow, span, wrap");
        robotConsolePanel = robotAndConsoleMap.get(currentRobot);
        add(robotConsolePanel, "grow, span, grow");
    }

    /**
     * Method description
     *
     *
     * @param publisher
     */
    @Override
    public void update(SimulatorPublisher publisher) {

        // Check that the reference to the current selected robot is still being
        // monitored by the simulator. If the simulator restarts or discards a
        // robot, the selected robot should be desellected. One option is to use
        // collection.contains() to check if the Robot is in the simulator's
        // collection of robots. However, due to the use of wildcard generics
        // and the returning of unmodifiable collections from the simulator,
        // collection.contains() wont work unless hashCode() and equals() are
        // overridden. So, either the below method can be used, or equals() and
        // hashCode() must be overridden. Unless there are more needs to
        // override equals() and hashCode(), the below method should suffice.
        boolean                     wasRobotValid   = isRobotValid;
        boolean                     isRobotValidNow = false;
        Collection<? extends Robot> simulatorRobots = publisher.getRobots();

        for (Robot simulatorRobot : simulatorRobots) {
            if (currentRobot == simulatorRobot) {
                isRobotValidNow = true;
                setVelocityLabel(currentRobot.getVelocity());

                break;
            }
        }

        if (isRobotValidNow == true) {
            isRobotValid = true;
        } else {
            isRobotValid = false;
        }

        if ((isRobotValidNow == false) && (wasRobotValid == true)) {
            createDefaultContents();
        }
    }

    private void setVelocityLabel(double Velocity) {
        long latestUpdateTime = System.currentTimeMillis();

        if (latestUpdateTime > (previousUpdateTime + UPDATE_PERIOD)) {
            previousUpdateTime = latestUpdateTime;

            DecimalFormat velocityFormat = new DecimalFormat("0.00");

            // Maintain number alignment when sign changes, as by default, there is no
            // character for positive values, and '-' sign for negative values.
            velocityFormat.setPositivePrefix(" ");

            String VelocityString = velocityFormat.format(Velocity);

            VelocityLabel.setText(VelocityString);
            sensorInfoPanel.revalidate();
            sensorInfoPanel.repaint();
        }
    }

    /**
     * @inheritdoc
     */
    @Override
    public void simulatorResetted(SimulatorPublisher publisher) {}
}



