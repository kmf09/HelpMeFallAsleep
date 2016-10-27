package tritri.helpmefallasleep;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Timer implements AdapterView.OnItemSelectedListener {
    private static int mBaseNumber = 10;
    private SharedPreferencesHelper mSharedPreferencesHelper;
    public int mSelectedTime;

    protected Timer(Context context, Spinner timer) {
        if (timer != null) {
            String[] numberPickerValues = new String[10];
            for(int i = 0; i < numberPickerValues.length; i++)
                numberPickerValues[i] = Integer.toString((i * 10) + mBaseNumber) + "sec";
            ArrayAdapter<String> adapter_state = new ArrayAdapter<>(context,
                    android.R.layout.simple_spinner_item, numberPickerValues);
            adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            timer.setAdapter(adapter_state);
            timer.setOnItemSelectedListener(this);
            mSharedPreferencesHelper = new SharedPreferencesHelper(context);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // remove "sec"
        mSelectedTime = Integer.parseInt(parent.getItemAtPosition(position).toString().replace("sec", ""));
        mSharedPreferencesHelper.SetTimerPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
