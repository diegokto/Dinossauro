package thenameofd.dinossauro;

import android.graphics.Bitmap;

public class Animation {
    private Bitmap[] frames;
    private int currentFrame;
    private long startTime;
    private long delay;
    private boolean playedOnce;
    private boolean loop;

    public Animation() {
        startTime = System.nanoTime();
    }

    public void iniciar() {
        currentFrame = -1;
        playedOnce = false;
    }

    public void setFrames(Bitmap[] frames) {
        this.frames = frames;
    }

    public void setDelay(long d) {
        this.delay = d;
    }

    public void setLoop(boolean b) {
        this.loop = b;
    }

    public void update() {
        long elapsed = (System.nanoTime() - startTime)/1000000;
        if (elapsed > delay) {
            currentFrame++;
            startTime = System.nanoTime();
        }

        if (currentFrame == frames.length) {
            playedOnce = true;

            if (loop)
                currentFrame = 0;
            else
                currentFrame--;
        }
    }

    public Bitmap getImage() {
        if (currentFrame < 0)
            return frames[0];

        return frames[currentFrame];
    }

    public boolean terminou() {
        return playedOnce;
    }

    public boolean animando() {
        return currentFrame > 0 && !playedOnce;
    }
}
