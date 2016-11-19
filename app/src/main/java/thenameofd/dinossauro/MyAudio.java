package thenameofd.dinossauro;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

/**
 * Created by TheNameofD on 05/11/2016.
 */

public class MyAudio {

    protected Activity m_mainActivity;

    private MediaPlayer mp_colisao;
    private MediaPlayer mp_pulo;

    private boolean audio;

    public MyAudio(Activity mainActivity, boolean preference_audio) {
        m_mainActivity = mainActivity;
        audio = preference_audio;

        if (audio) {
            try {
                AssetFileDescriptor assetFileDescriptor = mainActivity.getResources().openRawResourceFd(R.raw.colisao);
                mp_colisao = new MediaPlayer();
                mp_colisao.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
                assetFileDescriptor.close();
                assetFileDescriptor = null;
                mp_colisao.prepare();
                //mp_lateral = MediaPlayer.create(m_mainActivity, R.raw.lateral);

                assetFileDescriptor = mainActivity.getResources().openRawResourceFd(R.raw.pulo);
                mp_pulo = new MediaPlayer();
                mp_pulo.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
                assetFileDescriptor.close();
                assetFileDescriptor = null;
                mp_pulo.prepare();
                //mp_gol = MediaPlayer.create(m_mainActivity, R.raw.lateral);
            } catch (Exception e) {

            }
        }
    }

    public void colisaoPlay() {
        if (audio) {
            if (mp_colisao != null) {
                mp_colisao.start();
            }
        }
    }

    public void puloPlay() {
        if (audio) {
            if (mp_pulo != null) {
                mp_pulo.start();
            }
        }
    }
}

