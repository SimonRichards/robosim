package ux.painters;

import simulation.entities.Robot;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 * Interface for RobotPainter
 *
 * @author         Enter your name here...
 */
public interface RobotPainter {

    /**
     * Paints the Robot.
     *
     * @param robot Robot to paint
     * @param shapeTransform AffineTransform with which to alter shapes
     * @param graphics2D Graphics2D instance on which to call shape draw methods
     */
    void paint(Robot robot, AffineTransform shapeTransform, Graphics2D graphics2D);
}



