package algonquin.cst2335.gogidze;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton imgbtn = findViewById(R.id.myimagebutton);
        imgbtn.setOnClickListener((vw) -> {
            int width = imgbtn.getWidth();
            int height = imgbtn.getHeight();
        });

    }
}