package algonquin.cst2335.gogidze;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton btn = findViewById(R.id.bottom);
        btn.setOnClickListener((vw) -> {
            int width = btn.getWidth();
            int height = btn.getHeight();
        });

    }
}