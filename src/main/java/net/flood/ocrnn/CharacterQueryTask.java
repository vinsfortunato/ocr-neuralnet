package net.flood.ocrnn;

import net.flood.ocrnn.util.ImageUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author flood2d
 */
public class CharacterQueryTask extends Task<CharacterQueryResult> {
    private BufferedImage img;

    public CharacterQueryTask(BufferedImage img) {
        this.img = img;
    }

    @Override
    public CharacterQueryResult doWork() {
        BufferedImage normalizedImg = ImageUtils.cropWhiteBorder(img);
        normalizedImg = ImageUtils.invertColor(normalizedImg);
        normalizedImg = ImageUtils.fitInto(normalizedImg, 20, 20, Color.BLACK.getRGB());
        normalizedImg = ImageUtils.centerWithMassInto(normalizedImg, 28, 28, Color.BLACK.getRGB());
        double[] inputs = new double[normalizedImg.getWidth() * normalizedImg.getHeight()];
        int rows = normalizedImg.getWidth();
        int cols = normalizedImg.getHeight();
        for(int r = 0; r < rows; r++) {
            for(int c = 0; c < cols; c++) {
                int pixelValue = new Color(normalizedImg.getRGB(c, r)).getRed();
                inputs[r * cols + c] = pixelValue / 255.0D * 0.99 + 0.01;
            }
        }
        double[] confidences;
        synchronized (Main.network) {
           confidences = Main.network.query(inputs);
        }
        CharacterQueryResult result = new CharacterQueryResult();
        result.confidences = confidences;
        return result;
    }
}
