package thenameofd.dinossauro;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

    private GamePanel gamePanel;
    private GameView gameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        gamePanel = new GamePanel(this);

        gamePanel = new GamePanel(this);

        gameView = new GameView(getApplicationContext(), gamePanel);
        setContentView(gameView);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (gamePanel != null)
            gamePanel.resumeEvent();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        if (gamePanel != null)
            gamePanel.pauseEvent();
    }
}
