package ux.display;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

/**
 * Implementation of the Arrow interface.
 *
 * @author         Enter your name here...
 */
public class MitreArrow implements Arrow {
    private double  angle;
    private Color   arrowColor;
    private Shape   arrowShape;
    private Point2D position;
    private double  targetLength;
    private double  targetWidth;

    /**
     * Constructor
     *
     *
     * @param position Position of the arrow in the simulator window
     * @param angle Angle of offset
     * @param targetWidth Width of the arrow
     * @param targetLength Length of the arrow
     * @param arrowColor Colour of the arrow
     */
    public MitreArrow(Point2D position, double angle, double targetWidth, double targetLength, Color arrowColor) {
        this.position     = position;
        this.angle        = angle;
        this.targetWidth  = targetWidth;
        this.targetLength = targetLength;
        this.arrowColor   = arrowColor;
    }

    /**
     * Draw the arrow in the simulator window.
     *
     *
     * @param graphics2d
     */
    @Override
    public void draw(Graphics2D graphics2d) {
        float       outlineWidth  = 1f;
        BasicStroke outlineStroke = new BasicStroke(outlineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);

        graphics2d.setStroke(outlineStroke);
        graphics2d.setPaint(Color.BLACK);
        graphics2d.draw(arrowShape);
        graphics2d.setPaint(arrowColor);
        graphics2d.fill(arrowShape);
    }

    /**
     * Method description
     *
     *
     * @param graphics2d
     * @param postTransform
     */
    @Override
    public void draw(Graphics2D graphics2d, AffineTransform postTransform) {
        createArrow();
        arrowShape = postTransform.createTransformedShape(arrowShape);
        draw(graphics2d);
    }

    /**
     * Creates a new Arrow of default dimensions.
     */

    /**
     * Creates an arrow
     */
    protected void createArrow() {
        Path2D arrowPath = new Path2D.Double();

        // Create arrow shape at (0,0).
        final int tailWidth     = 2;
        final int tailLength    = 10;
        final int pointerWidth  = 6;
        final int pointerLength = 3;
        final int totalLength   = pointerLength + tailLength;

        arrowPath.moveTo(0, 0);
        arrowPath.lineTo(tailWidth / 2, 0);
        arrowPath.lineTo(tailWidth / 2, tailLength);
        arrowPath.lineTo(pointerWidth / 2, tailLength);
        arrowPath.lineTo(0, totalLength);
        arrowPath.lineTo(-pointerWidth / 2, tailLength);
        arrowPath.lineTo(-tailWidth / 2, tailLength);
        arrowPath.lineTo(-tailWidth / 2, 0);
        arrowPath.closePath();

        // Scale, move and rotate the arrow.
        AffineTransform arrowTransform = new AffineTransform();

        arrowTransform.translate(position.getX(), position.getY());
        arrowTransform.rotate(Math.PI - angle);
        arrowTransform.scale(targetWidth / pointerWidth, targetLength / totalLength);
        arrowShape = arrowTransform.createTransformedShape(arrowPath);
    }
}



