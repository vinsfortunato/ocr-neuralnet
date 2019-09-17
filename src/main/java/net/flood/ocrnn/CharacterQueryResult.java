package net.flood.ocrnn;

/**
 * @author flood2d
 */
public class CharacterQueryResult {
    public double[] confidences;

    public char getCharacter() {
        int index = 0;
        double value = confidences[0];
        for(int i = 1; i < confidences.length; i++) {
            if(confidences[i] > value) {
                index = i;
                value = confidences[i];
            }
        }
        return String.valueOf(index).charAt(0);
    }
}
