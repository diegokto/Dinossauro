package thenameofd.dinossauro;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;


public class Background
{
    public static final int GROUND = 85;
    public static final int HEIGHT = 533;
    public static final int WIDTH = 800;
    private Bitmap image;
    private int x, y;

    public Background(GamePanel gamePanel)
    {
        this.image = BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.background_jogo2);
        this.image = Bitmap.createScaledBitmap(this.image, gamePanel.getWidth(), gamePanel.getHeight(), true);
    }

    public void update()
    {
        x += GamePanel.VEL_X;
        if (x < - image.getWidth()) {
            x = 0;
        }
    }

    public void draw(Canvas canvas)
    {
        if (canvas != null)
        {
            canvas.drawBitmap(image, x, y, null);
            if (x < 0) {
                canvas.drawBitmap(image, x+image.getWidth(), y, null);
            }
        }
    }
}
