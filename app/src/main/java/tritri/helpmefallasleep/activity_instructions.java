package tritri.helpmefallasleep;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class activity_instructions extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
    }

    public void returnToMainApp(View v) {
        this.startActivity(new Intent(this, activity_home.class));
    }

}
