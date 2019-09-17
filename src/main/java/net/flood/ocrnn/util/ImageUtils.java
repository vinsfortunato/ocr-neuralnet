package net.flood.ocrnn.util;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author flood2d
 */
public final class ImageUtils {
    /**
     * Create a copy of the image.
     * @param image the image to copy.
     * @return the copy image.
     */
    public static BufferedImage deepCopy(BufferedImage image) {
        return new BufferedImage(
                image.getColorModel(),
                image.copyData(null),
                image.isAlphaPremultiplied(),
                null);
    }

    /**
     * Fit the source image in the given size while preserving image aspect ratio.
     * The image will be resized if necessary and will be centered in the result image.
     * @param source the source image.
     * @param width the dest image width
     * @param height the dest image height
     * @return the
     */
    public static BufferedImage fitInto(BufferedImage source, int width, int height, int backgroundColor) {
        float sourceAspectRatio = source.getWidth() / source.getHeight();
        float resultAspectRatio = (float) width / height;
        float newSourceWidth, newSourceHeight;

        if(resultAspectRatio > sourceAspectRatio) {
            //Use heights
            newSourceHeight = height;
            newSourceWidth = (newSourceHeight / source.getHeight()) * source.getWidth();
        } else {
            //Use widths
            newSourceWidth = width;
            newSourceHeight = (newSourceWidth / source.getWidth()) * source.getHeight();
        }

        //Scale image preserving aspect ratio
        Image scaledSrc = source.getScaledInstance((int)newSourceWidth, (int)newSourceHeight, Image.SCALE_SMOOTH);

        //Draw scaled image centered into result image.
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = result.createGraphics();
        g2.setColor(new Color(backgroundColor));
        g2.fillRect(0, 0, result.getWidth(), result.getHeight());
        g2.drawImage(scaledSrc, (int)(width / 2 - newSourceWidth / 2), (int)(height / 2 - newSourceHeight / 2), null);
        g2.dispose();
        return result;
    }

    /**
     * Invert the source image colors.
     * @param source
     * @return
     */
    public static BufferedImage invertColor(BufferedImage source) {
        for(int x = 0; x < source.getWidth(); x++) {
            for(int y = 0; y < source.getHeight(); y++) {
                Color pixelColor = new Color(source.getRGB(x, y), true);
                pixelColor = new Color(255 - pixelColor.getRed(),
                                       255 - pixelColor.getGreen(),
                                       255 - pixelColor.getBlue());
                source.setRGB(x, y, pixelColor.getRGB());
            }
        }
        return source;
    }

    /**
     * Calculate the center of masss of the given image and use it to center the image into
     * another image of the given width and height filled with given background color.
     * @param source
     * @param width
     * @param height
     * @param backgroundColor
     * @return the result image.
     */
    public static BufferedImage centerWithMassInto(BufferedImage source, int width, int height, int backgroundColor) {
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = result.createGraphics();
        g2.setColor(new Color(backgroundColor));
        g2.fillRect(0, 0, result.getWidth(), result.getHeight());
        int[] centerOfMass = computeCenterOfMass(source);
        g2.drawImage(source, width / 2 - centerOfMass[0], height / 2 - centerOfMass[1], null);
        g2.dispose();
        return result;
    }

    /**
     * Remove white borders from a gray-scale image.
     * @param source
     * @return the cropped image.
     */
    public static BufferedImage cropWhiteBorder(BufferedImage source) {
        int upperBound = -1, lowerBound = -1, leftBound = -1, rightBound = -1;

        //Check upper bound
        upper: for(int y = 0; y < source.getHeight(); y++) {
            for(int x = 0; x < source.getWidth(); x++) {
                if(source.getRGB(x, y) != Color.white.getRGB()) {
                    upperBound = y;
                    break upper;
                }
            }
        }

        //Check lower bound
        lower: for(int y = source.getHeight() - 1; y >= 0; y--) {
            for(int x = 0; x < source.getWidth(); x++) {
                if(source.getRGB(x, y) != Color.white.getRGB()) {
                    lowerBound = y;
                    break lower;
                }
            }
        }

        //Check left bound
        left: for(int x = 0; x < source.getWidth(); x++) {
            for(int y = 0; y < source.getHeight(); y++) {
                if(source.getRGB(x, y) != Color.white.getRGB()) {
                    leftBound = x;
                    break left;
                }
            }
        }

        //Check right bound
        right: for(int x = source.getWidth() - 1; x >= 0; x--) {
            for(int y = 0; y < source.getHeight(); y++) {
                if(source.getRGB(x, y) != Color.white.getRGB()) {
                    rightBound = x;
                    break right;
                }
            }
        }

        //Crop desired area from source image
        return source.getSubimage(leftBound, upperBound, rightBound - leftBound + 1, lowerBound - upperBound + 1);
    }

    /**
     * Compute image center of mass (Works only for gray-scales images).
     * @param img a gray-scale image.
     * @return the center of mass coordinates relative to the given img upper left corner as
     *         an array where first element is x and second element is y.
     */
    public static int[] computeCenterOfMass(BufferedImage img) {
        long xSum = 0;
        long ySum = 0;
        long num = 0;

        for(int x = 0; x < img.getWidth(); x++) {
             for(int y = 0; y < img.getHeight(); y++) {
                 int weight = new Color(img.getRGB(x, y)).getRed();
                 xSum += x * weight;
                 ySum += y * weight;
                 num += weight;
             }
        }
        return new int[] {(int)((double) xSum / num), (int)((double)ySum / num)};
    }

    /**
     * Convert a raw pixel matrix gray-scale to a buffered image.
     * @param pixels a matrix of values in range 0 to 255.
     * @return the result buffered image.
     */
    public static BufferedImage fromPixelMatrix(int[][] pixels) {
        BufferedImage img = new BufferedImage(pixels.length, pixels[0].length, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < img.getWidth(); i++) {
            for(int j = 0; j < img.getHeight(); j++) {
                int weight = pixels[i][j];
                img.setRGB(j, i, new Color(weight, weight, weight).getRGB());
            }
        }
        img.flush();
        return img;
    }

    /**
     * Convert a buffered image into a raw pixel matrix.
     * @param img the buffered img.
     * @return the result raw matrix with values in range 0 to 255.
     */
    public static int[][] toPixelMatrix(BufferedImage img) {
        int[][] pixels = new int[img.getWidth()][img.getHeight()];
        for(int i = 0; i < img.getWidth(); i++) {
            for(int j = 0; j < img.getHeight(); j++) {
                pixels[i][j] = new Color(img.getRGB(i, j)).getRed();
            }
        }
        return pixels;
    }
}
