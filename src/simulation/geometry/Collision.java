package simulation.geometry;

/**
 * A Collision object is a simple storage type which can be in one of two
 * states: A non-collision or a collision with the incidence angle stored.
 * @author Simon
 */
public class Collision {
    private final double  angle;
    private final boolean occured;

    /**
     * new Collision object represents a non-collision
     */
    public Collision() {
        occured = false;
        angle   = 0;
    }

    /**
     * new Collision represents a collision that actually occurred with the
     * given angle of incidence
     * @param angle The angle of incidence to store
     */
    public Collision(double angle) {
        this.angle = angle;
        occured    = true;
    }

    /**
     * @return True if a collision occurred, false otherwise
     */
    public boolean occurred() {
        return occured;
    }

    /**
     * If called when occurred() == false then output is meaningless
     * @return The angle of incidence
     */
    public double getAngle() {
        return angle;
    }
}



