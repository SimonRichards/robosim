package ux.painters;

import simulation.entities.Robot;

import ux.display.Arrow;
import ux.display.Colors;
import ux.display.MitreArrow;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * Class description
 *
 * @deprecated
 * @author
 */
public class RobotPainterImp implements RobotPainter {

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
        double    arrowX         = robotBounds.getCenterX();
        double    arrowY         = robotBounds.getCenterY();
        final int arrowWidth     = 20;
        final int arrowHeight    = 50;
        Arrow     directionArrow = new MitreArrow(new Point2D.Double(arrowX, arrowY), Math.PI - angleFromNorth,
                                       arrowWidth, arrowHeight, Color.BLUE);
        Arrow velocityArrow = new MitreArrow(new Point2D.Double(arrowX, arrowY), Math.PI - angleFromNorth, arrowWidth,
                                  robotVelocity, Color.ORANGE);

        velocityArrow.draw(graphics2D, shapeTransform);
        directionArrow.draw(graphics2D, shapeTransform);
    }
}



