package tritri.helpmefallasleep;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class activity_add_to_list extends AppCompatActivity {

    ListView listView;
    List<String> toSpeak;
    ArrayAdapter<String> arrayAdapter;
    EditText editText;
    Button okButton;
    boolean isActionModeOpen;
    List<String> toRemove;
    SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    protected void onResume() {
        super.onResume();
        Set<String> set = new HashSet<>();
        if (sharedPreferencesHelper == null)
            sharedPreferencesHelper = new SharedPreferencesHelper(this);
        toSpeak = sharedPreferencesHelper.GetItemsToSpeak(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_add_to_list);
        listView = (ListView) findViewById(R.id.listView);
        editText = (EditText) findViewById(R.id.addOption);
        okButton = (Button) findViewById(R.id.addToListButton);
        toRemove = new ArrayList<>();
        isActionModeOpen = false;
        // get items to speak
        sharedPreferencesHelper = new SharedPreferencesHelper(this);
        toSpeak = sharedPreferencesHelper.GetItemsToSpeak(this);

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                toRemove.add(arrayAdapter.getItem(position));

                if (!isActionModeOpen) {
                    startListActionMode();
                }
        }
        });

        displayList();
    }

    private void startListActionMode() {
        ActionMode mActionMode = startActionMode(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.setTitle("Selected");

                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.menu_contextactionbar, menu);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                editText.setVisibility(View.GONE);
                okButton.setVisibility(View.GONE);

                isActionModeOpen = true;

                Log.v("isActionModeOpen", isActionModeOpen + "");
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                Log.v("OnActionItemClicked", "itemClicked()");

                switch (item.getItemId()) {
                    case R.id.action_delete:
                        Log.v("Delete clicked", "deleteClicked()");

                        for (String element : toRemove) {
                            arrayAdapter.remove(element);
                        }

                        toRemove.clear();
                        listView.clearChoices();
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
                editText.setVisibility(View.VISIBLE);
                okButton.setVisibility(View.VISIBLE);
                listView.setItemsCanFocus(false);
                isActionModeOpen = false;
            }
        });
    }

    public void hideKeyboard(View view) {
        //InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Timer.INPUT_METHOD_SERVICE);
        //inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void addToList(View v)
    {
        String text = editText.getText().toString();
        toSpeak.add(text);
        editText.setText("");
        displayList();
    }

    public void displayList() {
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, toSpeak);

        listView.setAdapter(arrayAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_add_to_list, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        sharedPreferencesHelper.SetSharedPreferences(this, toSpeak);
    }
}
