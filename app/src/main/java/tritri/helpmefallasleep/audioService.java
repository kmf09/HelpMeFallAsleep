package tritri.helpmefallasleep;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
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
    private AudioServiceListener mAudioServiceListener;
    private SpeechAsyncTask mSpeechAsyncTask;

    @Override
    public void onCreate() {
        mTextToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    mTextToSpeech.setLanguage(Locale.US);
                }
            }
        });
    }

    public void speak(Integer timerValue, Boolean toShuffle) {
        if (mTextToSpeech != null) {
            mSpeechAsyncTask = new SpeechAsyncTask(timerValue, toShuffle);
            mSpeechAsyncTask.execute();
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
        Utilities.setIsRunning(mAudioServiceListener, false);
        mAudioServiceListener = null;
        super.onDestroy();
    }

    public boolean stopPlayback() {
        if (mSpeechAsyncTask == null)
            return true;
        mSpeechAsyncTask.cancel(true);
        return mSpeechAsyncTask.isCancelled();
    }

    public Boolean isRunning() {
        return mSpeechAsyncTask != null && mSpeechAsyncTask.getStatus() == AsyncTask.Status.RUNNING;
    }

    public void registerListener(AudioServiceListener asl) {
        mAudioServiceListener = asl;
    }

    public void unRegisterListener() {
        mAudioServiceListener = null;
    }

    public interface AudioServiceListener {
        void serviceFinishedCallback(Boolean stoppedValue);
    }

    public class AudioServiceBinder extends Binder {
        AudioService getService() {
            return AudioService.this;
        }
    }

    private class SpeechAsyncTask extends AsyncTask<String, Void, Void> {
        private Integer mTimerValue;
        private List<String> mToSpeak;

        public SpeechAsyncTask(Integer timerValue,  boolean toShuffle) {
            SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(getApplicationContext());
            mTimerValue = setTimerValue(timerValue);
            mToSpeak = sharedPreferencesHelper.GetItemsToSpeak();

            if (toShuffle) {
                Collections.shuffle(mToSpeak);
            }
        }

        private Integer setTimerValue(Integer timerValue) {
            if (timerValue == null) {
                return 10; // default
            }
            return timerValue;
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected Void doInBackground(String... strings) {
            for (int i = 0; i < mToSpeak.size(); i++) {
                try {
                    if (isCancelled())
                        break;

                    mTextToSpeech.speak(mToSpeak.get(i), TextToSpeech.QUEUE_ADD, null, Integer.toString(mToSpeak.get(i).hashCode()));
                    // for API 21 : lollipop
                    //textToSpeech.speak(description, TextToSpeech.QUEUE_ADD, null, Integer.toString(description.hashCode()));

                    if (i != mToSpeak.size()-1)
                        Thread.sleep(mTimerValue * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Utilities.setIsRunning(mAudioServiceListener, false);
        }

        @Override
        protected void onCancelled() {
            Utilities.setIsRunning(mAudioServiceListener, false);
        }
    }
}

