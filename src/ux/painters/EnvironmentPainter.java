package ux.painters;

import simulation.geometry.Environment;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 * Interface for EnvironmentPainter.
 *
 * @author         Enter your name here...
 */
public interface EnvironmentPainter {

    /**
     * Paints the environment.
     *
     * @param environment Environment to paint
     * @param shapeTransform AffineTransform with which to alter shapes
     * @param graphics2D Graphics2D instance on which to call shape draw methods
     */
    void paint(Environment environment, AffineTransform shapeTransform, Graphics2D graphics2D);
}



