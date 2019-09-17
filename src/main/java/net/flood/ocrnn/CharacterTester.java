package net.flood.ocrnn;

import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author flood2d
 */
public class CharacterTester {
    private CharacterImageProvider imageProvider;
    private Future<?> future;
    private TesterListener listener;

    public CharacterTester(CharacterImageProvider imageProvider) {
        this.imageProvider = imageProvider;
    }

    public void setListener(TesterListener listener) {
        this.listener = listener;
    }

    public void start() {
        future = Main.testExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    if(!imageProvider.hasNext()) {
                        imageProvider.reset();
                    }
                    CharacterImage img = imageProvider.next();
                    int rows = img.pixels.length;
                    int cols = img.pixels[0].length;
                    double[] inputs = new double[rows * cols];
                    for(int r = 0; r < rows; r++) {
                        for(int c = 0; c < cols; c++) {
                            inputs[r * cols + c] = img.pixels[r][c] / 255.0D * 0.99 + 0.01;
                        }
                    }

                    double[] confidences;
                    synchronized (Main.network) {
                        confidences = Main.network.query(inputs);
                    }
                    CharacterQueryResult result = new CharacterQueryResult();
                    result.confidences = confidences;
                    if(listener != null) {
                        listener.onTest(img, result.getCharacter());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, Const.TEST_DELAY, Const.TEST_DELAY, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        if(future != null) {
            future.cancel(true);
        }
    }

    public interface TesterListener {
        void onTest(CharacterImage img, char result);
    }
}
