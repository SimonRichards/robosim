package simulation.geometry;

/**
 *
 * @author Jermin
 * @version 1.1 25/05/11
 *
 */
public class Terrain extends RigidBody {
    private String description = "Terrain";

    /** The coefficient of the passable terrain */
    private double  friction = 0;
    private boolean passable = true;

    /**
     * Creates a new impassable Terrain
     * @param shape the shape of the terrain
     */
    public Terrain(final Entity shape) {
        super(shape, new XPoint(shape.getBounds2D().getCenterX(), shape.getBounds2D().getCenterY()));
        this.description = "Impassable terrain";
        this.passable    = false;
        this.friction    = 0;
    }

    public Terrain(Terrain terrain) {
        super(terrain);

        if (terrain.isPassable()) {
            this.friction    = terrain.friction;
            this.description = terrain.description;
            passable         = true;
        } else {
            this.description = terrain.description;
            passable         = false;
        }
    }

    /**
     * Creates a new Passable Terrain
     * @param shape the shape of the terrain
     * @param friction the coefficient of the passable terrain
     */
    public Terrain(final Entity shape, final double friction) {
        this(shape);
        this.friction    = friction;
        this.passable    = true;
        this.description = "Terrain with a frictional coefficient of " + String.valueOf(friction);
    }

    /**
     * Creates a new Passable Terrain
     * @param shape the shape of the terrain
     * @param friction the coefficient of the passable terrain
     * @param desc The description of the shape
     */
    public Terrain(final Entity shape, final double friction, String desc) {
        this(shape);
        this.friction    = friction;
        this.passable    = true;
        this.description = desc;
    }

    /**
     * @return True if this terrain is passable, false otherwise
     * @inheritDoc
     */
    public boolean isPassable() {
        return passable;
    }

    /**
     * Returns the coefficient of this terrain object
     * @return coefficient of this terrain (double)
     */
    public double getCoefficient() {
        return friction;
    }

    /**
     * @inheritDoc
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets a terrains description.
     * @param desc the new terrain description
     */
    public void setDescription(String desc) {
        description = desc;
    }
}



