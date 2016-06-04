package tritri.helpmefallasleep;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;

import java.util.ArrayList;

public class Timer implements AdapterView.OnItemSelectedListener {
    private static int baseNumber = 10;
    public int selectedTime;

    protected Timer(Context context, Spinner timer) {
        if (timer != null) {
            String[] numberPickerValues = new String[10];
            for(int i = 0; i < numberPickerValues.length; i++)
                numberPickerValues[i] = Integer.toString((i * 10) + baseNumber) + "sec";
            ArrayAdapter<String> adapter_state = new ArrayAdapter<>(context,
                    android.R.layout.simple_spinner_item, numberPickerValues);
            adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            timer.setAdapter(adapter_state);
            timer.setOnItemSelectedListener(this);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // remove "sec"
        selectedTime = Integer.parseInt(parent.getItemAtPosition(position).toString().replace("sec", ""));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

//    public void submit(View v) {
//        Intent intent = new Intent();
//        intent.setClass(this, activity_home.class);
//        intent.putExtra("number_picker_value", Integer.parseInt(numberPickerValues[numberPicker.getValue()]));
//        this.startActivity(intent);
//    }
}
