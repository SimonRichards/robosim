package ux.painters;

import simulation.geometry.Terrain;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

/**
 * Class description
 *
 * @author         Enter your name here...
 */
public class TerrainPainterImp2 implements TerrainPainter {

    /**
     * Method description
     *
     *
     * @param terrain
     * @param shapeTransform
     * @param graphics2D
     */
    @Override
    public void paint(Terrain terrain, AffineTransform shapeTransform, Graphics2D graphics2D) {
        Shape aTerrainShape = shapeTransform.createTransformedShape(terrain);
        Color myColor       = Color.black;

        if (terrain.isPassable()) {
            myColor = Color.getHSBColor((float) terrain.getCoefficient(), 1.0f, 0.7f);
        }

        graphics2D.setPaint(myColor);
        graphics2D.fill(aTerrainShape);
    }
}



