package net.flood.ocrnn.ui;

import net.flood.ocrnn.util.ColorUtils;
import net.flood.ocrnn.Const;

import javax.swing.*;
import java.awt.*;

/**
 * @author flood2d
 */
public class PercentageDrawer extends JPanel implements Animation {
    private String name;
    private float initValue = 0.0f;
    private float destValue = 1.0f;
    private float currValue = 0.0f;
    private long currentTime = 0;

    public PercentageDrawer(String name) {
        this.name = name;
    }

    /**
     * @param value from 0.0 to 1.0
     */
    public void setDestinationValue(float value) {
        this.destValue = value;
        this.initValue = currValue;
        this.currentTime = 0;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        FontMetrics metrics = g2.getFontMetrics();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        String labelText = getLabelText();
        g2.setColor(getForeground());
        g2.drawString(labelText, 0, getHeight() / 2 - metrics.getHeight() / 2 + metrics.getAscent());

        int strWidth = metrics.stringWidth(labelText);
        int barWidth = getWidth() - strWidth - 11;
        int barHeight = 10;
        g2.setColor(new Color(ColorUtils.getColor(Const.COLOR_RED, Const.COLOR_GREEN, currValue)));
        g2.fillRect(strWidth + 10, getHeight() / 2 - barHeight / 2, (int)(barWidth * currValue), barHeight);
    }

    private String getLabelText() {
        StringBuilder builder = new StringBuilder();
        builder.append(name);
        builder.append(" - ");
        String percentage = String.format("%.2f%%", currValue * 100f);
        for(int i = 0; i < 7 - percentage.length(); i++) {
            builder.append(" ");
        }
        builder.append(percentage);
        return builder.toString();
    }

    @Override
    public void animate(long deltaMs) {
        long animDuration = Const.ANIM_DURATION_SHORT;
        if(currentTime < animDuration) {
            currentTime += deltaMs;
            if(currentTime > animDuration) {
                currentTime = animDuration;
            }
            currValue = initValue + ((float) currentTime / animDuration) * (destValue - initValue);
            if(currValue > 1.0f) {
                currValue = 1.0f;
            }
            if(currValue < 0.0f) {
                currValue = 0.0f;
            }
            repaint();
        }
    }
}
