package net.flood.ocrnn.ui;

import net.flood.ocrnn.Const;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

/**
 * @author flood2d
 */
public class CharacterCanvas extends JPanel {
    private float pencilSize = 10;
    private BufferedImage img;
    private Graphics2D imgG2;
    private int prevMouseX = -1;
    private int prevMouseY = -1;
    private DrawListener listener = null;

    public CharacterCanvas() {
        setBackground(Color.WHITE);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                prevMouseX = e.getX();
                prevMouseY = e.getY();
                if(listener != null) {
                    listener.onDrawStart();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(listener != null) {
                    listener.onDrawEnd();
                }
            }
        });
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                handleMouseDrag(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {}
        });
    }

    private void handleMouseDrag(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        imgG2.drawLine(prevMouseX, prevMouseY, mouseX, mouseY);
        prevMouseX = mouseX;
        prevMouseY = mouseY;
        repaint();
    }

    public BufferedImage getImg() {
        return img;
    }

    public void setDrawListener(DrawListener listener) {
        this.listener = listener;
    }

    public void clear() {
        imgG2.setColor(Color.WHITE);
        imgG2.fillRect(0, 0, img.getWidth(), img.getHeight());
        imgG2.setColor(Color.BLACK);
        repaint();
    }

    @Override
    public void setPreferredSize(Dimension size) {
        super.setPreferredSize(size);
        img = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        imgG2 = img.createGraphics();
        imgG2.setColor(Color.WHITE);
        imgG2.fillRect(0, 0, size.width, size.height);
        imgG2.setColor(Color.BLACK);
        imgG2.setStroke(new BasicStroke(pencilSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(Const.COLOR_GRAY_DARK));
        g2.drawImage(img, 0, 0, getWidth(), getHeight(), null);
        g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }

    public interface DrawListener {
        void onDrawEnd();
        void onDrawStart();
    }
}
