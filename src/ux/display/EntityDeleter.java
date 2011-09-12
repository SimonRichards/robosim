package ux.display;

import simulation.Simulator;

import simulation.geometry.Entity;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Simon
 */
public class EntityDeleter implements SelectionSubscriber, KeyListener {
    private Entity entity;
    private Simulator      sim;

    public EntityDeleter(GraphicalDisplay display, Simulator sim) {
        display.addKeyListener(this);
        display.addSubscriber(this);
        this.sim = sim;
    }

    @Override
    public void setSelected(Entity selectedItem) {
        entity = selectedItem;
    }

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {
        sim.deleteEntity(entity);

        if ((entity != null) && e.isActionKey()) {
            System.out.println("shit happened");
        }
    }
}



