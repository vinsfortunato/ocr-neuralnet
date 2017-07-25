package net.flood.ocrnn.ui;

import net.flood.ocrnn.Const;

import javax.swing.*;
import java.awt.*;

/**
 * @author flood2d
 */
public class CharacterDrawer extends JPanel {
    private Character c = null;
    private Color color;

    public CharacterDrawer() {
        setBackground(Color.WHITE);
        setFont(getFont().deriveFont(150.0f));
    }

    public void draw(char c) {
        this.c = c;
        repaint();
    }

    public void setColor(Color c) {
        this.color = c;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(Const.COLOR_GRAY_DARK));
        g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        if(c != null) {
            FontMetrics metrics = g2.getFontMetrics();
            String str = String.valueOf(c);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setColor(color == null ? Color.BLACK : color);
            g2.drawString(
                    String.valueOf(c),
                    getWidth() / 2 - metrics.stringWidth(str) / 2,
                    getHeight() / 2 - metrics.getHeight() / 2 + metrics.getAscent());
        }
    }
}
