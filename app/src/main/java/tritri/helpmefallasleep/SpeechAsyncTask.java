//package tritri.helpmefallasleep;
//
//import android.os.AsyncTask;
//import android.speech.tts.TextToSpeech;
//
//import java.util.Collections;
//import java.util.List;
//
///**
// * Created by Katrina on 12/10/2016.
// */
//
//public class SpeechAsyncTask extends AsyncTask<String, Void, Void> {
//    private Integer mTimerValue;
//    private List<String> mToSpeak;
//    private TextToSpeech mTextToSpeech;
//    private AudioService mAudioService;
//
//    public SpeechAsyncTask(Integer timerValue, TextToSpeech textToSpeech, AudioService audioService, SharedPreferencesHelper sharedPreferencesHelper, boolean toShuffle) {
//        this.mTimerValue = setTimerValue(timerValue);
//        this.mToSpeak = sharedPreferencesHelper.GetItemsToSpeak();
//        this.mTextToSpeech = textToSpeech;
//        this.mAudioService = audioService;
//
//        if (toShuffle) {
//            Collections.shuffle(mToSpeak);
//        }
//    }
//
//    private Integer setTimerValue(Integer timerValue) {
//        if (timerValue == null) {
//            return 10; // default
//        }
//        return timerValue;
//    }
//
//    @Override
//    protected void onPreExecute() {}
//
//    @Override
//    protected Void doInBackground(String... strings) { //TODO: Either figure out what this means or change it
//        for (String description : mToSpeak) {
//            try {
//                if (isCancelled()) {
//                    break;
//                }
//                mTextToSpeech.speak(description, TextToSpeech.QUEUE_ADD, null, Integer.toString(description.hashCode()));
//                // for API 21 : lollipop
//                //textToSpeech.speak(description, TextToSpeech.QUEUE_ADD, null, Integer.toString(description.hashCode()));
//
//                Thread.sleep(mTimerValue * 1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(Void aVoid) {
//
//        Utilities.setIsRunning(mAudioServiceListener, false);
//    }
//
//    @Override
//    protected void onCancelled() {
//        Utilities.setIsRunning(mAudioServiceListener, false);
//    }
//}
