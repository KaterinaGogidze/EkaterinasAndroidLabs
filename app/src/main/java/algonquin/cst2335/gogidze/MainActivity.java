package algonquin.cst2335.gogidze;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(TAG, "In onCreate() - Loading Widgets");
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("MyDate", Context.MODE_PRIVATE);
         prefs.getString("EmailAddress", "NONE");
         String emailAddress = prefs.getString("EmailAddress", "");


        //now xml is loaded xml
        TextView topView = findViewById(R.id.textview);
        EditText editText = findViewById(R.id.myedittext);
        editText.setText(emailAddress);
        Button btn = findViewById(R.id.mybutton);

        btn.setOnClickListener( (click) ->
                {

                    Intent nextPage = new Intent(MainActivity.this, SecondActivity.class);
                    String userTyped = editText.getText().toString();
                    nextPage.putExtra("EmailAddress", editText.getText().toString());



                    //Make the transition:
                    startActivity( nextPage );

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("EmailAddress", "");
                    editor.apply();

                }); // OnClickListener goes in here
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w(TAG, "In onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w(TAG, "In onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w(TAG, "In onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w(TAG, "In onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}