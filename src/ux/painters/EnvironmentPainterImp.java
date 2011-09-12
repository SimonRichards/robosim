package ux.painters;

import simulation.geometry.Environment;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

/**
 * Class description
 *
 * @author         Enter your name here...
 */
public class EnvironmentPainterImp implements EnvironmentPainter {

    /**
     * Paints the Environment
     *
     *
     * @param environment The Environment to be painted
     * @param shapeTransform For manipulating Shapes
     * @param graphics2D For drawing Shapes
     */
    @Override
    public void paint(Environment environment, AffineTransform shapeTransform, Graphics2D graphics2D) {

        // Outline the environment with a thick black stroke
        // (type of 'paintbrush/pen').
        float       outlineWidth  = 2f;
        BasicStroke outlineStroke = new BasicStroke(outlineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);

        graphics2D.setStroke(outlineStroke);
        graphics2D.setPaint(Color.BLACK);

        Shape environmentShape = shapeTransform.createTransformedShape(environment);

        graphics2D.draw(environmentShape);

        // Fill the shape with a color.
        graphics2D.setPaint(Color.WHITE);
        graphics2D.fill(environmentShape);
    }
}



