package ux.display;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

/**
 * Class description
 *
 * @author         Enter your name here...
 */
public class MitreRect implements Rect {
    private double  angle;
    private Point2D position;
    private Color   rectColor;
    private double  rectPosHeight;
    private Shape   rectShape;
    private double  targetHeight;
    private double  targetWidth;

    /**
     * Constructs ...
     *
     *
     * @param position
     * @param angle
     * @param rectWidth
     * @param rectHeight
     * @param rectColor
     * @param rectPosHeight
     */
    public MitreRect(Point2D position, double angle, double rectWidth, double rectHeight, Color rectColor,
                     double rectPosHeight) {
        this.position      = position;
        this.angle         = angle;
        this.targetWidth   = rectWidth;
        this.targetHeight  = rectHeight;
        this.rectColor     = rectColor;
        this.rectPosHeight = rectPosHeight;
    }

    /**
     * Draws the arrow.
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
        graphics2d.draw(rectShape);
        graphics2d.setPaint(rectColor);
        graphics2d.fill(rectShape);
    }

    /**
     * Draws the transformed arrow.
     *
     *
     * @param graphics2d
     * @param postTransform
     */
    @Override
    public void draw(Graphics2D graphics2d, AffineTransform postTransform) {
        createArrow();
        rectShape = postTransform.createTransformedShape(rectShape);
        draw(graphics2d);
    }

    /**
     * Creates an arrow.
     */

    /**
     * Creates an arrow
     */
    protected void createArrow() {

        // creates the rectangle
        Shape rect = new Rectangle(0, 0, (int) targetWidth, (int) targetHeight);

        // Scale, move and rotate the arrow.
        AffineTransform arrowTransform = new AffineTransform();

        arrowTransform.translate(position.getX(), position.getY());
        arrowTransform.rotate(Math.PI - angle);
        arrowTransform.translate(-targetWidth / 2, rectPosHeight);
        rectShape = arrowTransform.createTransformedShape(rect);
    }
}



