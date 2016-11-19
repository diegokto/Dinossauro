package thenameofd.dinossauro;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import static java.lang.Math.round;

public class Player {
    private final int distanciaTransparente = 3;

    private int distanciaY = Background.GROUND - distanciaTransparente;
    private int x;
    private int y;
    private int dx;
    private int dy;
    private int width;
    private int height;

    private Bitmap[] images_run;
    private Animation animation_run;

    private Bitmap[] images_jump;
    private Animation animation_jump;

    private Bitmap[] images_down;
    private Animation animation_down;

    private boolean gameOver;
    private boolean jump;
    private boolean down;

    private int groundY;

    private long startTime;
    private long delay;
    private boolean piscar;
    private int frameCount;
    private int frame;

    private int g = 10; //gravidade
    private int margemErroY = 20;
    private int margemErroX = 20;

    private int deviceHeight;

    public Player(GamePanel gamePanel) {
        deviceHeight = gamePanel.getHeight();

        images_run = new Bitmap[8];
        images_run[0] = BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.ace1);
        images_run[1] = BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.ace2);
        images_run[2] = BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.ace3);
        images_run[3] = BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.ace4);
        images_run[4] = BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.ace5);
        images_run[5] = BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.ace6);
        images_run[6] = BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.ace7);
        images_run[7] = BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.ace8);
        resizeImages(images_run);

        images_jump = new Bitmap[9];
        images_jump[0] = BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.ace_jump1);
        images_jump[1] = BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.ace_jump2);
        images_jump[2] = BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.ace_jump3);
        images_jump[3] = BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.ace_jump4);
        images_jump[4] = BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.ace_jump5);
        images_jump[5] = BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.ace_jump6);
        images_jump[6] = BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.ace_jump7);
        images_jump[7] = BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.ace_jump8);
        images_jump[8] = BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.ace_jump9);
        resizeImages(images_jump);

        images_down = new Bitmap[7];
        images_down[0] = BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.ace_down1);
        images_down[1] = BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.ace_down2);
        images_down[2] = BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.ace_down3);
        images_down[3] = BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.ace_down4);
        images_down[4] = BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.ace_down5);
        images_down[5] = BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.ace_down5);
        images_down[6] = BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.ace_down1);
        resizeImages(images_down);

        animation_run = new Animation();
        animation_run.setFrames(images_run);
        animation_run.setDelay(2);
        animation_run.setLoop(true);

        animation_jump = new Animation();
        animation_jump.setFrames(images_jump);
        animation_jump.setDelay(2);

        animation_down = new Animation();
        animation_down.setFrames(images_down);
        animation_down.setDelay(2);

        startTime = System.nanoTime();
        delay = 50;
        jump = false;
        gameOver = false;

        x = 100;

        int groundPxScaled = round((deviceHeight*1.0f/Background.HEIGHT)*distanciaY);
        groundY = deviceHeight - groundPxScaled - images_run[0].getHeight();
        y = groundY;
    }

    public void update(){
        if (pulando()) {
            frameCount++;
            if (frameCount > 1) {
                frameCount = 0;

                y += dy;
                dy += g;

                if (y >= groundY) {
                    y = groundY;
                    jump = false;
                }
            }

            animation_jump.update();
        }
        else if (abaixando()) {
            animation_down.update();
        }
        else {
            animation_run.update();
        }
    }

    public void draw(Canvas canvas) {
        Bitmap image = getImage();
        if (image != null)
            canvas.drawBitmap(image, x, y, null);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Rect getRectangle() {
        return new Rect(x + margemErroX, y, x+ animation_run.getImage().getWidth()- margemErroX, y + animation_run.getImage().getHeight()- margemErroY);
    }

    public void pular() {
        if (!jump) {
            jump = true;
            dy = -60;
            animation_jump.iniciar();
        }
    }

    public void abaixar() {
        if (!down) {
            down = true;
            animation_down.iniciar();
        }
    }

    public void perdeu() {
        gameOver = true;
    }

    private void resizeImages(Bitmap[] images) {
        int newHeight = deviceHeight/6;
        for (int i = 0; i < images.length; i++) {
            float scale = newHeight*1.0f/images[i].getHeight();
            int newWidth = round(images[i].getWidth()*scale);
            images[i] = Bitmap.createScaledBitmap(images[i], newWidth, newHeight, true);
        }
    }

    private Bitmap getImage() {
        if (!gameOver) {
            if (pulando()) {
                return animation_jump.getImage();
            }
            else if (abaixando()) {
                return animation_down.getImage();
            }
            else {
                return animation_run.getImage();
            }
        }
        else {
            long elapsed = (System.nanoTime() - startTime) / 1000000;
            if (elapsed > 200) {
                startTime = System.nanoTime();
                piscar = !piscar;
            }

            if (piscar)
                return animation_run.getImage();

            return null;
        }
    }

    public boolean pulando() {
        return jump || animation_jump.animando();
    }

    public boolean abaixando() {
        if (animation_down.terminou())
            down = false;
        return down;
    }
}
