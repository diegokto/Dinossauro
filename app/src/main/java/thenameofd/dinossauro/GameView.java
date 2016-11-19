package thenameofd.dinossauro;

import android.content.Context;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback
{
    //--------------------------------------------------------------------------------
    //
    //--------------------------------------------------------------------------------
    private SurfaceHolder surfaceHolder;

    private GamePanel game;
    private MainThread thread;

    //--------------------------------------------------------------------------------
    // Constructors.
    //--------------------------------------------------------------------------------
    public GameView(Context context, GamePanel gamePanel)
    {
        super(context);

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        setFocusable(true);
        setKeepScreenOn(true);

        this.game = gamePanel;
        thread = new MainThread(surfaceHolder, this.game);
    }

    //--------------------------------------------------------------------------------
    //
    //--------------------------------------------------------------------------------
//    public boolean onBackPressed()
//    {
//        boolean handled = false;
//
//        if (m_gameMain != null)
//            handled = m_gameMain.backPressed();
//
//        return handled;
//    }

    //--------------------------------------------------------------------------------
    // Interaction events (from android.view.View).
    //--------------------------------------------------------------------------------
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event)
//    {
//        boolean handled = false;
//
//        if (m_gameMain != null)
//            handled = m_gameMain.keyDown(keyCode, event);
//
//        if (!handled)
//            handled = super.onKeyDown(keyCode, event);
//
//        return handled;
//    }

//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event)
//    {
//        boolean handled = false;
//
//        if (m_gameMain != null)
//            handled = m_gameMain.keyUp(keyCode, event);
//
//        if (!handled)
//            handled = super.onKeyUp(keyCode, event);
//
//        return handled;
//    }

//    @Override
//    public boolean onTrackballEvent(MotionEvent event)
//    {
//        return super.onTrackballEvent(event);
//    }

    public boolean onTouchEvent(MotionEvent event)
    {
        boolean handled = false;

        if (game != null) {
            game.onTouchEvent(event);
            handled = true;
        }

        if (!handled)
            handled = super.onTouchEvent(event);

        return handled;
    }

    //--------------------------------------------------------------------------------
    // SurfaceHolder.Callback methods.
    //--------------------------------------------------------------------------------
    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        // If surface was destroyed, then game loop thread was also destroyed.
        // In this case, we must reconstruct it.
        if (thread == null)
            thread = new MainThread(surfaceHolder, game);

        thread.setRunning(true);
        //thread.start();

        if(!thread.isAlive()){
            thread.start();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        if (thread != null) {
            boolean retry = true;
            thread.setRunning(false);
            while (retry) {
                try {
                    thread.join();
                    retry = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            thread = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    //--------------------------------------------------------------------------------
    // Our own methods.
    //--------------------------------------------------------------------------------


    //--------------------------------------------------------------------------------
    // Our own methods, to be called by SurfaceActivity.
    //--------------------------------------------------------------------------------
//    public void restartEvent()
//    {
//        if (game != null)
//            game.restartEvent();
//    }
//
//    public void startEvent()
//    {
//        if (game != null)
//            game.startEvent();
//    }

    public void resumeEvent()
    {
        if (game != null)
            game.resumeEvent();
    }

    public void pauseEvent()
    {
        if (game != null)
            game.pauseEvent();
    }

//    public void stopEvent()
//    {
//        if (game != null)
//            game.stopEvent();
//    }
//
//    public void destroyEvent()
//    {
//        if (game != null)
//            game.destroyEvent();
//    }
}

