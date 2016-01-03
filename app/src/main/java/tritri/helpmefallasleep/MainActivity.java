package tritri.helpmefallasleep;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.util.Collections;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextToSpeech textToSpeech;
    int numberPickerValue = 10;
    NumberPicker numberPicker;
    String[] numberPickerValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: Move this to activity_add_to_list.java and then pass it through an intent, must be done through an array can't passs List<string>
        Button numberPickerButton = (Button) findViewById(R.id.numberPickerButton);
        numberPicker = (NumberPicker) findViewById(R.id.numberPicker);
        int base = 10;
        numberPickerValues = new String[10];
        for(int i = 0; i < numberPickerValues.length; i++)
            numberPickerValues[i] = Integer.toString((i * 10) + base);
        numberPicker.setMaxValue(numberPickerValues.length-1);
        numberPicker.setMinValue(0);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setDisplayedValues(numberPickerValues);
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR)
                {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });
    }

    public void turnOnSound() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int volume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
        // if the sound is already on, don't bother
        if (volume != 0) {
            volume = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        }
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, volume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    public void start(View v) {
        turnOnSound();
//        Collections.shuffle(toSpeak);
//
//        for (String description : toSpeak)
//        {
//            try {
//                // for debugging purposes
//                //Toast.makeText(getApplicationContext(), description, Toast.LENGTH_SHORT).show();
//                //textToSpeech.speak(description, TextToSpeech.QUEUE_ADD, null);
//                // for API 21 : lollipop
//                textToSpeech.speak(description, TextToSpeech.QUEUE_ADD, null, Integer.toString(description.hashCode()));
//
//                Thread.sleep(numberPickerValue * 1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }

    public void stop(View v) {
        textToSpeech.stop();
    }

    public void timeValuesSelected(View v) {
        numberPickerValue = Integer.parseInt(numberPickerValues[numberPicker.getValue()]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
