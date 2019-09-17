package net.flood.ocrnn.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author flood2d
 */
public class AnimationTimer extends Timer implements ActionListener {
    private static final int ANIMATION_DELAY = 30; //millis
    private final List<Animation> animations = new ArrayList<>();

    public AnimationTimer() {
        super(ANIMATION_DELAY, e -> {});
        this.setRepeats(true);
        this.addActionListener(this);
    }

    public void registerAnimation(Animation animation) {
        synchronized (animations) {
            this.animations.add(animation);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        synchronized (animations) {
            for(Animation animation : animations) {
                animation.animate(getDelay());
            }
        }
    }
}
