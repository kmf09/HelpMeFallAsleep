package tritri.helpmefallasleep;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.IBinder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Spinner;

public class activity_home extends Activity {
    AudioService audioService;
    Boolean mBoundToService = false;
    Boolean toShuffle = false;
    Timer timer;
    CheckBox shuffleCheckBox;
    SharedPreferencesHelper sharedPreferencesHelper;
    Spinner timerSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        timerSpinner = (Spinner) findViewById(R.id.spinner);
        timer = new Timer(this, timerSpinner);
        sharedPreferencesHelper = new SharedPreferencesHelper(this);

        shuffleCheckBox = (CheckBox) findViewById(R.id.checkBox);
        toShuffle = shuffleCheckBox.isChecked();
    }

    @Override
    protected void onStart() {
        super.onStart();

        bindService(new Intent(this, AudioService.class), serviceConnection, Context.BIND_AUTO_CREATE);
        toShuffle = sharedPreferencesHelper.GetItemsToShuffle(this);
        shuffleCheckBox.setChecked(toShuffle);
        timerSpinner.setSelection(sharedPreferencesHelper.GetTimerValue(this));
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mBoundToService)
        {
            unbindService(serviceConnection);
            mBoundToService = false;
        }

        sharedPreferencesHelper.SetSharedPreferencesToShuffle(this, shuffleCheckBox);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        i.putExtra("timer value", timer.selectedTime);
        i.putExtra("toShuffle", shuffleCheckBox.isChecked());
        startService(i);
    }

    public void stop(View v) {
        if (mBoundToService) {
            unbindService(serviceConnection);
            mBoundToService = false;
        }
        Intent i = new Intent(this, AudioService.class);
        stopService(i);
        sharedPreferencesHelper.SetSharedPreferencesToShuffle(this, shuffleCheckBox);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sharedPreferencesHelper == null)
            sharedPreferencesHelper = new SharedPreferencesHelper(this);
        toShuffle = sharedPreferencesHelper.GetItemsToShuffle(this);
        shuffleCheckBox.setChecked(toShuffle);
        timerSpinner.setSelection(sharedPreferencesHelper.GetTimerValue(this));
    }

    @Override
    protected void onPause() {
        super.onPause();
        sharedPreferencesHelper.SetSharedPreferencesToShuffle(this, shuffleCheckBox);
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

    public void instructions(View v) {
        // This was for screen overlay, moving to separate activity
        // topLevelLayout.setVisibility(View.VISIBLE);

        this.startActivity(new Intent(this, activity_instructions.class));
    }
}
