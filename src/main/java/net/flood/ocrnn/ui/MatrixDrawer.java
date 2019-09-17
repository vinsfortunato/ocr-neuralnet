package net.flood.ocrnn.ui;

import net.flood.ocrnn.Const;

import javax.swing.*;
import java.awt.*;

/**
 * @author flood2d
 */
public class MatrixDrawer extends JPanel {
    private int[][] matrix;

    public MatrixDrawer() {
        setPreferredSize(new Dimension(400, 400));
        setBackground(Color.WHITE);
    }

    public void draw(int[][] matrix) {
        this.matrix = matrix;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if(matrix != null) {
            int pixelSize = getPixelSize();
            int pixelColor;
            for(int r = 0; r < matrix.length; r++) {
                for(int c = 0; c < matrix[r].length; c++) {
                    pixelColor = matrix[r][c];
                    g.setColor(new Color(255 - pixelColor, 255 - pixelColor, 255 - pixelColor));
                    g.fillRect(c * pixelSize, r * pixelSize, pixelSize, pixelSize);
                }
            }
        }
        g.setColor(new Color(Const.COLOR_GRAY_DARK));
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }

    private int getPixelSize() {
        return (int)((double)getWidth() / matrix.length);
    }
}
