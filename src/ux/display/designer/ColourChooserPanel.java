package ux.display.designer;

/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
import java.awt.*;

import javax.swing.*;
import javax.swing.event.*;

/**
 *
 * @author Andrew
 */
public class ColourChooserPanel extends JPanel implements ChangeListener {
    private Color         fillColour;
    private JColorChooser fillColourChooser;

    /**
     * Constructs ...
     *
     */
    public ColourChooserPanel() {
        fillColourChooser = new JColorChooser(Color.DARK_GRAY);
        fillColourChooser.getSelectionModel().addChangeListener(this);
        fillColourChooser.setBorder(BorderFactory.createTitledBorder("Choose Text Color"));
    }

    /**
     * Sets the colour of this panel <code>ColourChooserPanel</code> once the
     * user has closed it.
     *
     * @param e
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        fillColour = fillColourChooser.getColor();
    }

    /**
     * Opens a <code>JColorChooser</code> dialog and returns the colour the user
     * has set by the dialog.
     *
     * @return Color of this panel
     */
    public Color getFillColour() {
        fillColour = fillColourChooser.showDialog(this, "Choose Fill Colour", fillColour);

        return fillColour;
    }
}



