package thenameofd.dinossauro;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{
    public static int VEL_X = -12;

    private MainThread thread;
    private Background bg;
    private Player player;
    private ArrayList<Obstaculo> obstaculos;

    private int frameCount;
    private int frame;
    private Random r;

    private long startTime_score;
    private long delay_score = 500;

    private boolean gameOver;

    private Preference preference;

    private int meioTela;

    public GamePanel(Context context) {
        super(context);
        preference = new Preference(context);


        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);

        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        meioTela = getHeight()/2;
        gameOver = false;

        bg = new Background(this);

        obstaculos = new ArrayList<Obstaculo>();
        obstaculos.add(new Obstaculo(this));

        frameCount = 0;
        r = new Random();
        setDelay();

        startTime_score = System.nanoTime();

        player = new Player(this);

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();

                retry = false;
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void update()
    {
        if (!gameOver) {
            if (!collision()) {
                bg.update();

                frameCount++;
                if (frameCount > frame) {
                    frameCount = 0;
                    obstaculos.add(new Obstaculo(this));

                    setDelay();
                }

                for (int i = 0; i < obstaculos.size(); i++) {
                    obstaculos.get(i).update();
                }

                player.update();
                updateScore();
            }
        }
    }

    @Override
    public void draw (Canvas canvas)
    {
        bg.draw(canvas);

        Iterator<Obstaculo> it = obstaculos.iterator();
        while (it.hasNext()) {
            Obstaculo o = it.next();
            if (o.offScreen())
                it.remove();
        }

        for (int i = 0; i < obstaculos.size(); i++) {
            obstaculos.get(i).draw(canvas);

        }

        player.draw(canvas);
        drawScore(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (event.getY() < meioTela) {
                player.pular();
            }
            else {
                player.abaixar();
            }
        }

        return super.onTouchEvent(event);
    }

    private void setDelay() {
        frame = r.nextInt(100) + 30;
    }

    private boolean collision() {


        Rect playerRect = player.getRectangle();
        for (int i = 0; i < obstaculos.size(); i++) {
            Obstaculo obstaculo = obstaculos.get(i);
            if (Rect.intersects(playerRect, obstaculo.getRectangle())) {
                if (obstaculo.obstaculoVoa() && !player.abaixando()) {
                    player.perdeu();
                    gameOver = true;

                    return true;
                }
                else if (!obstaculo.obstaculoVoa() && !player.pulando()) {
                    player.perdeu();
                    gameOver = true;

                    return true;
                }
            }
        }

        return false;
    }

    private void updateScore() {
        long elapsed = (System.nanoTime() - startTime_score)/1000000;
        if (elapsed > delay_score) {
            startTime_score = System.nanoTime();
            int score = preference.getScore() + 1;
            preference.setScore(score);
        }
    }

    private void drawScore(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(40);
        canvas.drawText("Score: " + preference.getScore(), canvas.getWidth() - 300, 100, paint);
    }
}
