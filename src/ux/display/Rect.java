package ux.display;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 * Interface description
 *
 * @author         Enter your name here...
 */
public interface Rect {

    /**
     * Method description
     *
     *
     * @param graphics2d
     */
    public void draw(Graphics2D graphics2d);

    /**
     * Method description
     *
     *
     * @param graphics2d
     * @param postTransform
     */
    public void draw(Graphics2D graphics2d, AffineTransform postTransform);
}



