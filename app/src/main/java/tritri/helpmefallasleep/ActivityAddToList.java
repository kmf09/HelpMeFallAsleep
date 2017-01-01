package tritri.helpmefallasleep;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ActivityAddToList extends Activity {
    private ListView mListView;
    private List<String> mToSpeak;
    private ArrayAdapter<String> mArrayAdapter;
    private EditText mEditText;
    private Button mOkButton;
    private boolean mIsActionModeOpen;
    private List<String> mToRemove;
    private SharedPreferencesHelper mSharedPreferencesHelper;

    @Override
    protected void onResume() {
        super.onResume();
        if (mSharedPreferencesHelper == null)
            mSharedPreferencesHelper = new SharedPreferencesHelper(this);
        mToSpeak = mSharedPreferencesHelper.GetItemsToSpeak();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_list);
        mListView = (ListView) findViewById(R.id.listView);
        mEditText = (EditText) findViewById(R.id.addOption);
        mOkButton = (Button) findViewById(R.id.addToListButton);
        mToRemove = new ArrayList<>();
        mIsActionModeOpen = false;
        // get items to speak
        mSharedPreferencesHelper = new SharedPreferencesHelper(this);
        mToSpeak = mSharedPreferencesHelper.GetItemsToSpeak();

//        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    hideKeyboard(v);
//                }
//            }
//        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                mToRemove.add(mArrayAdapter.getItem(position));

                if (!mIsActionModeOpen) {
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
                mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                mEditText.setVisibility(View.GONE);
                mOkButton.setVisibility(View.GONE);

                mIsActionModeOpen = true;

                Log.v("isActionModeOpen", mIsActionModeOpen + "");
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

                        for (String element : mToRemove) {
                            mArrayAdapter.remove(element);
                            updateListItems(element);
                        }

                        mToRemove.clear();
                        mListView.clearChoices();
                        mArrayAdapter.notifyDataSetChanged();
                        return true;
                    default:
                        Log.v("Done clicked", "doneClicked()");
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                Log.v("Done clicked", "doneClicked()");
                mEditText.setVisibility(View.VISIBLE);
                mOkButton.setVisibility(View.VISIBLE);
                mListView.setItemsCanFocus(false);
                mIsActionModeOpen = false;
            }
        });
    }

//    public void hideKeyboard(View view) {
//        //InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Timer.INPUT_METHOD_SERVICE);
//        //inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
//    }

    public void updateListItems(String item) {
        mToSpeak.remove(item);
        mSharedPreferencesHelper.SetSharedPreferencesToSpeak(mToSpeak);
    }

    public void addToList(View v)
    {
        String text = mEditText.getText().toString();
        if (text != null && !text.isEmpty())
        {
            mToSpeak.add(text);
            mEditText.setText("");
        }
        displayList();
    }

    public void displayList() {
        mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, mToSpeak);

        mListView.setAdapter(mArrayAdapter);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
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
        mSharedPreferencesHelper.SetSharedPreferencesToSpeak(mToSpeak);
    }
}
