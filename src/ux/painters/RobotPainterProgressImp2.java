
/**
 * RobotPainterProgressImp.Java
 *
 * @author Ben
 */
package ux.painters;

import simulation.entities.Robot;

import ux.display.Colors;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

/**
 * RobotPainterProgressImp.Java
 * a robots that display progress bars.
 *
 * @author Ben
 */
public class RobotPainterProgressImp2 implements RobotPainter {
    public static final float PROGENDHUE   = 0.33333334f;    // Green
    public static final float PROGSTARTHUE = 0.0f;           // Red

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

        /* Arm progress colour display */
        if ((robot.getCollectionProgress() < 0.1) || (robot.getCollectionProgress() > 99.9)) {
            graphics2D.setPaint(Colors.NAVY);
        } else {
            float prog = (float) robot.getCollectionProgress();
            float hue  = PROGSTARTHUE - prog / 100 * (PROGSTARTHUE - PROGENDHUE);

            graphics2D.setPaint(Color.getHSBColor(hue, 1f, 1f));
        }

        graphics2D.fill(transformedShape);

        /*
         * Shape collectionShape = shapeTransform.createTransformedShape(
         *   (robot.getCollectionArea()));
         * graphics2D.setPaint(Colors.AGGRESSIVE_ORANGE);
         * graphics2D.draw(collectionShape);
         */
    }
}



