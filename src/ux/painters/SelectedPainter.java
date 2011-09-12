
/**
 * SelectedPainter.java
 *
 * @author Ben Snalam
 */
package ux.painters;

import simulation.geometry.RigidBody;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 * Interface for SelectedPainter
 *
 *
 * @author Ben Snalam
 */
public interface SelectedPainter {

    /**
     * Paints the selected RigidBody.
     *
     * @param selectedItem RigidBody to paint
     * @param shapeTransform AffineTransform with which to alter shapes
     * @param graphics2D Graphics2D instance on which to call shape draw methods
     */
    void paint(RigidBody selectedItem, AffineTransform shapeTransform, Graphics2D graphics2D);
}



