package net.flood.ocrnn.ui;

import net.flood.ocrnn.*;
import net.flood.ocrnn.util.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author flood2d
 */
public class QueryPanel extends JPanel implements CharacterCanvas.DrawListener {
    private MainFrame frame;

    public QueryPanel(MainFrame frame, int width, int height) {
        this.frame = frame;
        setPreferredSize(new Dimension(width, height));
        initComponents();
        initListeners();
        initLayout();
        animTimer.start();
    }

    private void initComponents() {
        charCanvas = new CharacterCanvas();
        charCanvas.setPreferredSize(new Dimension(CHAR_WINDOW_SIZE, CHAR_WINDOW_SIZE));
        charDrawer = new CharacterDrawer();
        charDrawer.setPreferredSize(new Dimension(CHAR_WINDOW_SIZE, CHAR_WINDOW_SIZE));
        clearCanvasButton = new JButton("Clear");
        clearCanvasButton.setPreferredSize(new Dimension(CHAR_WINDOW_SIZE, 30));
        confidenceLabel = new JLabel("Confidence: ?");
        inputTitleLabel = new JLabel("INPUT");
        inputTitleLabel.setFont(inputTitleLabel.getFont().deriveFont(Const.TITLE_FONT_SIZE));
        outputTitleLabel = new JLabel("OUTPUT");
        outputTitleLabel.setFont(outputTitleLabel.getFont().deriveFont(Const.TITLE_FONT_SIZE));
        resutlsTitleLabel = new JLabel("CONFIDENCES");
        resutlsTitleLabel.setFont(resutlsTitleLabel.getFont().deriveFont(Const.TITLE_FONT_SIZE));
        trainButton = new JButton("Train");
        percentageDrawerContainer = new JPanel();
        percentageDrawerContainer.setLayout(new BoxLayout(percentageDrawerContainer, BoxLayout.Y_AXIS));
        for(int c = 0; c < 10; c++) {
            PercentageDrawer drawer = new PercentageDrawer(String.valueOf(c));
            drawer.setDestinationValue(0.01f);
            drawer.setPreferredSize(new Dimension(CHAR_WINDOW_SIZE, 21));
            animTimer.registerAnimation(drawer);
            percentageDrawerContainer.add(drawer);
            percentageDrawerList.add(drawer);
            if(c < 9) {
                percentageDrawerContainer.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }
    }

    private void initListeners() {
        clearCanvasButton.addActionListener(e -> charCanvas.clear());
        trainButton.addActionListener(e -> frame.showTrainingContent());
        charCanvas.setDrawListener(this);
    }

    private void initLayout() {
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGap(20)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(inputTitleLabel)
                                .addComponent(charCanvas, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(clearCanvasButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(20)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(outputTitleLabel)
                                .addComponent(charDrawer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(confidenceLabel))
                        .addGap(20)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(resutlsTitleLabel)
                                .addComponent(percentageDrawerContainer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(trainButton))
                        ).addGap(20));

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGap(20)
                        .addGroup(layout.createParallelGroup()
                                .addComponent(inputTitleLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(outputTitleLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(resutlsTitleLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(20)
                        .addGroup(layout.createParallelGroup()
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(charCanvas, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addGap(20)
                                        .addComponent(clearCanvasButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(charDrawer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addGap(20)
                                        .addComponent(confidenceLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addComponent(percentageDrawerContainer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(trainButton)
                        .addGap(20));
    }

    @Override
    public void onDrawEnd() {
        Task<CharacterQueryResult> task = Main.query(ImageUtils.deepCopy(charCanvas.getImg()));
        task.setListener(result -> {
            try {
                SwingUtilities.invokeAndWait(() -> {
                    for(int i = 0; i < percentageDrawerList.size(); i++) {
                        PercentageDrawer drawer = percentageDrawerList.get(i);
                        drawer.setDestinationValue((float)result.confidences[i]);
                    }
                    charDrawer.draw(result.getCharacter());
                    confidenceLabel.setText("Confidence: " + String.format("%.2f%%",
                            100D * result.confidences[Integer.valueOf(String.valueOf(result.getCharacter()))]));
            });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onDrawStart() {

    }

    private static int CHAR_WINDOW_SIZE = 200;
    private CharacterCanvas charCanvas;
    private CharacterDrawer charDrawer;
    private JButton clearCanvasButton;
    private JLabel confidenceLabel;
    private JLabel inputTitleLabel;
    private JLabel outputTitleLabel;
    private JLabel resutlsTitleLabel;
    private JButton trainButton;
    private List<PercentageDrawer> percentageDrawerList = new ArrayList<>();
    private JPanel percentageDrawerContainer;
    private AnimationTimer animTimer = new AnimationTimer();
}

