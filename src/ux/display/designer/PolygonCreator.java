package ux.display.designer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;


import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * @author Samuel
 */
public class PolygonCreator extends JPanel implements MouseListener, MouseMotionListener {
    private static final int  GRIDH            = 25;     // size of grid height
    private static final int  GRIDW            = 25;     // size of grid width
    private static final int  PANEL_HEIGHT     = 600;    // size of paint area
    private static final int  PANEL_WIDTH      = 600;    // size of paint area
    private static final long serialVersionUID = 1L;

    // --- BufferedImage to store the underlying saved painting.
    // Will be initialized first time paintComponent is called.
    transient private BufferedImage _bufImage       = null;
    private int                     currentX        = 0;
    private int                     currentY        = 0;
    private int                     nPoints         = 0;
    protected Polygon               shapeOutline    = new Polygon();
    private Color                   foregroundColor = Color.WHITE;
    private Color                   backgroundColor = Color.GRAY;
    private boolean                 editable;
    private LinkedList<Integer>     xPoints;
    private LinkedList<Integer>     yPoints;

    /**
     * Allows drawing of own polygons
     */
    public PolygonCreator() {
        super();
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(backgroundColor);

        // --- Add the mouse listeners.
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        xPoints = new LinkedList<Integer>();
        yPoints = new LinkedList<Integer>();
    }

    /**
     * Draws polygon from buffer.
     */
    public void drawPolygon() {
        Graphics2D g2          = _bufImage.createGraphics();
        int[]      xPointArray = linkedListtointArray(xPoints);
        int[]      yPointArray = linkedListtointArray(yPoints);

        shapeOutline = new Polygon(xPointArray, yPointArray, nPoints);
        g2.setColor(backgroundColor);
        g2.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
        g2.setColor(foregroundColor);
        g2.fillPolygon(shapeOutline);
        drawGrid(g2);

        float       outlineWidth  = 8.0F;
        BasicStroke outlineStroke = new BasicStroke(outlineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);

        g2.setStroke(outlineStroke);
        g2.setColor(Color.BLACK);
        g2.drawPolygon(shapeOutline);
        this.repaint();
    }

    /**
     * @return PANEL_WIDTH
     */
    public int getPanelWidth() {
        return PANEL_WIDTH;
    }

    /**
     * @return PANEL_HEIGHT
     */
    public int getPanelHeight() {
        return PANEL_HEIGHT;
    }

    /**
     * Method allows the draw functionality of the terrain builder only if param is
     * true. Else drawing functionality blocked
     * @param editable True if editable
     */
    public void editable(boolean editable) {
        this.editable = editable;
    }

    /**
     * @return shapeOutline
     */
    public Polygon getShapeOutline() {
        return shapeOutline;
    }

    /**
     * Sets the colours to default
     */
    public void setStandardColor() {
        backgroundColor = Color.WHITE;
        foregroundColor = Color.GRAY;
    }

    /**
     * Sets the colours to reversed default
     */
    public void setInvertedColor() {
        backgroundColor = Color.GRAY;
        foregroundColor = Color.WHITE;
    }

    /**
     * Transform to flip shape correctly (180) for placement in environment.
     * @param outline The shape to flip
     * @return the Corrected Transform.
     */

    /**
     * Translate shape back to the origin
     * @param outline The shape to flip
     * @return the Corrected Transform.
     */
    public AffineTransform getCorrectionTransform(Polygon outline) {
        AffineTransform transform = new AffineTransform();

        transform.scale(1, -1);
        transform.translate(-shapeOutline.getBounds2D().getX(), -(shapeOutline.getBounds2D().getMaxY()));

        return transform;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;    // // downcast to Graphics2D

        // Increase the rendering quality of the display.
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (_bufImage == null) {
            _bufImage = (BufferedImage) this.createImage(PANEL_WIDTH, PANEL_HEIGHT);

            Graphics2D gc = _bufImage.createGraphics();

            clearCanvas(gc);
            drawGrid(gc);
        }

        g2.drawImage(_bufImage, null, 0, 0);    // draw previous shapes

        // If there is a robot outline then display otherwise clear display
        if (nPoints != 0) {
            drawPolygon();
        } else {
            clearCanvas(g2);
            drawGrid(g2);
        }
    }

    /**
     * Resets the x and y points of the current terrain
     * Resets the length array index back to zero
     * Recolors the display to white
     */
    public void resetCanvas() {
        xPoints.clear();
        yPoints.clear();
        nPoints = 0;
    }

    /**
     * Method adds another point to the terrain
     * @param Point nextPoint
     */
    public void addPoint(int x, int y) {
        try {
            xPoints.add(x);
            yPoints.add(y);
            nPoints++;
        } catch (ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "No more sides can be added to the terrain shape");
        }
    }

    /**
     * Method removes the previous point from the polygon shape
     */
    private void removePreviousPoint() {
        try {
            if (nPoints > 1) {
                nPoints--;
                xPoints.removeLast();
                yPoints.removeLast();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "No more points left to remove");
        }
    }

    /**
     * Method clears the canvas image, and resets the fill color to white
     * @param Graphics2D: g2
     */
    private void clearCanvas(Graphics2D g2) {
        _bufImage.flush();
        g2.setColor(backgroundColor);
        g2.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);    // fill in background
    }

    /**
     * Method displays the grid on the panel
     * @author Andrew Nicholas
     * @param Graphics2D
     */
    private void drawGrid(Graphics2D g2) {
        g2.setColor(Color.LIGHT_GRAY);

        // Insert horizontal lines
        for (int i = GRIDW; i < PANEL_WIDTH; i += GRIDW) {
            g2.drawLine(i, 0, i, PANEL_WIDTH);
        }

        // Insert vertical lines
        for (int i = GRIDH; i < PANEL_HEIGHT; i += GRIDH) {
            g2.drawLine(0, i, PANEL_HEIGHT, i);
        }
    }

    /**
     * Method overrides mousePressed...
     * @param e MouseEvent to respond to
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            int modx;
            int mody;
            int diffx;
            int diffy;

            if (editable) {
                currentX = e.getX();    // save x coordinate of the click
                modx     = currentX % GRIDW;
                diffx    = GRIDW - modx;

                if (diffx < modx) {
                    currentX += diffx;
                } else {
                    currentX -= modx;
                }

                currentY = e.getY();    // save y coordinate of the click
                mody     = currentY % GRIDH;
                diffy    = GRIDH - mody;

                if (diffy < mody) {
                    currentY += diffy;
                } else {
                    currentY -= mody;
                }

                addPoint(currentX, currentY);
                this.repaint();
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            removePreviousPoint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        /**
         * Method overrides mouseReleased
         * @param mouseEvent: e
         */
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        /**
         * Method overrides mouseClicked
         * @param mouseEvent: e
         */
    }

    @Override
    public void mouseDragged(MouseEvent e) {

        /**
         * Method overrides mouseDragged
         * @param mouseEvent: e
         */
    }

    @Override
    public void mouseEntered(MouseEvent e) {

        /**
         * Method overrides mouseEntered
         * @param MouseEvent: e
         */
    }

    @Override
    public void mouseExited(MouseEvent e) {

        /**
         * Method overrides mouseExited
         * @param MouseEvent: e
         */
    }

    @Override
    public void mouseMoved(MouseEvent e) {

        /**
         * Method overrides mouseMoved
         * @param mouseEvent: e
         */
    }

    /**
     * Converts a LinkedList to a primitive int array.
     * @author Andrew Nicholas
     * @param LinkedList
     * @return int[]
     */
    static private int[] linkedListtointArray(LinkedList list) {
        int   listSize = list.size();
        int[] intArray;

        // If there are elements in the array otherwise return null
        if (listSize > 0) {
            intArray = new int[list.size()];

            Iterator<Integer> it = list.listIterator();

            for (int i = 0; i < listSize; i++) {
                intArray[i] = it.next();    // No downcasting required.
            }
        } else {
            intArray = null;
        }

        return intArray;
    }
}



