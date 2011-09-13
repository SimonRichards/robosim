package ux.painters;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import simulation.geometry.RigidBody;

/**
 *
 * @author Simon
 */
public class OutlinePainter {

    public void paint(RigidBody draggingOutline, AffineTransform shapeTransform, Graphics2D graphics2D) {
        Shape aTerrainShape = shapeTransform.createTransformedShape(draggingOutline);
        graphics2D.setPaint(Color.MAGENTA);
        graphics2D.draw(aTerrainShape);
    }
}
