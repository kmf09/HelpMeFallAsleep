package tritri.helpmefallasleep;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class activity_home extends ActionBarActivity {
    TextToSpeech textToSpeech;
    int numberPickerValue;
    List<String> toSpeak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_home);

        // get timer value
        if (getIntent().hasExtra("number_picker_value")) {
            numberPickerValue = getIntent().getIntExtra("number_picker_value", 10);
        }

        // get the list to speak
        SharedPreferences sharedpreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        if (sharedpreferences.contains("wordsToSpeak"))
        {
            toSpeak = new ArrayList<>(sharedpreferences.getStringSet("wordsToSpeak", null));
        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_home, menu);
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

    public void addToList(View v) {
        this.startActivity(new Intent(this, activity_add_to_list.class));
    }

    public void setTimer(View v) {
        this.startActivity(new Intent(this, activity_set_timer.class));
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
        Collections.shuffle(toSpeak);

        for (String description : toSpeak)
        {
            try {
                // for debugging purposes
                Toast.makeText(getApplicationContext(), description, Toast.LENGTH_SHORT).show();
                textToSpeech.speak(description, TextToSpeech.QUEUE_ADD, null);
                // for API 21 : lollipop
                //textToSpeech.speak(description, TextToSpeech.QUEUE_ADD, null, Integer.toString(description.hashCode()));

                Thread.sleep(numberPickerValue * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop(View v) {
        textToSpeech.stop();
    }
}
