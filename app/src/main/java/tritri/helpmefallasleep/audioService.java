package tritri.helpmefallasleep;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;

/**
 * Created by Katrina on 3/5/2016.
 */
public class AudioService extends Service {
    private TextToSpeech mTextToSpeech;
    private SharedPreferencesHelper mSharedPreferencesHelper;
    private Queue<Integer> mQueue = new LinkedList<>();
    private AudioServiceListener mAudioServiceListener;
    private SpeechAsyncTask mSpeechAsyncTask;

    @Override
    public void onCreate() {
        mSharedPreferencesHelper = new SharedPreferencesHelper(this);
        mTextToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    mTextToSpeech.setLanguage(Locale.US);
                }
            }
        });

        if (!mQueue.isEmpty()) {
            speak(mQueue.poll(), false);
        }
    }

    public void speak(Integer timerValue, Boolean toShuffle) {
        if (mTextToSpeech != null) {
            mSpeechAsyncTask = new SpeechAsyncTask(timerValue, mTextToSpeech, mAudioServiceListener, mSharedPreferencesHelper, toShuffle);
            mSpeechAsyncTask.execute();
        } else {
            mQueue.add(timerValue);
        }
    }

    /**
     * This method is called after OnCreate() in the service. These
     * are called when StartService() is called from the main activity.
     *
     * @param intent
     * @param flags
     * @param startId
     * @return int
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            speak(intent.getIntExtra("timer value", 1), intent.getBooleanExtra("toShuffle", false));
        }
        return Service.START_STICKY;
    }

    /**
     * This is called when bindService is called from the main activity.
     * It returns a binder to the service.
     * @Intnet intent
     * @return service binder to Android
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new AudioServiceBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onDestroy() {
        if (mTextToSpeech != null) {
            mTextToSpeech.stop();
            mTextToSpeech.shutdown();
        }
        utilities.setIsRunning(mAudioServiceListener, false);
        mAudioServiceListener = null;
        super.onDestroy();
    }

    public boolean stopPlayback() {
        mSpeechAsyncTask.cancel(true);
        if (mSpeechAsyncTask.isCancelled()) {
            return true;
        }
        return false;
    }

    public void registerListener(AudioServiceListener asl) {
        mAudioServiceListener = asl;
    }

    public interface AudioServiceListener {
        void isRunning(Boolean stoppedValue);
    }

    public class AudioServiceBinder extends Binder {
        AudioService getService() {
            return AudioService.this;
        }
    }
}