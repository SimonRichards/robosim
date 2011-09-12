
/**
 * RobotPainterProgressImp.Java
 *
 * @author Ben
 */
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
 * RobotPainterProgressImp.Java
 * a robots that display progress bars.
 * @deprecated
 * @author Ben
 */
public class RobotPainterProgressImp implements RobotPainter {
    public static final float PROGENDHUE   = 0.33333334f;    // Green
    public static final float PROGSTARTHUE = 0.0f;           // Red

    @Override
    public void paint(Robot robot, AffineTransform shapeTransform, Graphics2D graphics2D) {
        Shape transformedShape = shapeTransform.createTransformedShape((robot));

        /* Arm progress colour display */
        if ((robot.getCollectionProgress() < 0.1) || (robot.getCollectionProgress() > 99.9)) {
            graphics2D.setPaint(Colors.NAVY);
        } else {
            float prog = (float) robot.getCollectionProgress();
            float hue  = PROGSTARTHUE - prog / 100 * (PROGSTARTHUE - PROGENDHUE);

            graphics2D.setPaint(Color.getHSBColor(hue, 1f, 1f));
        }

        graphics2D.fill(transformedShape);

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



