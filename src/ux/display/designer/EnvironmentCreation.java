package ux.display.designer;

import simulation.geometry.Entity;

import java.awt.geom.AffineTransform;

/**
 * EnvironmentCreation.java
 * @author Andrew Nicholas, Samuel Frei, Sam Sanson, Josh Jordon, Ben Snalam
 */
public class EnvironmentCreation extends PolygonCreator {
    private double scaleFactor = 1;    // default value is 1x1m

    /**
     * Constructs ...
     */
    public EnvironmentCreation() {
        super();
    }

    /**
     * @return The outline of the environment
     */
    public Entity getEnvironmentOutline() {
        AffineTransform correction = getCorrectionTransform(shapeOutline);
        Entity  result     = new Entity(shapeOutline);

        result.transform(correction);
        scaleFactor(result);

        return result;
    }

    /**
     * Method stores the required size of the environment. Is update by the slider
     */
    public void setScaleSize(int scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    /**
     * This method changes the size of the enviroment to that defined by the user.
     * @param the environment to be changed
     * @return retunrs the 'fixed' environment
     */
    public Entity scaleFactor(Entity shape) {
        AffineTransform correction = new AffineTransform();

        /*
         *  multiplication by the scaleFactor changes the size from its default
         *  1m x 1m to anywhere ftom 1x1 to 20x20 depending on the user
         */
        correction.scale(scaleFactor, scaleFactor);
        shape.transform(correction);

        return shape;
    }
}



