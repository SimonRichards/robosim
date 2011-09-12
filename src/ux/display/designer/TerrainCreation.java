package ux.display.designer;

import simulation.geometry.*;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

/**
 * ShapeDesignerCanvas.java
 * @author Andrew Nicholas, Samuel Frei, Sam Sanson, Josh Jordon
 */
public class TerrainCreation extends PolygonCreator {

    // --- BufferedImage to store the underlying saved painting.
    // Will be initialized first time paintComponent is called.
    private double      coefficent  = 0.5;
    private boolean     passable    = false;
    final private float percentage  = 100f;
    private float       scaleFactor = 10;

    /**
     * Constructs ...
     */
    public TerrainCreation() {
        super();
    }

    /**
     * @return The terrain outline
     */
    public Entity getTerrainOutline() {
        AffineTransform correction = getCorrectionTransform(shapeOutline);
        Entity  result     = new Entity(shapeOutline);

        result.transform(correction);
        scaleFactor(result);

        return result;
    }

    public void setTerrainOutline(Polygon shape) {
        shapeOutline = shape;
    }

    /*
     * This method changes the size of the enviroment to that defined by the user.
     * @param the terrain to be changed
     * @return retunrs the 'fixed' terrain
     */
    public void scaleFactor(Entity shape) {
        AffineTransform correction = new AffineTransform();

        correction.scale(scaleFactor, scaleFactor);
        shape.transform(correction);
    }

    public void setScaleSize(int scaleFactor, Rectangle rect) {
        double maxSize = ((rect.getWidth() > rect.getHeight())
                          ? rect.getWidth()
                          : rect.getWidth());

        this.scaleFactor = (float) scaleFactor / percentage * (float) maxSize / (float) getPanelHeight();
    }

    /**
     * sets the current terrain to passable if true, or impassable if false
     * @param passable the passable state of the terrain
     */
    public void setPassable(boolean passable) {
        this.passable = passable;
    }

    /**
     * gets the passable state of the current terrain. True implies that the
     * terrain is passable, and false implies impassable
     * @return booleans: passable
     */
    public boolean getPassable() {
        return this.passable;
    }

    /**
     * Method sets the friction coefficient to the inputted value
     * @param coefficent Value of the friction co-efficent
     */
    public void setCoefficent(double coefficent) {
        this.coefficent = coefficent;
    }

    /**
     * Method gets the friction coefficient of the current terrain
     * @return double: friction coefficient
     */
    public double getCoefficent() {
        return this.coefficent;
    }
}



