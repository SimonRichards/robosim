package ux.painters;

import simulation.entities.Cup;

import ux.display.Colors;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

/**
 * Class description
 *
 * @author         Enter your name here...
 */
public class ItemPainterImp implements ItemPainter {

    /**
     * Method description
     *
     *
     * @param item
     * @param shapeTransform
     * @param graphics2D
     */
    @Override
    public void paint(Cup item, AffineTransform shapeTransform, Graphics2D graphics2D) {
        Shape thingShape = shapeTransform.createTransformedShape(item);

        graphics2D.setPaint(Colors.PALEGREEN);
        graphics2D.fill(thingShape);
    }
}



