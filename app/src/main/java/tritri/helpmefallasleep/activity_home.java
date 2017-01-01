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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

public class Activity_home extends Activity implements AudioService.AudioServiceListener {
    private AudioService mAudioService;
    private Boolean mIsBoundToService = false;
    private Boolean mToShuffle = false;
    private Timer mTimer;
    private CheckBox mShuffleCheckBox;
    private SharedPreferencesHelper mSharedPreferencesHelper;
    private Spinner mTimerSpinner;
    private Button mStartButton, mStopButton;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        /**
         * This is called from Android. It passes in the service binder
         * that was created in the service.
         *
         * @param name
         * @param service
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mAudioService = ((AudioService.AudioServiceBinder)service).getService();
            mAudioService.registerListener(Activity_home.this);
            mIsBoundToService = true;
            setStartButtonState(mAudioService.isRunning());
        }

        /** This doesn't happen unless the service crashes*/
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIsBoundToService = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mStartButton = (Button) findViewById(R.id.startButton);
        mStopButton = (Button )findViewById(R.id.stopButton);
        mTimerSpinner = (Spinner) findViewById(R.id.spinner);
        mShuffleCheckBox = (CheckBox) findViewById(R.id.checkBox);

        mTimer = new Timer(this, mTimerSpinner);
        mSharedPreferencesHelper = new SharedPreferencesHelper(this);
        mToShuffle = mShuffleCheckBox.isChecked();
    }

    /**
     * This method calls bindService which obtains a connection
     * to the service. This just makes a connection to the service,
     * and starting the service will be called later. This calls
     * onBind in the service.
     *
     * Also, the shuffle checkbox is updated based on the shared preferences.
     * The time indicated on the spinner is also updated.
     */
    @Override
    protected void onStart() {
        super.onStart();

        bindService(new Intent(this, AudioService.class), mServiceConnection, Context.BIND_AUTO_CREATE);
        mToShuffle = mSharedPreferencesHelper.AreItemsToShuffle();
        mShuffleCheckBox.setChecked(mToShuffle);
        mTimerSpinner.setSelection(mSharedPreferencesHelper.GetTimerPosition());
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mIsBoundToService)
        {
            unbindService(mServiceConnection);
            mIsBoundToService = false;
            mAudioService.unRegisterListener();
        }

        mSharedPreferencesHelper.SetSharedPreferencesToShuffle(mShuffleCheckBox);
    }

    @Override
    protected void onDestroy() {
        mAudioService.stopPlayback();
        super.onDestroy();
    }

    public void stop(View v) {
        // Disable both buttons until service sends a message saying playback stopped successfully
        mStopButton.setEnabled(false);
        if (mAudioService.stopPlayback()) {
            mStopButton.setEnabled(true);
            mStartButton.setEnabled(true);
        }
    }

    public void addToList(View v) {
        this.startActivity(new Intent(this, ActivityAddToList.class));
    }

    public void instructions (View v) {
        this.startActivity(new Intent(this, ActivityInstructions.class));
    }

    public void turnOnSound() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        // if the sound is already on, don't bother
        if (volume != 0) {
            // TO get current volume level
            volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        }
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    public void start(View v) {
        turnOnSound();
        // use this to start and trigger a service
        Intent i= new Intent(this, AudioService.class);
        i.putExtra("timer value", mTimer.mSelectedTime);
        i.putExtra("toShuffle", mShuffleCheckBox.isChecked());
        startService(i);
        mIsBoundToService = true;
        mStartButton.setEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSharedPreferencesHelper == null)
            mSharedPreferencesHelper = new SharedPreferencesHelper(this);
        mToShuffle = mSharedPreferencesHelper.AreItemsToShuffle();
        mShuffleCheckBox.setChecked(mToShuffle);
        mTimerSpinner.setSelection(mSharedPreferencesHelper.GetTimerPosition());
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSharedPreferencesHelper.SetSharedPreferencesToShuffle(mShuffleCheckBox);
    }

    @Override
    public void serviceFinishedCallback(final Boolean isRunning) {
        setStartButtonState(isRunning);
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

    public void setStartButtonState(Boolean isServiceRunning) {
        if (mStartButton != null)
            mStartButton.setEnabled(!isServiceRunning);
    }
}
