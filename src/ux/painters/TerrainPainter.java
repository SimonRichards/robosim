package ux.painters;

import simulation.geometry.Terrain;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 * Interface for TerrainPainter
 *
 * @author         Enter your name here...
 */
public interface TerrainPainter {

    /**
     * Paints the Terrain.
     *
     * @param terrain Terrain to paint
     * @param shapeTransform AffineTransform with which to alter shapes
     * @param graphics2D Graphics2D instance on which to call shape draw methods
     */
    void paint(Terrain terrain, AffineTransform shapeTransform, Graphics2D graphics2D);
}



