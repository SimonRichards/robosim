package ux.display.infopanels;

import simulation.geometry.Terrain;

import javax.swing.JLabel;

/**
 * Class description
 *
 * @author         Enter your name here...
 */
public class TerrainInfoPanel extends ItemInfoPanel {
    private JLabel passibleLabel;

    /**
     * Constructs ...
     *
     */
    public TerrainInfoPanel() {}

    /**
     * @inheritdoc
     */
    @Override
    protected void createDefaultContents() {
        super.createDefaultContents();
        add(new JLabel("Passable:"));
        passibleLabel = new JLabel();
        passibleLabel.setText("-----");
        add(passibleLabel, "grow");
    }

    /**
     * Method description
     *
     *
     * @param terrain
     */
    public void setTerrain(Terrain terrain) {
        super.setItem(terrain);

        // Display whether the terrain is passable.
        passibleLabel.setText("" + terrain.isPassable());
    }
}



