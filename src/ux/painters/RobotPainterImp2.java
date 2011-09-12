package ux.painters;

import simulation.entities.Robot;

import ux.display.Arrow;
import ux.display.Colors;
import ux.display.MitreArrow;
import ux.display.MitreRect;
import ux.display.Rect;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * Class description
 * @deprecated
 *
 * @author         Enter your name here...
 */
public class RobotPainterImp2 implements RobotPainter {

    /**
     * Method description
     *
     *
     * @param robot
     * @param shapeTransform
     * @param graphics2D
     */
    @Override
    public void paint(Robot robot, AffineTransform shapeTransform, Graphics2D graphics2D) {
        Shape transformedShape = shapeTransform.createTransformedShape((robot));

        graphics2D.setPaint(Colors.NAVY);
        graphics2D.fill(transformedShape);

        /*
         * Shape collectionShape = shapeTransform.createTransformedShape(
         *   (robot.getCollectionArea()));
         * graphics2D.setPaint(Colors.AGGRESSIVE_ORANGE);
         * graphics2D.draw(collectionShape);
         */

        // Draw the velocity vector.
        double    angleFromNorth = robot.getAngle();
        double    robotVelocity  = robot.getVelocity();
        Rectangle robotBounds    = robot.getBounds();
        double    centerX        = robotBounds.getCenterX();
        double    centerY        = robotBounds.getCenterY();
        final int arrowWidth     = 20;

        // For the rectangle
        final double border         = 4;
        double       robotPosHeight = (robot.getLength() / 2) / 5 * 4;
        double       width          = robot.getWidth() - border;
        double       height         = robotPosHeight / 5;
        Rect         directionRect  = new MitreRect(new Point2D.Double(centerX, centerY), Math.PI - angleFromNorth,
                                          width, height, Color.GREEN, robotPosHeight);
        Arrow velocityArrow = new MitreArrow(new Point2D.Double(centerX, centerY), Math.PI - angleFromNorth,
                                  arrowWidth, robotVelocity, Color.ORANGE);

        directionRect.draw(graphics2D, shapeTransform);
        velocityArrow.draw(graphics2D, shapeTransform);
    }
}



