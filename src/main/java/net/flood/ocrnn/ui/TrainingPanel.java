package net.flood.ocrnn.ui;

import net.flood.ocrnn.*;
import net.flood.ocrnn.util.FileUtils;
import net.flood.ocrnn.util.MathUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author flood2d
 */
public class TrainingPanel extends JPanel implements CharacterTrainer.TrainListener, CharacterTester.TesterListener {
    private MainFrame frame;
    private CharacterTrainer trainer;
    private CharacterTester tester;
    private File trainingImageFile;
    private File trainingLabelFile;
    private File testImageFile;
    private File testLabelFile;

    public TrainingPanel(MainFrame frame, int width, int height) {
        this.frame = frame;
        setPreferredSize(new Dimension(width, height));
        initComponents();
        initListeners();
        initLayout();
    }

    private void initComponents() {
        loadTrainingLabel = new JLabel("Load training files");
        loadTestLabel = new JLabel("Load test files");
        loadTrainingImageButton = new JButton("Load images");
        loadTrainingLabelButton = new JButton("Load labels");
        loadTestImageButton = new JButton("Load images");
        loadTestLabelButton = new JButton("Load labels");
        loadTrainingFileLabel = new JLabel("");
        loadTestFileLabel = new JLabel("");
        exportModelButton = new JButton("Export model");
        importModelButton = new JButton("Import model");
        trainingTitle = new JLabel("Training state");
        trainingTitle.setFont(trainingTitle.getFont().deriveFont(Const.TITLE_FONT_SIZE));
        trainingProgressBar = new JProgressBar();
        epochLabel = new JLabel("Epoch : 0");
        performanceLabel = new JLabel("Performance : 0%");
        controlButton = new JButton("Start");
        matrixDrawer = new MatrixDrawer();
        matrixDrawer.setPreferredSize(new Dimension(150, 150));
        characterDrawer = new CharacterDrawer();
        characterDrawer.setPreferredSize(new Dimension(150, 150));
        testTitle = new JLabel("Test");
        testTitle.setFont(testTitle.getFont().deriveFont(Const.TITLE_FONT_SIZE));
        backButton = new JButton("Go back");
        controlButton.setEnabled(false);
        importModelButton.setVisible(false);
        exportModelButton.setVisible(false);
        performanceLabel.setVisible(false);
    }

    private void initListeners() {
        backButton.addActionListener(e -> frame.showQueryContent());
        importModelButton.addActionListener(e -> {

        });
        controlButton.addActionListener(e -> {
            loadTrainingImageButton.setEnabled(false);
            loadTrainingLabelButton.setEnabled(false);
            loadTestImageButton.setEnabled(false);
            loadTestLabelButton.setEnabled(false);
            if(trainer == null || !trainer.isStarted()) {
                try {
                    CharacterImageProvider trainProvider = new CharacterImageProvider(trainingImageFile, trainingLabelFile);
                    CharacterImageProvider testProvider = new CharacterImageProvider(testImageFile, testLabelFile);
                    tester = new CharacterTester(testProvider);
                    tester.setListener(this);
                    tester.start();
                    trainer = new CharacterTrainer(trainProvider);
                    trainer.setListener(this);
                    trainer.start();
                    controlButton.setText("Pause");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } else if(trainer.isPaused()) {
                trainer.resume();
                tester.start();
                controlButton.setText("Pause");
            } else {
                trainer.pause();
                tester.stop();
                controlButton.setText("Continue");
            }
        });
        loadTrainingImageButton.addActionListener(e -> {
            File f = FileUtils.chooseFile(TrainingPanel.this, "IDX", Const.IMAGE_FILE_EXT);
            if(f != null) {
                trainingImageFile = f;
                updateFileLabels();
            }
        });
        loadTrainingLabelButton.addActionListener(e -> {
            File f = FileUtils.chooseFile(TrainingPanel.this, "IDX", Const.LABEL_FILE_EXT);
            if(f != null) {
                trainingLabelFile = f;
                updateFileLabels();
            }
        });
        loadTestImageButton.addActionListener(e -> {
            File f = FileUtils.chooseFile(TrainingPanel.this, "IDX", Const.IMAGE_FILE_EXT);
            if(f != null) {
                testImageFile = f;
                updateFileLabels();
            }
        });
        loadTestLabelButton.addActionListener(e -> {
            File f = FileUtils.chooseFile(TrainingPanel.this, "IDX", Const.LABEL_FILE_EXT);
            if(f != null) {
                testLabelFile = f;
                updateFileLabels();
            }
        });
    }

    private void updateFileLabels() {
        StringBuilder builder = new StringBuilder();
        if(trainingImageFile != null) {
            builder.append(trainingImageFile.getName());
        }
        if(trainingLabelFile != null) {
            if(!builder.toString().isEmpty()) {
                builder.append(" , ");
                builder.append(trainingLabelFile.getName());
            }
        }
        loadTrainingFileLabel.setText(builder.toString());
        builder = new StringBuilder();
        if(testImageFile != null) {
            builder.append(testImageFile.getName());
        }
        if(testLabelFile != null) {
            if(!builder.toString().isEmpty()) {
                builder.append(" , ");
                builder.append(testLabelFile.getName());
            }
        }
        loadTestFileLabel.setText(builder.toString());
        controlButton.setEnabled(trainingLabelFile != null && trainingImageFile != null && testImageFile != null && testLabelFile != null);

    }

    private void initLayout() {
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);

        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addGap(20)
            .addGroup(layout.createParallelGroup()
                .addComponent(loadTrainingLabel)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(loadTrainingImageButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGap(10)
                    .addComponent(loadTrainingLabelButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGap(10)
                    .addComponent(loadTrainingFileLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(importModelButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addComponent(loadTestLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(loadTestImageButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGap(10)
                    .addComponent(loadTestLabelButton)
                    .addGap(10)
                    .addComponent(loadTestFileLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(exportModelButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addComponent(trainingTitle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(epochLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(performanceLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(trainingProgressBar)
                    .addGap(10)
                    .addComponent(controlButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addComponent(testTitle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(matrixDrawer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGap(50)
                    .addComponent(characterDrawer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(backButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
            .addGap(20));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGap(20)
                .addComponent(loadTrainingLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(5)
                .addGroup(layout.createParallelGroup()
                    .addComponent(loadTrainingImageButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(loadTrainingLabelButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(loadTrainingFileLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(importModelButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(10)
                .addComponent(loadTestLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(5)
                .addGroup(layout.createParallelGroup()
                    .addComponent(loadTestImageButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(loadTestLabelButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(loadTestFileLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(exportModelButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(30)
                .addComponent(trainingTitle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(10)
                .addComponent(epochLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(5)
                .addComponent(performanceLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(10)
                .addGroup(layout.createParallelGroup()
                    .addComponent(trainingProgressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(controlButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(20)
                .addComponent(testTitle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(10)
                .addGroup(layout.createParallelGroup()
                    .addComponent(matrixDrawer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(characterDrawer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(backButton)))
                .addGap(20));

        layout.linkSize(SwingConstants.VERTICAL, trainingProgressBar, controlButton);
    }

    @Override
    public void onTrainEpochEnd() {
        SwingUtilities.invokeLater(() -> {
            epochLabel.setText("Epoch: " + trainer.getEpoch());
        });
    }

    @Override
    public void onTrainProgress(float progress) {
        SwingUtilities.invokeLater(() -> {
            trainingProgressBar.setMinimum(0);
            trainingProgressBar.setMaximum(100);
            trainingProgressBar.setValue(MathUtils.clamp(0, 100, (int)(progress * 100)));
            trainingProgressBar.setIndeterminate(false);
            trainingProgressBar.setStringPainted(true);
        });
    }

    @Override
    public void onTest(CharacterImage img, char result) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                matrixDrawer.draw(img.pixels);
                characterDrawer.draw(result);
                boolean correct = img.label == result;

                System.out.println(img.label + " " +  result);
                characterDrawer.setColor(new Color(correct ? Const.COLOR_GREEN : Const.COLOR_RED));
            }
        });
    }

    private JFileChooser fileChooser;
    private JLabel loadTrainingLabel;
    private JLabel loadTestLabel;
    private JButton loadTrainingImageButton;
    private JButton loadTrainingLabelButton;
    private JButton loadTestImageButton;
    private JButton loadTestLabelButton;
    private JLabel loadTrainingFileLabel;
    private JLabel loadTestFileLabel;
    private JButton exportModelButton;
    private JButton importModelButton;
    private JLabel trainingTitle;
    private JProgressBar trainingProgressBar;
    private JLabel epochLabel;
    private JLabel performanceLabel;
    private JButton controlButton;
    private MatrixDrawer matrixDrawer;
    private CharacterDrawer characterDrawer;
    private JLabel testTitle;
    private JButton backButton;
}
