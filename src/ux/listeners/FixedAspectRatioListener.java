package ux.listeners;

import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Class description
 *
 * @author         Enter your name here...
 */
public class FixedAspectRatioListener extends ComponentAdapter {

    /**
     * Method description
     *
     *
     * @param event
     */
    @Override
    public void componentResized(ComponentEvent event) {
        Component sourceComponent  = event.getComponent();
        int       width            = sourceComponent.getWidth();
        int       height           = sourceComponent.getHeight();
        int       largestDimension = (width > height)
                                     ? width
                                     : height;

        sourceComponent.setSize(largestDimension, largestDimension);
    }
}



