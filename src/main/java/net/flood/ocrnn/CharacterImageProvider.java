package net.flood.ocrnn;

import java.io.*;

/**
 * Provides training/test images in subsequent order reading from IDX format files. <p>
 * A simply custom IDX decoder is used to parse files.
 * <a href="http://yann.lecun.com/exdb/mnist/">Here</a> you can find more info on how files are parsed
 * in the section 'File formats for the mninst database'</p>
 * @author flood2d
 */
public class CharacterImageProvider implements Closeable {
    private DataInputStream labelDis;
    private DataInputStream imageDis;
    private File imageFile;
    private File labelFile;
    private int numberOfImages;
    private int rows;
    private int cols;
    private int i = 0;

    /**
     * @param imageFile the idx file that contains images data
     * @param labelFile the idx file that contains labels data
     * @throws IOException
     */
    public CharacterImageProvider(File imageFile, File labelFile) throws IOException {
       this.imageFile = imageFile;
       this.labelFile = labelFile;
       reset();
    }

    private DataInputStream createDataInputStream(File file) throws FileNotFoundException {
        return new DataInputStream(new FileInputStream(file));
    }

    /**
     * @return false if {@link #next()} will return null if called.
     */
    public boolean hasNext() {
        return i < numberOfImages;
    }

    /**
     * @return the next image in the set, or null if the set is end.
     * @throws IOException
     */
    public CharacterImage next() throws IOException {
        if(!hasNext()) {
            return null;
        }

        i++;
        byte label = labelDis.readByte();
        int[][] pixels = new int[rows][cols];
        for(int r = 0; r < rows; r++) {
            for(int c = 0; c < cols; c++) {
                byte b = imageDis.readByte();
                pixels[r][c] = b & 0xff;
            }
        }
        return new CharacterImage(String.valueOf(label).charAt(0), pixels);
    }

    /** Reset the image provider by reloading input streams. A call to next()
     * after this will return the first item in the set.
     * @throws IOException
     */
    public void reset() throws IOException {
        if(labelDis != null) {
            try {
                labelDis.close();
            } catch(Exception e) {}
        }
        if(imageDis != null) {
            try {
                imageDis.close();
            } catch(Exception e) {}
        }
        labelDis = createDataInputStream(labelFile);
        imageDis = createDataInputStream(imageFile);

        labelDis.readInt(); //Magic number
        imageDis.readInt(); //Magic number

        numberOfImages = labelDis.readInt();
        imageDis.readInt();

        rows = imageDis.readInt();
        cols = imageDis.readInt();
        i = 0;
    }

    /**
     * @return the amount of images in the set.
     */
    public int getNumberOfImages() {
        return numberOfImages;
    }

    @Override
    public void close() throws IOException {
        imageDis.close();
        labelDis.close();
    }
}
