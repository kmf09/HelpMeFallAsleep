package tritri.helpmefallasleep;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Katrina on 2/28/2016.
 */
public class SharedPreferencesHelper {
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private static final String WORDS_TO_SPEAK = "wordsToSpeak";
    private static final String TO_SHUFFLE = "toShuffle";
    private static final String TIMER_VALUE = "timerValue";

    public SharedPreferencesHelper(Context context) {
        List<String> toSpeak = GetItemsToSpeak();

        mSharedPreferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        if (toSpeak == null)
        {
            // set initial list
            toSpeak = new ArrayList<>();
            toSpeak.add("friends");
            toSpeak.add("walking on the beach");
            toSpeak.add("riding a rollercoaster");
            toSpeak.add("jumping on the moon");
            toSpeak.add("watching the sunset");
            toSpeak.add("taking off in a space shuttle");
            toSpeak.add("a childhood memory");
            toSpeak.add("swimming");
            SetSharedPreferencesToSpeak(toSpeak);
        }
    }

    public void SetSharedPreferencesToSpeak(List<String> toSpeak) {
        Set<String> set = new HashSet<>();
        set.addAll(toSpeak);
        mEditor.putStringSet(WORDS_TO_SPEAK, set);
        mEditor.apply();
    }

    public void SetSharedPreferencesToShuffle(CheckBox shuffleCheckBox) {
        mEditor.putBoolean(TO_SHUFFLE, shuffleCheckBox.isChecked());
        mEditor.apply();
    }

    public void SetTimerPosition(int position) {
        mEditor.putInt(TIMER_VALUE, position);
        mEditor.apply();
    }

    public List<String> GetItemsToSpeak() {
        if (mSharedPreferences.contains(WORDS_TO_SPEAK))
        {
            return new ArrayList<>(mSharedPreferences.getStringSet(WORDS_TO_SPEAK, null));
        }

        return null;
    }

    public Boolean GetItemsToShuffle() {
        if (mSharedPreferences.contains(TO_SHUFFLE))
        {
            return mSharedPreferences.getBoolean(TO_SHUFFLE, false);
        }

        return false;
    }

    public int GetTimerPosition() {
        if (mSharedPreferences.contains(TIMER_VALUE))
        {
            return mSharedPreferences.getInt(TIMER_VALUE, 0);
        }

        return 0;
    }
}
