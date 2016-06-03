package tritri.helpmefallasleep;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class activity_home extends ActionBarActivity {
    TextToSpeech textToSpeech;
    int timerValue;
    List<String> toSpeak;
    SharedPreferencesHelper sharedPreferencesHelper;
    AudioService audioService;
    Boolean mBoundToService = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_home);

        // get timer value
        if (getIntent().hasExtra("number_picker_value")) {
            timerValue = getIntent().getIntExtra("number_picker_value", 10);
        }
        // deafult
        else {
            timerValue = 1;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        bindService(new Intent(this, AudioService.class), serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mBoundToService)
        {
            unbindService(serviceConnection);
            mBoundToService = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        if (mBoundToService)
//        {
//            stopService(new Intent(this, AudioService.class));
//        }
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
        // use this to start and trigger a service
        Intent i= new Intent(this, AudioService.class);
        // potentially add data to the intent
        i.putExtra("timer value", timerValue);
        startService(i);
    }

    public void stop(View v) {
        if (mBoundToService) {
            unbindService(serviceConnection);
            mBoundToService = false;
        }
        Intent i = new Intent(this, AudioService.class);
        stopService(i);
    }

    @Override
    protected void onPause() {
        super.onPause();

        //TODO: Implement this
//        SharedPreferences sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        Set<String> set = new HashSet<>();
//        set.addAll(toSpeak);
//        editor.putStringSet("wordsToSpeak", set);
//        editor.apply();
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AudioService.AudioServiceBinder binder = (AudioService.AudioServiceBinder) service;
            audioService = binder.getService();
            mBoundToService = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBoundToService = false;
        }
    };
}
