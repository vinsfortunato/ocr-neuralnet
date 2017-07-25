package net.flood.ocrnn;

import java.util.concurrent.Future;

/**
 * @author flood2d
 */
public class CharacterTrainer {
    private CharacterImageProvider imageProvider;
    private TrainListener listener;
    private boolean started = false;
    private boolean paused = false;
    private int epoch = 0;
    private CharacterTrainTask task;
    private Future<Object> taskFuture;

    public CharacterTrainer(CharacterImageProvider imageProvider) {
        this.imageProvider = imageProvider;
    }

    public void setListener(TrainListener listener) {
        this.listener = listener;
    }

    public void start() {
        started = true;
        task = new CharacterTrainTask();
        taskFuture = Main.trainExecutor.submit(task);
    }

    public void stop() {
        if(taskFuture != null) {
            taskFuture.cancel(true);
        }
    }

    public void pause() {
        if(task == null) {
            throw new IllegalStateException();
        }
        synchronized (task) {
            paused = true;
        }
    }

    public void resume() {
        if(task == null) {
            throw new IllegalStateException();
        }
        synchronized (task) {
            paused = false;
            task.notifyAll();
        }
    }

    public int getEpoch() {
        return epoch;
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isPaused() {
        return paused;
    }

    private class CharacterTrainTask extends Task<Object> {
        @Override
        public Object doWork() {
            try {
                CharacterImage img;

                while(true) {
                    if(Thread.interrupted()) {
                        throw new InterruptedException();
                    }
                    int count = 0;
                    while(imageProvider.hasNext()) {
                        if(Thread.interrupted()) {
                            throw new InterruptedException();
                        }
                        synchronized (CharacterTrainTask.this) {
                            if(paused) {
                                CharacterTrainTask.this.wait();
                            }
                        }
                        img = imageProvider.next();
                        int rows = img.pixels.length;
                        int cols = img.pixels[0].length;
                        double[] inputs = new double[rows * cols];
                        double[] targets = new double[Main.network.getOutputNodes()];
                        for(int r = 0; r < rows; r++) {
                            for(int c = 0; c < cols; c++) {
                                inputs[r * cols + c] = img.pixels[r][c] / 255.0D * 0.99 + 0.01;
                            }
                        }
                        for(int i = 0; i < targets.length; i++) {
                            if(i == Integer.valueOf(String.valueOf(img.label))) {
                                targets[i] = 0.99D;
                            } else {
                                targets[i] = 0.01D;
                            }
                        }
                        synchronized (Main.network) {
                            Main.network.train(inputs, targets);
                        }
                        count++;
                        if(count % 200 == 0) {
                            if(listener != null) {
                                listener.onTrainProgress(count / (float) imageProvider.getNumberOfImages());
                            }
                        }
                    }

                    if(listener != null) {
                        listener.onTrainEpochEnd();
                    }

                    epoch++;
                    imageProvider.reset();
                }
            } catch(Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public interface TrainListener {
        void onTrainEpochEnd();
        void onTrainProgress(float progress);
    }
}
