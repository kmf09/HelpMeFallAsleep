package tritri.helpmefallasleep;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

public class activity_set_timer extends AppCompatActivity {
    NumberPicker numberPicker;
    String[] numberPickerValues;
    public static int baseNumber = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_timer);

        // TODO: Move this to activity_add_to_list.java and then pass it through an intent, must be done through an array can't passs List<string>
        //Button numberPickerButton = (Button) findViewById(R.id.numberPickerButton);
        numberPicker = (NumberPicker) findViewById(R.id.numberPicker);
        numberPickerValues = new String[10];
        for(int i = 0; i < numberPickerValues.length; i++)
            numberPickerValues[i] = Integer.toString((i * 10) + baseNumber);
        numberPicker.setMaxValue(numberPickerValues.length-1);
        numberPicker.setMinValue(0);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setDisplayedValues(numberPickerValues);

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

    public void submit(View v) {
        Intent intent = new Intent();
        intent.setClass(this, activity_home.class);
        intent.putExtra("number_picker_value", Integer.parseInt(numberPickerValues[numberPicker.getValue()]));
        this.startActivity(intent);
    }
}
