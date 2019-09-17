package net.flood.ocrnn.util;

import java.awt.*;

/**
 * <a href="https://stackoverflow.com/questions/4414673/android-color-between-two-colors-based-on-percentage">
 * Inspired by this</a> and adapted to use Swing framework.
 */
public class ColorUtils {
    private static class Yuv {
        private float y;
        private float u;
        private float v;

        private Yuv(int c) {
            Color color = new Color(c);
            int r = color.getRed();
            int g = color.getGreen();
            int b = color.getBlue();
            this.y = 0.299f * r + 0.587f * g + 0.114f * b;
            this.u = (b - y) * 0.493f;
            this.v = (r - y) * 0.877f;
        }
    }

    public static int getColor(int startColor, int endColor, float percentage) {
        Yuv c0 = new Yuv(startColor);
        Yuv c1 = new Yuv(endColor);
        float y = ave(c0.y, c1.y, percentage);
        float u = ave(c0.u, c1.u, percentage);
        float v = ave(c0.v, c1.v, percentage);

        int b = (int) (y + u / 0.493f);
        int r = (int) (y + v / 0.877f);
        int g = (int) (1.7f * y - 0.509f * r - 0.194f * b);

        return new Color(
                MathUtils.clamp(0, 255, r),
                MathUtils.clamp(0, 255, g),
                MathUtils.clamp(0, 255, b)).getRGB();
    }

    private static float ave(float src, float dst, float p) {
        return src + Math.round(p * (dst - src));
    }
}