package algonquin.cst2335.gogidze;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class SecondActivity extends AppCompatActivity {

    Button changePicture;

    ActivityResultLauncher <Intent> cameraResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult() ,
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Bitmap thumbnail = data.getParcelableExtra("data");


                        try {


                            FileOutputStream fOut = openFileOutput("Picture.png", Context.MODE_PRIVATE);
                            thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                            fOut.flush();
                            fOut.close();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();

                        } catch (IOException ioe) {
                            Log.w("IOException", "cannot find PNG file");
                        }
                    }
                }
            });




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String userTyped = intent.getStringExtra("EmailAddress");// If "EmailAddress" is not found, return null

        //save data from first page
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("EmailAddress", userTyped);

        editor.apply();








        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.view);
        textView.setText("Welcome Back " + userTyped);

        Button callNumber = findViewById(R.id.button);
        callNumber.setOnClickListener( click -> {



            EditText editText = findViewById(R.id.editText);
            String phoneNumber = editText.getText().toString();

            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(Uri.parse("tel:" + phoneNumber));

            startActivity(call);

            SharedPreferences pref = getSharedPreferences("MyDate", Context.MODE_PRIVATE);
            pref.getString("tel:", phoneNumber);
            SharedPreferences.Editor editor1 = pref.edit();
            editor1.putString("tel:", phoneNumber);

            editor1.apply();



        });



        Button changePicture = findViewById(R.id.button2);
        changePicture.setOnClickListener( click -> {

            ImageView imageView = findViewById(R.id.imageView);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


            cameraResult.launch(cameraIntent);




        });

        File file = new File(getFilesDir(),"algonquin.cst2335.gogidze" );
        if(file.exists()) {
            Bitmap theImage = BitmapFactory.decodeFile("algonquin.cst2335.gogidze");
        }


    }

}
