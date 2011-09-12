package simulation.sensors;

import simulation.geometry.Environment;
import simulation.geometry.Entity;
import simulation.geometry.Terrain;
import simulation.geometry.XPoint;
import simulation.entities.Cup;
import simulation.entities.Robot;
import java.awt.Shape;
import java.util.Collection;

/**
 * Finds the coefficient of the Terrain object at a location.
 *
 * @author Joshua Jordan, Jermin Tiu
 * @WalkedThrough
 * @Deskchecked Ben
 *
 */
public class TerrainSensor extends Entity implements Sensor {
    private XPoint location;
    private double output;

    /**
     * Constructs ...
     *
     */
    public TerrainSensor() {
        super(new Entity(20, 20, 20));
    }

    /**
     * Assumes that location is on a passable terrain, cycles through all terrain
     * and sets the output to the Passable terrains coefficient at <code>location<\code>
     * @param env     The environment to look at
     * @param robots  unused
     * @param things  unused
     */
    @Override
    public void analyse(final Environment env, final Collection<Robot> robots, final Collection<Cup> things) {
        final Collection<Terrain> terrain = env.getPassableTerrain();

        for (Terrain t : terrain) {
            if (t.contains(location.getX(), location.getY())) {
                output = env.getCoefficient(t);

                break;
            }
        }
    }

    /**
     * Returns the sensor's output.
     * @return The coefficient of the passableTerrain this location
     */
    public double getOutput() {
        return output;
    }

    /**
     * @inheritdoc 
     */
    public void setObject(final SensorAble obj) {
        this.location = obj.getCom();
    }

    /**
     * @inheritdoc 
     */
    public String getDescription() {
        return "Terrain sensor (friction coefficient)";
    }

    /**
     * @inheritdoc 
     */
    @Override
    public Shape getRange() {
        return null;
    }

    /**
     * @inheritdoc
     */
    @Override
    public String getValue() {
        return valueFormat.format(output);
    }
}



