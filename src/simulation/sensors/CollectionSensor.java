package simulation.sensors;

import simulation.geometry.Environment;
import simulation.entities.Cup;
import simulation.entities.Robot;
import java.awt.Shape;
import java.util.Collection;

/**
 * Determines the number of cups that have been collected.
 * @author Joshua Jordan
 */
public class CollectionSensor implements Sensor {
    private int        collectionCount;
    private SensorAble robot;

    @Override
    public void analyse(Environment env, Collection<Robot> robots, Collection<Cup> things) {
        collectionCount = robot.getHeldItems().size();
    }

    @Override
    public String getDescription() {
        return "Collection sensor (item held)";
    }

    @Override
    public Shape getRange() {
        return null;
    }

    /**
     *
     * @return the number of items in the collection.
     */
    public int getCollectionCount() {
        return collectionCount;
    }

    @Override
    public String getValue() {
        return String.valueOf(collectionCount);
    }

    @Override
    public void setObject(SensorAble obj) {
        this.robot = obj;
    }
}



