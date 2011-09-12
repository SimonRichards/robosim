package ux.display.infopanels;

import net.miginfocom.swing.MigLayout;

import publishersubscriber.SimulatorPublisher;

import simulation.geometry.Entity;
import simulation.geometry.Terrain;

import simulation.entities.Cup;

import simulation.entities.Robot;

import ux.display.SelectionPublisher;
import ux.display.SelectionSubscriber;

import java.awt.Dimension;

import javax.swing.JPanel;

/**
 * Class description
 *
 * @author         Enter your name here...
 */
public class InfoPanel extends JPanel implements SelectionSubscriber {
    private final int        PANEL_HEIGHT = 120;
    private final int        PANEL_WIDTH  = 200;
    private ItemInfoPanel    currentPanel;
    private RobotInfoPanel   robotInfo;
    private TerrainInfoPanel terrainInfo;
    private ItemInfoPanel    thingInfo;

    /**
     * Constructs ...
     *
     *
     * @param simulatorPublisher
     * @param selectionPublisher
     */
    public InfoPanel(SimulatorPublisher simulatorPublisher, SelectionPublisher selectionPublisher) {
        selectionPublisher.addSubscriber(this);
        setMinimumSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setLayout(new MigLayout("", "[grow]", "[grow]"));
        robotInfo = new RobotInfoPanel(simulatorPublisher);
        robotInfo.setBorderTitle("Robot Information");
        terrainInfo = new TerrainInfoPanel();
        terrainInfo.setBorderTitle("Terrain Information");
        thingInfo = new ItemInfoPanel();
        thingInfo.setBorderTitle("Item Information");
        switchToInfoPanel(robotInfo);
    }

    /**
     * Method description
     *
     *
     * @param selected
     */
    @Override
    public void setSelected(Entity selected) {
        if (selected instanceof Robot) {
            switchToInfoPanel(robotInfo);
            robotInfo.setRobot((Robot) selected);
        }

        if (selected instanceof Terrain) {
            switchToInfoPanel(terrainInfo);
            terrainInfo.setTerrain((Terrain) selected);
        }

        if (selected instanceof Cup) {
            switchToInfoPanel(thingInfo);
            thingInfo.setItem(selected);
        }

        if (selected == null) {

            // Uncomment this line to make info panel blank when nothing is
            // selected. It looks unapealing, however.
            // switchToInfoPanel(blankInfo);
        }

        revalidate();
    }

    private void switchToInfoPanel(ItemInfoPanel panelToSwitchTo) {
        if (panelToSwitchTo == null) {
            removeAll();
        } else if (currentPanel != panelToSwitchTo) {
            removeAll();
            add(panelToSwitchTo.getScrollingPanel(), "grow");
            currentPanel = panelToSwitchTo;
        }
    }
}



