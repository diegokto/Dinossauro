package thenameofd.dinossauro;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

import static java.lang.Math.random;
import static java.lang.Math.round;

public class Obstaculo {
    private final int distanciaTransparente = 10;

    private int distanciaY = Background.GROUND - distanciaTransparente;
    private int x;
    private int y;
    private int dx = -10;
    private int dy;
    private int width;
    private int height;
    private int deviceWidth;
    private int deviceHeight;

    private int[] ids = {
            R.drawable.flamingo1,
            R.drawable.flamingo2,
            R.drawable.flamingo3
    };

    private Bitmap image;

    public Obstaculo(GamePanel gamePanel) {
        deviceWidth = gamePanel.getWidth();
        deviceHeight = gamePanel.getHeight();

        Random r = new Random();
        int index = r.nextInt(ids.length)%ids.length;

        image = BitmapFactory.decodeResource(gamePanel.getResources(), ids[index]);
        image = resizeImage(image);

        x = deviceWidth;
        int groundPxScaled = round((deviceHeight*1.0f/Background.HEIGHT)*distanciaY);
        System.out.println(groundPxScaled);
        y = deviceHeight - groundPxScaled - image.getHeight();

        //flamingo voador
        if (index == 0) {
            y += -(deviceHeight/7);
        }
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public void update(){
        x += dx;
    }

    public void draw(Canvas canvas) {
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
        return new Rect(x, y, x+image.getWidth(), y+image.getHeight());
    }

    public boolean offScreen() {
        if (x < -image.getWidth())
            return true;

        return false;
    }

    private Bitmap resizeImage(Bitmap image) {
        int newHeight = round(deviceHeight*1.0f/6);
        float scale = newHeight*1.0f/image.getHeight();
        int newWidth = round(image.getWidth()*scale);
        return Bitmap.createScaledBitmap(image, newWidth, newHeight, true);
    }
}

