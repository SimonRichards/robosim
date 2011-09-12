package ux.painters;

import simulation.entities.Robot;

import simulation.sensors.RobotRadar;
import simulation.sensors.Sensor;

import ux.display.Colors;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

public class SensorPainterImp implements SensorPainter {
    @Override
    public void paint(Robot robot, AffineTransform shapeTransform, Graphics2D graphics2D) {
        for (Sensor sensor : robot.getSensors()) {
            Shape sensorRangeShape = shapeTransform.createTransformedShape(sensor.getRange());

            if (sensorRangeShape != null) {
                if (sensor instanceof RobotRadar) {
                    if (((RobotRadar) sensor).isRobot()) {
                        graphics2D.setColor(Color.GREEN);
                    } else {
                        graphics2D.setColor(Colors.FERN);
                    }

                    graphics2D.draw(sensorRangeShape);
                } else {
                    graphics2D.setColor(Colors.AGGRESSIVE_ORANGE);
                    graphics2D.draw(sensorRangeShape);
                }
            }
        }
    }
}



