package simulation.entities;

import simulation.geometry.Collision;
import simulation.geometry.RigidBody;

/**
 * Supplies method for interacting with a cup which extends RigidBody
 * 
 * @author Simon
 */
public class Cup extends RigidBody {

    // public final static double DEFAULT_MASS   = 50;
    public final static double DEFAULT_RADIUS = 10;
    public final static double DEFAULT_SIZE   = 50;
    private boolean            full           = true;
    private boolean            upright        = true;

    /**
     * Cup constructor
     * @param inanimate
     */
    public Cup(Cup inanimate) {
        super(inanimate);
        full        = inanimate.full;
        upright     = inanimate.upright;
    }

    /**
     * A new cup is instantiated at the given coordinate.
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @param full True if the cup is full of delicious coffee
     */
    public Cup(final double x, final double y, final boolean full) {
        super(x, y, DEFAULT_RADIUS);
        upright     = true;
        this.full   = full;
    }

    /**
     * A description of the Cup.
     *
     * @return A string describing the cup.
     */
    public String getDescription() {
        return "A Cup";
    }

    /**
     * @return True if the cup is still upright
     */
    public boolean isUpright() {
        return upright;
    }

    /**
     * Irrevocably knocks over the cup
     * @return True if the cup spilt sticky coffee
     */
    public boolean knockOver() {
        boolean temp = false;

        temp    = full;
        upright = false;
        full    = false;

        return temp;
    }

    /**
     * Rotate the cup
     * 
     * @param angle angle to rotate the cup to
     */
    public void rotateTo(double angle) {
        rotate(-this.getAngle());
        rotate(angle);
    }


    @Override
    public Collision collideWith(final Cup cup, double criticalAngle) {
        Collision collision;
        double xDif = cup.getX() - getX();
        double yDif = cup.getY() - getY();
        
        double angleToNextObject = Math.atan(xDif / yDif);

        if (this.intersects(cup)) {
            collision = new Collision(angleToNextObject);
        } else {
            collision = new Collision();
        }

        return collision;
    }
}



