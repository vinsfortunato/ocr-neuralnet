package net.flood.ocrnn;

import net.flood.ocrnn.ui.MainFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Main {
    public static NeuralNetwork network;
    public static MainFrame frame;
    public static ExecutorService queryExecutor;
    public static ExecutorService trainExecutor;
    public static ScheduledExecutorService testExecutor;

    public static void main(String[] args) throws InterruptedException {
        queryExecutor = Executors.newSingleThreadExecutor();
        trainExecutor = Executors.newSingleThreadExecutor();
        testExecutor = Executors.newSingleThreadScheduledExecutor();

        //Initialize a new neaural network with 0.1 learning rate, 784 inputs, 200 hidden nodes and 10 outputs
        network = new NeuralNetwork(0.1, 784, 200, 10);

        //Show the UI
        MainFrame.setSystemLookAndFeel();
        frame = new MainFrame();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                //Dispose active resources
                network.dispose();
                queryExecutor.shutdown();
                trainExecutor.shutdown();
                testExecutor.shutdown();
            }
        });
        frame.open();
    }

    public static CharacterQueryTask query(BufferedImage img) {
        CharacterQueryTask task = new CharacterQueryTask(img);
        queryExecutor.submit(task);
        return task;
    }
}
