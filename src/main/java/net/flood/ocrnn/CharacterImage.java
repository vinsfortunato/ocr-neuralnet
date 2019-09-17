package net.flood.ocrnn;

/**
 * @author flood2d
 */
public class CharacterImage {
    public final char label;
    public final int[][] pixels;

    public CharacterImage(char label, int[][] pixels) {
        this.label = label;
        this.pixels = pixels;
    }
}
