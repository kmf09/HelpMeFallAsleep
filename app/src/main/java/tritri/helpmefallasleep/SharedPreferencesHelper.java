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
    public SharedPreferencesHelper(Context context) {
        List<String> toSpeak = GetItemsToSpeak(context);

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
            SetSharedPreferencesToSpeak(context, toSpeak);
        }
    }

    public void SetSharedPreferencesToSpeak(Context context, List<String> toSpeak) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> set = new HashSet<>();
        set.addAll(toSpeak);
        editor.putStringSet("wordsToSpeak", set);
        editor.apply();
    }

    public void SetSharedPreferencesToShuffle(Context context, CheckBox shuffleCheckBox) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("toShuffleBool", shuffleCheckBox.isChecked());
        editor.apply();
    }

    public void SetSelectedTime(Context context, int location) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("selectedTime", location);
        editor.apply();
    }

    public List<String> GetItemsToSpeak(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        if (sharedpreferences.contains("wordsToSpeak"))
        {
            return new ArrayList<>(sharedpreferences.getStringSet("wordsToSpeak", null));
        }

        return null;
    }

    public Boolean GetItemsToShuffle(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        if (sharedpreferences.contains("toShuffleBool"))
        {
            return sharedpreferences.getBoolean("toShuffleBool", false);
        }

        return false;
    }

    public int GetSelectedTime(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        if (sharedpreferences.contains("selectedTime"))
        {
            return sharedpreferences.getInt("selectedTime", 10);
        }

        return 10;
    }
}
