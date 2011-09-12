package ux.display.infopanels;

import net.miginfocom.swing.MigLayout;

import publishersubscriber.SimulatorPublisher;
import publishersubscriber.SimulatorSubscriber;

import simulation.entities.Robot;

import simulation.sensors.Sensor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class SensorInfoPanel extends JPanel implements SimulatorSubscriber {
    private static final long UPDATE_PERIOD      = 500;    // In milliseconds.
    private long              previousUpdateTime = 0;
    Map<Sensor, SensorLabels> sensorToLabelMap   = new HashMap<Sensor, SensorLabels>();

    /**
     * Creates an info panel for sensors
     * @param simulator The simulator which to publish the information to
     * @param robot The robot which to take the sensor info from.
     */
    public SensorInfoPanel(SimulatorPublisher simulator, Robot robot) {
        simulator.addSubscriber(this);
        setLayout(new MigLayout("wrap 2", "grow", "grow"));
        add(new JLabel("<html><b>Description</b></html>"), "grow");
        add(new JLabel("<html><b>Value</b></html>"), "grow");
        setSensors(robot.getSensors());
    }

    /**
     * Sets the collection of sensors for the panel to display the information of.
     * @param sensors The collection of sensors.
     */
    public void setSensors(Collection<Sensor> sensors) {
        for (Sensor sensor : sensors) {
            if (sensorToLabelMap.containsKey(sensor) == false) {
                SensorLabels sensorLabels = new SensorLabels();

                sensorToLabelMap.put(sensor, sensorLabels);
                add(sensorLabels.description, "grow");
                add(sensorLabels.value, "grow");
            }

            SensorLabels sensorLabel = sensorToLabelMap.get(sensor);
            String       description = sensor.getDescription();

            if (description != null) {
                sensorLabel.description.setText(description);
            }

            String value = sensor.getValue();

            if (value != null) {
                sensorLabel.value.setText(value);
            }

            revalidate();
            repaint();
        }
    }

    @Override
    public void update(SimulatorPublisher publisher) {
        long latestUpdateTime = System.currentTimeMillis();

        if (latestUpdateTime > (previousUpdateTime + UPDATE_PERIOD)) {
            previousUpdateTime = latestUpdateTime;

            for (Sensor sensor : sensorToLabelMap.keySet()) {
                sensorToLabelMap.get(sensor).description.setText(sensor.getDescription());
                sensorToLabelMap.get(sensor).value.setText(sensor.getValue());
            }
        }
    }

    /**
     * @inheritdoc
     */
    @Override
    public void simulatorResetted(SimulatorPublisher publisher) {    // Nothing needs to be done here
    }

    private class SensorLabels {
        public JLabel description = new JLabel("-----");
        public JLabel value       = new JLabel("-----");
    }
}



