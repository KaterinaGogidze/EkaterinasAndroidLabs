package algonquin.cst2335.gogidze;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CheckBox cb = findViewById(R.id.check);


        RadioButton radio = findViewById(R.id.radio);
        Switch sw = findViewById(R.id.sw);

        sw.setOnCheckedChangeListener(( btn, onOrOff) -> {
            radio.setChecked(onOrOff);

            Toast.makeText(MainActivity.this, "You clicked on switch", Toast.LENGTH_LONG).show();
        });

        cb.setOnCheckedChangeListener((b, c) -> {
            Toast.makeText(MainActivity.this, "You clicked on checkbox", Toast.LENGTH_LONG).show();
            if(c)
                radio.setChecked(true);
            else
                radio.setChecked(false);
        });



    }

}