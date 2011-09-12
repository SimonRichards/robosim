package ux.display.infopanels;

import net.miginfocom.swing.MigLayout;

import publishersubscriber.SimulatorPublisher;
import publishersubscriber.SimulatorSubscriber;

import java.io.StringWriter;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneLayout;

/**
 * Class description
 *
 * @author         Enter your name here...
 */
public class ConsoleOutputPanel extends JPanel implements SimulatorSubscriber {
    private static final long UPDATE_PERIOD      = 500;    // In milliseconds.
    private int               noOfTextAreaRows   = 5;
    private long              previousUpdateTime = 0;
    private JScrollPane       scrOutput;
    private JTextArea         textOutput;
    private StringWriter      writer;

    /**
     * Constructs ...
     *
     *
     * @param simulator
     * @param writer
     */
    public ConsoleOutputPanel(SimulatorPublisher simulator, StringWriter writer) {
        this.setLayout(new MigLayout("fill"));
        setBorder(BorderFactory.createTitledBorder("Script Console"));
        this.writer = writer;
        textOutput  = new JTextArea();
        textOutput.setRows(noOfTextAreaRows);
        textOutput.setEditable(false);
        textOutput.setLineWrap(true);
        scrOutput = new JScrollPane(textOutput);
        scrOutput.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrOutput.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.add(scrOutput, "width 90%,height 90%, grow");
        simulator.addSubscriber(this);
    }

    /**
     * sets the number of rows in the text area
     * @param rows the number of rows to be used
     */
    public void setNumberOfRows(int rows) {
        noOfTextAreaRows = rows;
        textOutput.setRows(noOfTextAreaRows);
    }

    /**
     * @return noOfTextAreaRows the number of text area rows
     */
    public int getNumberOfRows() {
        return noOfTextAreaRows;
    }

    /**
     * @inheritdoc
     */
    @Override
    public void update(SimulatorPublisher publisher) {
        long latestUpdateTime = System.currentTimeMillis();

        if (latestUpdateTime > (previousUpdateTime + UPDATE_PERIOD)) {
            previousUpdateTime = latestUpdateTime;
            textOutput.append(writer.toString());
            writer.getBuffer().delete(0, writer.getBuffer().length());
        }
    }

    /**
     * @inheritdoc
     */
    @Override
    public void simulatorResetted(SimulatorPublisher publisher) {}
}



