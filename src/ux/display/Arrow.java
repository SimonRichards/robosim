package ux.display;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 * Interface for the MitreArrow class.
 *
 * @author         Enter your name here...
 */
public interface Arrow {

    /**
     * Draws the arrow
     *
     *
     * @param graphics2d
     */
    public void draw(Graphics2D graphics2d);

    /**
     * Draws the transformable arrow
     *
     *
     * @param graphics2d
     * @param postTransform
     */
    public void draw(Graphics2D graphics2d, AffineTransform postTransform);
}



