package simulation.geometry;

import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The Environment Class contains the walls and Terrain of the area the robot will move in.
 * It contains methods for determining obstruction with walls and determining coefficients of robot in an area
 *
 * @author Simon, Jermin and Josh
 */
public class Environment extends Entity {
    final private Collection<Terrain> impassableTerrains;
    final private Collection<Terrain> passableTerrains;    // contains terrain within the area
    final private Collection<Terrain> terrains;            // contains terrain within the area

    /**
     * Environment constructor
     *
     */
    public Environment() {
        this(new RigidBody(0, 0, 1000, 1000));
    }

    /**
     * Environment constructor
     * 
     * @param environment Environment to be created
     */
    public Environment(Environment environment) {
        super(environment);
        impassableTerrains = new CopyOnWriteArrayList<Terrain>();
        passableTerrains   = new CopyOnWriteArrayList<Terrain>();
        terrains           = new CopyOnWriteArrayList<Terrain>();

        for (Terrain terrain : environment.terrains) {
            terrains.add(new Terrain(terrain));
        }

        for (Terrain terrain : environment.passableTerrains) {
            passableTerrains.add(new Terrain(terrain));
        }

        for (Terrain terrain : environment.impassableTerrains) {
            impassableTerrains.add(new Terrain(terrain));
        }
    }

    /**
     * Creates a new environment object with the given outline.
     * @param area to be used as the walls.
     */
    public Environment(final RigidBody area) {
        super(area);
        this.passableTerrains   = new CopyOnWriteArrayList<Terrain>();
        this.impassableTerrains = new CopyOnWriteArrayList<Terrain>();
        this.terrains           = new CopyOnWriteArrayList<Terrain>();
    }

    /**
     * Adds a new Terrain to the environment.
     * @param shape of the new Terrain.
     * @param coefficient of the new Terrain.
     * @return true if an passable terrain is successfully created.
     */
    public boolean createNewPassableTerrain(final Entity shape, final double coefficient) {
        Terrain t = new Terrain(shape, coefficient);

        passableTerrains.add(t);
        terrains.add(t);

        return true;
    }

    /**
     * Adds a new Terrain to the environment.
     * @param shape of the new Terrain.
     * @param coefficient of the new Terrain.
     * @param desc a description of the new terrain.
     * @return true if an passable terrain is successfully created.
     */
    public boolean createNewPassableTerrain(final Entity shape, final double coefficient, String desc) {
        Terrain t = new Terrain(shape, coefficient, desc);

        passableTerrains.add(t);
        terrains.add(t);

        return true;
    }

    /**
     * Adds a new ImpassableTerrain to the environment.
     * @param shape of the new ImpassableTerrain.
     * @return true if an impassable terrain is successfully created. 
     */
    public boolean createNewImpassableTerrain(final Entity shape) {
        Terrain t = new Terrain(shape);

        impassableTerrains.add(t);
        terrains.add(t);

        return true;
    }

    /**
     * Adds a new ImpassableTerrain to the environment with specified description.
     * @param shape of the new ImpassableTerrain.
     * @param desc a description of the terrain.
     * @return true if an impassable terrain is successfully created.
     */
    public boolean createNewImpassableTerrain(final Entity shape, String desc) {
        Terrain t = new Terrain(shape, 0, desc);

        impassableTerrains.add(t);
        terrains.add(t);

        return true;
    }

    /**
     * Sets the shape of the environment.
     *
     * @param newShape the new shape of the environment.
     */
    public void setShape(Entity newShape) {
        super.setShape(newShape);
    }

    /**
     * Get's the origin of the environment, for painting.
     * @return origin coordinates.
     */
    public XPoint getLocation() {
        return new XPoint(0, 0);
    }

    /**
     * Tests an object's position for legality and calculates angle of incidence on collision detection.
     * @param shape polygon representing the moving objects shape.
     * @return True if the object is in an illegal state.
     */
    public Collision obstructs(final RigidBody shape) {
        Collision collision;

        for (Terrain terrain : impassableTerrains) {
            collision = terrain.incidenceAngleCollision(shape);

            if (collision.occurred()) {
                return collision;
            }
        }

        collision = internalIncidenceAngleCollision(shape);

        if (collision.occurred()) {
            return collision;
        }

        return collision;
    }

    /**
     * Tests an object's position for legality and calculates angle of incidence on collision detection.
     * @param shape polygon representing the moving objects shape.
     * @return True if the object is in an illegal state.
     */
    public boolean obstructs(final Entity shape) {
        for (Terrain terrain : impassableTerrains) {
            if (terrain.intersects(shape)) {
                return true;
            }
        }
        if (!this.contains(shape)) {
            return true;
        }

        return false;
    }

    /**
     * Calculates the coefficient of the ground the shape is on
     * by looking at how much of the vehicle is on the terrain. <p>
     * For example, if half the vehicle is on a terrain with a coefficient of 0.3,
     * the returned value will be (half the vehicle * 0.3) = 0.15. This number is
     * accumulated with all the other surfaces the vehicle is touching.
     * @param shape The shape used to check against the terrain.
     * @return the coefficient of the ground the shape is on.
     */
    public double getCoefficient(final Shape shape) {
        double coefficent             = 0.1;
        double noTerrain              = 0;
        double avrgTerrainCoefficient = 0;

        // create an area of the moving object
        Area movingObject = new Area(shape);

        for (Terrain t : passableTerrains) {

            // creates an area from the current terrain
            Area terrainShape = new Area(t);

            // alter terrainShape to an area of the intersection
            terrainShape.intersect(movingObject);

            // if moving object intersects a terrain
            if (!terrainShape.isEmpty()) {

                // if moving object is entirely on one passable terrain
                final double intersectArea = getShapeArea(terrainShape);
                final double vehicleArea   = getShapeArea(movingObject);

                coefficent += (intersectArea / vehicleArea) * t.getCoefficient();

                // multiply the proportion of the vehicle on the terrain by the terrains coefficient
                noTerrain++;
            }
        }

        if ((coefficent != 0) && (noTerrain != 0)) {
            avrgTerrainCoefficient = coefficent / noTerrain;
            return avrgTerrainCoefficient;
        } else {
            return coefficent;
        }
    }

    /**
     * Creates a big sticky mess around the given point, this is a passable 
     * terrain with a friction co-efficient.
     * @param location Origin of the coffee spill.
     * @param size A value that is proportional to sqrt(area) of the final shape.
     */
    public void spillHotCoffeeEverywhere(XPoint location, final double size) {
        double        x     = location.getX();
        double        y     = location.getY();
        Random        rand  = new Random();
        Path2D.Double spill = new Path2D.Double();
        double        start = x - rand.nextDouble() * size;

        spill.moveTo(start, y);
        spill.quadTo(x, y + rand.nextDouble() * size, x + rand.nextDouble() * size, y);
        spill.quadTo(x, y - rand.nextDouble() * size, start, y);
        createNewPassableTerrain(new RigidBody(spill, location), 0.1, "Big sticky mess of hot coffee.");
    }

    /**
     * Returns an approximation of the area of a given shape by multiplying the
     * width and height of its bounding rectangle.
     * @param shape Shape to get the area of.
     * @return an approximation of the area of the shape.
     */
    private double getShapeArea(final Shape shape) {
        return shape.getBounds2D().getHeight() * shape.getBounds2D().getWidth();
    }

    /**
     * Returns the shapes of all the impassable terrains in the environment.
     * @return Shape[] of the impassable terrains in the environment.
     */
    public Collection<Terrain> getImpassableTerrain() {
        return Collections.unmodifiableCollection(impassableTerrains);
    }

    /**
     * Returns the shapes of all the impassable terrains in the environment.
     * @return Collection of the impassable terrains in the environment.
     */
    public Collection<Terrain> getPassableTerrain() {
        return Collections.unmodifiableCollection(passableTerrains);
    }

    /**
     * @inheritDoc
     *
     * @return The description of the environment.
     */
    @Override
    public String getDescription() {
        return "The environment";
    }

    /**
     * Returns the shape of terrains in the environment.
     * 
     * @return Collection of terrain objects in the environment.
     */
    public Collection<Terrain> getTerrain() {
        return Collections.unmodifiableCollection(terrains);
    }

    /**
     * Gets the width of the environment, calculated using a bounding box.
     *
     * @return The width of the environment
     */
    public double getWidth() {
        return getBounds2D().getWidth();
    }

    /**
     * Gets the height of the environment, calculated using a bounding box.

     * @return The height of the environment
     */
    public double getHeight() {
        return getBounds2D().getHeight();
    }

    /**
     * Clear all terrains in the environment.
     */
    public void clear() {
        terrains.clear();
        impassableTerrains.clear();
        passableTerrains.clear();
    }

    /**
     * Delete a terrain from the environment.
     * 
     * @param deletionEntity Terrain to be deleted from environment.
     */
    public void deleteTerrain(Entity deletionEntity) {
        terrains.remove(deletionEntity);
        passableTerrains.remove(deletionEntity);
        impassableTerrains.remove(deletionEntity);
    }
    /**
     * 
     * @return True if the environment contains no terrain
     */
    public boolean hasNoTerrain(){
        return terrains.isEmpty();
    }
}



