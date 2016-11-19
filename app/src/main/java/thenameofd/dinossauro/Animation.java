package thenameofd.dinossauro;

import android.graphics.Bitmap;

public class Animation {
    private Bitmap[] frames;
    private int currentFrame;
    private int frameCount;
    private int delayFrame;
    private boolean playedOnce;
    private boolean loop;

    public Animation() {
        frameCount = 0;
    }

    public void iniciar() {
        currentFrame = -1;
        playedOnce = false;
        frameCount = 0;
    }

    public void setFrames(Bitmap[] frames) {
        this.frames = frames;
    }

    public void setDelay(int d) {
        this.delayFrame = d;
    }

    public void setLoop(boolean b) {
        this.loop = b;
    }

    public void update() {
        frameCount++;
        if (frameCount > delayFrame) {
            currentFrame++;
            frameCount = 0;
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
