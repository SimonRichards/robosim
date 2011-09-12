package ux.painters;

import simulation.geometry.Terrain;

import ux.display.Colors;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

/**
 * Class description
 *
 * @author         Enter your name here...
 */
public class TerrainPainterImp implements TerrainPainter {

    /**
     * @inheritdoc
     *
     * @param terrain
     * @param shapeTransform
     * @param graphics2D
     */
    @Override
    public void paint(Terrain terrain, AffineTransform shapeTransform, Graphics2D graphics2D) {
        Shape aTerrainShape = shapeTransform.createTransformedShape(terrain);

        graphics2D.setPaint(Colors.VOMIT);
        graphics2D.fill(aTerrainShape);
    }
}



