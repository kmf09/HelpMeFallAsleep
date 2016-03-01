package tritri.helpmefallasleep;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Katrina on 2/28/2016.
 */
public class ItemsToSpeak {
    public ItemsToSpeak(Context context) {
        List<String> toSpeak = GetItemsToSpeak(context);

        if (toSpeak == null)
        {
            // set initial list
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
            SetSharedPreferences(context, toSpeak);
        }
    }

    public void SetSharedPreferences(Context context, List<String> toSpeak) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> set = new HashSet<>();
        set.addAll(toSpeak);
        editor.putStringSet("wordsToSpeak", set);
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
}
