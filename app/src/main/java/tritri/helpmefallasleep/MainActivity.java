package tritri.helpmefallasleep;

import android.content.Context;
import android.media.AudioManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.speech.tts.TextToSpeech;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends ActionBarActivity {

    TextToSpeech textToSpeech;
    List<String> toSpeak;
    EditText editText;
    ListView listView;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button start = (Button) findViewById(R.id.startButton);
        editText = (EditText) findViewById(R.id.editText);
        listView = (ListView) findViewById(R.id.list);
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR)
                {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });
        toSpeak = new ArrayList<>();
        toSpeak.add("friends");
        toSpeak.add("kittens");
        toSpeak.add("walking on the beach");
        toSpeak.add("riding a rollercoaster");
        toSpeak.add("puppies");
        toSpeak.add("watching the sunset");
        toSpeak.add("taking off in a space shuttle");
        toSpeak.add("pandas");
        toSpeak.add("taylor swift");
        toSpeak.add("jackie robinson");
        toSpeak.add("shirley temple");
        displayList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.v("Long click", "You have long clicked this item");

                ActionMode mActionMode = startActionMode(new ActionMode.Callback() {
                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        mode.setTitle("Selected");

                        MenuInflater inflater = mode.getMenuInflater();
                        inflater.inflate(R.menu.menu_contextactionbar, menu);
                        return true;
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        return false;
                    }

                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_delete:
                                Log.v("Delete clicked", "deleteClicked()");
                                String toRemove = arrayAdapter.getItem(position);
                                arrayAdapter.remove(toRemove);
                                arrayAdapter.notifyDataSetChanged();
                                return true;
                            default:
                                Log.v("Done clicked", "doneClicked()");
                                return false;
                        }
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {
                        Log.v("Done clicked", "doneClicked()");
                    }
                });
            }
        });
    }

    public void addToList(View v)
    {
        String text = editText.getText().toString();
        toSpeak.add(text);
        editText.setText("");
        displayList();
    }

    public void displayList() {
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, toSpeak);
        listView.setAdapter(arrayAdapter);
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
                //Toast.makeText(getApplicationContext(), description, Toast.LENGTH_SHORT).show();
                textToSpeech.speak(description, TextToSpeech.QUEUE_ADD, null);
                // for API 21 : lollipop
                //textToSpeech.speak(description, TextToSpeech.QUEUE_ADD, null, Integer.toString(description.hashCode()));

                //Thread.sleep(20000);
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop(View v) {

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
