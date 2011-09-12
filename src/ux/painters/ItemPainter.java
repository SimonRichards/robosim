package ux.painters;

import simulation.entities.Cup;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 * Interface for ItemPainter
 *
 * @author         Enter your name here...
 */
public interface ItemPainter {

    /**
     * Paints the Item.
     *
     * @param item Item to paint
     * @param shapeTransform AffineTransform with which to alter shapes
     * @param graphics2D Graphics2D instance on which to call shape draw methods
     */
    void paint(Cup item, AffineTransform shapeTransform, Graphics2D graphics2D);
}



