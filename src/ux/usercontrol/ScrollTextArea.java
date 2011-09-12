package ux.usercontrol;

import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * <code>ScrollTextArea</code> is simply a scrollable text area. It is useful
 * to uses with {@link javax.swing.JOptionPane} to allow displayed messages
 * to wrap, and not create a very wide dialog.
 *
 * @author Kevin Doran
 * @version 1.0 15.04.2011
 */
public class ScrollTextArea extends JScrollPane {
    private final static int PREFERRED_HEIGHT = 150;
    private final static int PREFERRED_WIDTH  = 300;
    private final int        INSET_SIZE       = 5;

    /**
     * Constructs a <code>ScrollTextArea</code>, setting the area's text to be
     * the given <code>String</code>.
     *
     * @param text  the text the <ScrollTextArea</code> is to display.
     */
    public ScrollTextArea(String text) {
        JTextArea textArea = new JTextArea(text);

        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setMargin(new Insets(INSET_SIZE, INSET_SIZE, INSET_SIZE, INSET_SIZE));
        setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));

        // Assign the text area to the scroll pane.
        getViewport().setView(textArea);
    }
}



