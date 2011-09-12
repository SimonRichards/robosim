
/**
 * SelectedPainter.java
 *
 * @author Ben Snalam
 */
package ux.painters;

import simulation.geometry.RigidBody;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

/**
 * SelectedPainterImp.java
 * Paints an outline around the selected object.
 *
 * @author Ben Snalam
 */
public class SelectedPainterImp implements SelectedPainter {

    /**
     * Method description
     *
     *
     * @param selectedItem
     * @param shapeTransform
     * @param graphics2D
     */
    @Override
    public void paint(RigidBody selectedItem, AffineTransform shapeTransform, Graphics2D graphics2D) {

        // Outline the environment with a thick yellow stroke
        // (type of 'paintbrush/pen').
        float       outlineWidth  = 3f;
        BasicStroke outlineStroke = new BasicStroke(outlineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);

        graphics2D.setStroke(outlineStroke);
        graphics2D.setPaint(Color.YELLOW);

        Shape SelectedShape = shapeTransform.createTransformedShape(selectedItem);

        graphics2D.draw(SelectedShape);
    }
}



