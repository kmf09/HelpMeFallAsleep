package tritri.helpmefallasleep;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;

/**
 * Created by Katrina on 3/5/2016.
 */
public class AudioService extends Service {
    // Binder given to clients
    private final IBinder audioServiceBinder = new AudioServiceBinder();
    TextToSpeech textToSpeech;
    SharedPreferencesHelper sharedPreferencesHelper;
    List<String> toSpeak;
    Boolean isTextToSpeechInitialized = false;
    Queue<Integer> queue = new LinkedList<>();

    @Override
    public void onCreate() {
        sharedPreferencesHelper = new SharedPreferencesHelper(this);
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR)
                {
                    textToSpeech.setLanguage(Locale.US);
                    isTextToSpeechInitialized = true;
                    if (!queue.isEmpty())
                    {
                        speak(queue.poll(), false);
                    }
                }
            }
        });
        toSpeak = sharedPreferencesHelper.GetItemsToSpeak(this);
    }

    public void speak(Integer timerValue, Boolean toShuffle) {
        if (isTextToSpeechInitialized) {

            if (timerValue == null) {
                timerValue = 1; // default
            }

            if (toShuffle) {
                Collections.shuffle(toSpeak);
            }

            SpeechRunnable thread1 = new SpeechRunnable(timerValue);
            Thread t1 = new Thread(thread1);
            t1.start();
        }
        else {
            queue.add(timerValue);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Activity has called startservice and I can do some work
        if (intent != null) {
            speak(intent.getIntExtra("timer value", 1), intent.getBooleanExtra("toShuffle", false));
        }
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return audioServiceBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        textToSpeech.stop();
        return true;
    }

    @Override
    public void onDestroy() {
        textToSpeech.stop();
        textToSpeech.shutdown();
        super.onDestroy();
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class AudioServiceBinder extends Binder {
        AudioService getService() {
            return AudioService.this;
        }
    }

    private class SpeechRunnable implements Runnable {
        Integer timerValue;

        public SpeechRunnable(Integer timerValue) {
            this.timerValue = timerValue;
        }

        @Override
        public void run() {
            for (String description : toSpeak)
            {
                try {
                    textToSpeech.speak(description, TextToSpeech.QUEUE_ADD, null);
                    // for API 21 : lollipop
                    //textToSpeech.speak(description, TextToSpeech.QUEUE_ADD, null, Integer.toString(description.hashCode()));

                    Thread.sleep(timerValue * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
