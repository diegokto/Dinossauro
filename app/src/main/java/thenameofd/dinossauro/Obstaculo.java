package thenameofd.dinossauro;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

import static java.lang.Math.random;
import static java.lang.Math.round;

public class Obstaculo {
    private final int distanciaTransparente = 0;

    private int distanciaY = Background.GROUND - distanciaTransparente;
    private int x;
    private int y;
    private int dy;
    private int width;
    private int height;
    private int deviceWidth;
    private int deviceHeight;
    private int margemErroLeft = 20;
    private int margemErroRight = 20;
    private int margemErroTop = 20;
    private int margemErroBottom = 20;

    private boolean fly;

    private int[] ids = {
//            R.drawable.flamingo1,
//            R.drawable.flamingo2,
//            R.drawable.flamingo3,
            R.drawable.flamingo4,
            R.drawable.flamingo5,
            R.drawable.flamingo6,
    };

    private Bitmap image;

    public Obstaculo(GamePanel gamePanel) {
        deviceWidth = gamePanel.getWidth();
        deviceHeight = gamePanel.getHeight();

        Random r = new Random();
        int index = r.nextInt(ids.length)%ids.length;

        image = BitmapFactory.decodeResource(gamePanel.getResources(), ids[index]);
        if (index == 0) {
            image = resizeImage(image, round(deviceHeight*1.0f/5));
        }
        else {
            image = resizeImage(image);
        }


        x = deviceWidth;
        int groundPxScaled = round((deviceHeight*1.0f/Background.HEIGHT)*distanciaY);
        y = deviceHeight - groundPxScaled - image.getHeight();

        margemErroRight = image.getWidth()/2;

        //flamingo voador
        if (index != 0) {
            fly = true;
            y += -(deviceHeight/7);


            margemErroBottom = 0;
        }
    }

    public void update(){
        x += GamePanel.VEL_X;
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
        return new Rect(x + margemErroLeft, y + margemErroTop, x + image.getWidth() - margemErroRight, y + image.getHeight() - margemErroBottom);
    }

    public boolean offScreen() {
        if (x < -image.getWidth())
            return true;

        return false;
    }

    private Bitmap resizeImage(Bitmap image, int newHeight) {
        //newHeight = round(deviceHeight*1.0f/6);
        float scale = newHeight*1.0f/image.getHeight();
        int newWidth = round(image.getWidth()*scale);
        return Bitmap.createScaledBitmap(image, newWidth, newHeight, true);
    }

    private Bitmap resizeImage(Bitmap image) {
        int newHeight = round(deviceHeight*1.0f/6);
        float scale = newHeight*1.0f/image.getHeight();
        int newWidth = round(image.getWidth()*scale);
        return Bitmap.createScaledBitmap(image, newWidth, newHeight, true);
    }

    public boolean obstaculoVoa() {
        return fly;
    }
}

