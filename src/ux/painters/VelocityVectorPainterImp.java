package ux.painters;

import simulation.entities.Robot;

import ux.display.Arrow;
import ux.display.MitreArrow;
import ux.display.MitreRect;
import ux.display.Rect;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * Implementation of the VelocityVectorPainter interface
 *
 * @author Ben, Andrew Dutton
 */
public class VelocityVectorPainterImp implements VelocityVectorPainter {
    private int   maxVelocity                   = 1;    // The fastest Velocity the robot has traveled
    private Color VELOCITY_ARROW_COLOR          = Color.ORANGE;
    private Paint COLLECTION_AREA_OUTLINE_COLOR = Color.LIGHT_GRAY;
    private Paint COLLECTION_AREA_INNER_COLOR   = Color.WHITE;

    @Override

    /**
     * @param robot The Robot on which to paint the VelocityVector
     */
    public void paint(Robot robot, AffineTransform shapeTransform, Graphics2D graphics2D) {

        // Draw the velocity vector.
        double    angleFromNorth = robot.getAngle();
        int       robotVelocity  = (int) robot.getVelocity();
        Rectangle robotBounds    = robot.getBounds();
        double    centerX        = robotBounds.getCenterX();
        double    centerY        = robotBounds.getCenterY();
        int       maxArrowHeight = (int) (2 * robot.getLength());
        int       arrowWidth     = (int) (robot.getWidth() * 0.5);

        // Change the maximum Velocity of the robot (to scale the arrow)
        if (robotVelocity > maxVelocity) {
            maxVelocity = robotVelocity;
        }

        graphics2D.setPaint(COLLECTION_AREA_OUTLINE_COLOR);
        graphics2D.setStroke(createDashedStroke());
        graphics2D.draw(shapeTransform.createTransformedShape(robot.getCollectionArea()));
        graphics2D.setStroke(new BasicStroke());
        graphics2D.setPaint(COLLECTION_AREA_INNER_COLOR);
        graphics2D.fill(shapeTransform.createTransformedShape(new Ellipse2D.Double(robot.getCom().getX() - 5,
                robot.getCom().getY() - 5, 10, 10)));

        // Make the length of the arrow a percentage of its max Velocity
        int arrowHeight = (maxArrowHeight * robotVelocity) / maxVelocity;

        // For the rectangle
        final double border         = 4;
        double       robotPosHeight = (robot.getLength() / 2) / 5 * 4;
        double       width          = robot.getWidth() - border;
        double       height         = robotPosHeight / 5;
        Rect         directionRect  = new MitreRect(new Point2D.Double(centerX, centerY), Math.PI - angleFromNorth,
                                          width, height, robot.getColour(), robotPosHeight);
        Arrow velocityArrow = new MitreArrow(new Point2D.Double(centerX, centerY), Math.PI - angleFromNorth,
                                  arrowWidth, arrowHeight, VELOCITY_ARROW_COLOR);

        directionRect.draw(graphics2D, shapeTransform);
        velocityArrow.draw(graphics2D, shapeTransform);
    }

    private BasicStroke createDashedStroke() {
        float       dashPattern[] = { 6.0f };
        float       miterlimit    = 10.0f;
        BasicStroke dashedStroke  = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, miterlimit,
                                        dashPattern, 0.0f);

        return dashedStroke;
    }
}



