package utils;

import java.awt.image.BufferedImage;

public class Animation {
    private BufferedImage[] frames;
    private int currentFrame;
    private long lastUpdate;
    private long delay;
    private boolean looping;

    public Animation(BufferedImage[] frames, long delayMs, boolean looping) {
        this.frames = frames;
        this.delay = delayMs * 1_000_000; // Convert to nanoseconds
        this.looping = looping;
        this.currentFrame = 0;
        this.lastUpdate = System.nanoTime();
    }

    public void update() {
        long now = System.nanoTime();
        long elapsed = now - lastUpdate;

        if (elapsed > delay) {
            currentFrame++;
            if (looping) {
                currentFrame %= frames.length;
            } else {
                currentFrame = Math.min(currentFrame, frames.length - 1);
            }
            lastUpdate = now;
        }
    }

    public BufferedImage getCurrentFrame() {
        return frames[currentFrame];
    }

    public void reset() {
        currentFrame = 0;
        lastUpdate = System.nanoTime();
    }
}