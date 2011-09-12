package ux.display.infopanels;

import net.miginfocom.swing.MigLayout;

import simulation.geometry.Entity;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Class description
 *
 * @author         Enter your name here...
 */
public class ItemInfoPanel extends JPanel {
    protected JLabel descriptionLabel = new JLabel();
    private String   borderTitle;

    /**
     * Constructs ...
     *
     */
    public ItemInfoPanel() {

        // TODO: hey Kev, you might wanna have a look at this
        // sub-classes implicity call super(), which means that
        // they will call createDefaultContents(), except they
        // prob wanna call their own version or something.
        // Smells fishy anyway.
        setLayout(new MigLayout("wrap 2", "[grow]"));
        createDefaultContents();
    }

    /**
     * Method description
     *
     *
     * @param borderTitle
     */
    public void setBorderTitle(String borderTitle) {
        this.borderTitle = borderTitle;
    }

    /**
     * Creates a JLabel with the default contents
     */
    protected void createDefaultContents() {
        removeAll();
        add(new JLabel("Description:"));
        descriptionLabel.setText("-----");
        add(descriptionLabel);
        revalidate();
        repaint();
    }

    /**
     * Method description
     *
     *
     * @param thing
     */
    public void setItem(Entity thing) {
        String description = thing.getDescription();

        if ((description != null) && (description != "")) {
            descriptionLabel.setText(description);
        } else {
            descriptionLabel.setText("-----");
        }
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public JScrollPane getScrollingPanel() {
        JScrollPane scrollPane = new JScrollPane(this);

        scrollPane.setBorder(BorderFactory.createTitledBorder(borderTitle));

        return scrollPane;
    }
}



