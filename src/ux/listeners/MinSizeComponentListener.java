package ux.listeners;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * <code>MinSizeComponentListener</code> is a class that, when instantiated
 * and added as a {@link javax.swing.JComponent}'s
 * {@link java.awt.event.ComponentListener}, prevents the component from being
 * smaller than its minimum size. If the component becomes smaller than its
 * minimum size, <code>MinSizeComponentListener</code> resize the component to
 * its minimum size.
 *
 * @author Kevin Doran
 * @version 1.0 15.04.2011
 */
public class MinSizeComponentListener extends ComponentAdapter {

    /**
     * Called when a component resizes.
     *
     * @param event
     */
    @Override
    public void componentResized(ComponentEvent event) {
        Component sourceComponent = event.getComponent();
        Dimension minimumSize     = sourceComponent.getMinimumSize();
        int       minimumWidth    = new Double(minimumSize.getWidth()).intValue();
        int       minimumHeight   = new Double(minimumSize.getHeight()).intValue();
        int       currentWidth    = sourceComponent.getWidth();
        int       currentHeight   = sourceComponent.getHeight();

        if ((currentWidth < minimumWidth) || (currentHeight < minimumHeight)) {
            sourceComponent.setSize(Math.max(minimumWidth, currentWidth), Math.max(minimumHeight, currentHeight));
        }
    }
}



