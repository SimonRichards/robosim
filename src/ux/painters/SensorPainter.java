package ux.painters;

import simulation.entities.Robot;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 * Interface for SensorPainter
 *
 * @author amd140
 */
public interface SensorPainter {

    /**
     * Paints the Sensor.
     *
     * @param robot Robot on which to paint the Sensor
     * @param shapeTransform AffineTransform with which to alter shapes
     * @param graphics2D Graphics2D instance on which to call shape draw methods
     */
    void paint(Robot robot, AffineTransform shapeTransform, Graphics2D graphics2D);
}



