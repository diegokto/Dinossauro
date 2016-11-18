package thenameofd.dinossauro;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Preference {

    private static final String PREFS_NAME = "my_prefs";
    private static final String KEY_HIGHSCORE = "high_score";
    private static final String KEY_SCORE = "score";

    private SharedPreferences sharedPreferences;
    private int score;
    private int highScore;

//    public Preference(SharedPreferences sp) {
//        sharedPreferences = sp;
//        setAtributes();
//    }

    public Preference(Activity activity) {
        sharedPreferences = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        setAtributes();
    }
    public Preference(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        setAtributes();
    }

    private void setAtributes() {
        if (sharedPreferences != null) {
            highScore = sharedPreferences.getInt(KEY_HIGHSCORE, 0);
            score = sharedPreferences.getInt(KEY_SCORE, 0);
        }
    }

    public int getHighScore() {
        return highScore;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
        if (score > highScore)
            highScore = score;
    }

    public void savePreferences()
    {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putInt(KEY_HIGHSCORE, highScore);
            editor.putInt(KEY_SCORE, score);
            editor.commit();
        }
    }

    public void zerarScore() {
        score = 0;
    }
}
