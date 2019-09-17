package net.flood.ocrnn.ui;

import javax.swing.*;
import java.awt.*;

/**
 * @author flood2d
 */
public class MainFrame extends JFrame {
    private static final int FRAME_WIDTH = 700;
    private static final int FRAME_HEIGHT = 500;
    private QueryPanel queryPanel;
    private TrainingPanel trainingPanel;

    public MainFrame() {
        setTitle("Digit recognition neural network");
        setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        queryPanel = new QueryPanel(this, FRAME_WIDTH, FRAME_HEIGHT);
        trainingPanel = new TrainingPanel(this, FRAME_WIDTH, FRAME_HEIGHT);
        setContentPane(queryPanel);
        pack();
        setLocationRelativeTo(null);
    }

    protected void showQueryContent() {
        setContentPane(queryPanel);
        revalidate();
        repaint();
    }

    protected void showTrainingContent() {
        setContentPane(trainingPanel);
        revalidate();
        repaint();
    }

    public void open() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }

    public static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
