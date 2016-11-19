package thenameofd.dinossauro;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;


public class GamePanel
{
    public static int VEL_X = -12;

    private Activity activity;

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
    private boolean gamePause;
    private boolean gamePlay;
    private boolean gameOpen;

    private Preference preference;

    private int meioTela;

    private int deviceWidth;
    private int deviceHeight;

    private MyAudio audio;

    public GamePanel(Activity activity) {
        this.activity = activity;
        preference = new Preference(activity);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) activity.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        deviceWidth = displayMetrics.widthPixels;
        deviceHeight = displayMetrics.heightPixels;

        audio = new MyAudio(activity, true);
        initialize();
    }

    private void initialize() {
        meioTela = deviceHeight/2;
        gameOver = false;

        bg = new Background(this);

        obstaculos = new ArrayList<Obstaculo>();
        obstaculos.add(new Obstaculo(this));

        frameCount = 0;
        r = new Random();
        setDelay();

        startTime_score = System.nanoTime();

        player = new Player(this);
        gameOpen = true;
    }



    public void update()
    {
        if (!gameOpen) {
            if (!gamePause) {
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
                } else {
                    frameCount++;
                }
            }
        }
    }

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

        if (gameOpen) {
            drawGameOpen(canvas);
        }

        else if (gameOver) {
            drawGameOver(canvas);
        }

        else if (gamePause) {
            drawGamePause(canvas);
        }
    }

    public void onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (gameOpen) {
                gamePlay = true;
                gameOpen = false;
                gamePause = false;
            }
            else if (gamePause) {
                gamePlay = true;
                gamePause = false;
            }
            else if (!gameOver) {
                if (event.getY() < meioTela) {
                    player.pular();
                    audio.puloPlay();
                } else {
                    player.abaixar();
                }
            }
            else {
                if (frameCount > 30) {
                    reset();
                }
            }
        }
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
                    setGameOver();
                    audio.colisaoPlay();
                    return true;
                }
                else if (!obstaculo.obstaculoVoa() && !player.pulando()) {
                    setGameOver();
                    audio.colisaoPlay();
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

    private void drawGameOpen(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(40);
        canvas.drawText("DINOSSAURO", canvas.getWidth()/2 - 120, canvas.getHeight()/2 - 50, paint);
        canvas.drawText("DIOGO DE FREITAS KATO " , canvas.getWidth()/2 - 260, canvas.getHeight()/2, paint);
        canvas.drawText("Toque na tela para começar", canvas.getWidth()/2 - 300, canvas.getHeight()/2 -200, paint);
    }

    private void drawGameOver(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(40);
        canvas.drawText("GAME OVER", canvas.getWidth()/2 - 100, canvas.getHeight()/2 - 50, paint);
        canvas.drawText("High Score: " + preference.getHighScore(), canvas.getWidth()/2 - 120, canvas.getHeight()/2, paint);
        canvas.drawText("Score: " + preference.getScore(), canvas.getWidth()/2 - 80, canvas.getHeight()/2 + 50, paint);
        canvas.drawText("Toque na tela para jogar outra vez", canvas.getWidth()/2 - 300, canvas.getHeight()/2 -200, paint);
    }

    private void drawGamePause(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(40);
        canvas.drawText("Toque na tela para continuar", canvas.getWidth()/2 - 300, canvas.getHeight()/2 -200, paint);
    }

    private void reset() {
        gameOver = false;
        gamePause = false;
        gamePlay = true;

        obstaculos = new ArrayList<Obstaculo>();
        obstaculos.add(new Obstaculo(this));

        player.reset();

        preference.zerarScore();

        frameCount = 0;
    }

    private void setGameOver() {
        gameOver = true;
        player.perdeu();
        preference.saveHighScore();
        frameCount = 0;
    }

    public void resumeEvent()     {
//        gamePlay = true;
//        gamePause = false;
    }

    public void pauseEvent() {
        gamePause = true;
        gamePlay = false;
    }

    public Resources getResources() {
        return activity.getResources();
    }

    public int getWidth() {
        return deviceWidth;
    }

    public int getHeight() {
        return deviceHeight;
    }
}
