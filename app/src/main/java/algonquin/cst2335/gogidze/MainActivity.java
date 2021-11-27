package algonquin.cst2335.gogidze;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/** This page lets the user type in a pass....
 *
 * @author Ekaterina Gogidze
 * @versin 1.0
 */
public class MainActivity extends AppCompatActivity {

    /** This holds the text at the centre of the screen */
    TextView tv = null;

    /** This holds the password text at the centre of the screen */
    EditText cityText = null;

    /** This holds the Login button  at the button of the screen */
    Button forecastBtn = null;
    private String stringUrl;
    Bitmap image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView);
        cityText = findViewById(R.id.cityTextField);
        forecastBtn = findViewById(R.id.forecastButton);

        forecastBtn.setOnClickListener( clk -> {
            String cityName = cityText.getText().toString();
            Executor newTread = Executors.newSingleThreadExecutor();


                             //Run function
            newTread.execute( ( ) -> {
                //done on a second processor:
                try {
                    stringUrl = "https://api.openweathermap.org/data/2.5/weather?q="
                            + URLEncoder.encode(cityName, "UTF-8")
                            + "&appid=7e943c97096a9784391a981c4d878b22&units=metric";

                    //Must be done on other thread
                    URL url = new URL(stringUrl); //build the server connection
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(); //connect to server
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream()); // read the information

                    String text = (new BufferedReader(
                            new InputStreamReader(in, StandardCharsets.UTF_8)))
                            .lines()
                            .collect(Collectors.joining("\n"));

                    JSONObject theDocument = new JSONObject( text );
                    JSONObject mainObject = theDocument.getJSONObject("main");
                    double current = mainObject.getDouble("temp");
                    double max = mainObject.getDouble("temp_max");
                    double min = mainObject.getDouble("temp_min");
                    int Humitidy = mainObject.getInt("humidity");

                runOnUiThread( ( ) -> {
                    TextView tv = findViewById(R.id.temp);
                    tv.setText("The current temperature is " + current);
                    tv.setVisibility(View.VISIBLE);

                    tv = findViewById(R.id.minTemp);
                    tv.setText("The min temperature is " + current);
                    tv.setVisibility(View.VISIBLE);

                    tv = findViewById(R.id.maxTemp);
                    tv.setText("The max temperature is " + current);
                    tv.setVisibility(View.VISIBLE);

                    tv = findViewById(R.id.humitidy);
                    tv.setText("The humidity is " + current);
                    tv.setVisibility(View.VISIBLE);

                    ImageView iv = findViewById(R.id.icon);
                    iv.setImageBitmap(image);
                });

                    JSONArray weatherArray = theDocument.getJSONArray("weather");
                    JSONObject position0 = weatherArray.getJSONObject(0);

                    String description = position0.getString("description");
                    String iconName = position0.getString("icon");

                    File file = new File(getFilesDir(), iconName +".png");
                    if(file.exists()) {
                        image = BitmapFactory.decodeFile(getFilesDir() + "/" + iconName + ".png");
                    } else {
                        URL imgURL = new URL("https://openweathermap.org/img/w/" + iconName + ".png");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.connect();
                        int responseCode = connection.getResponseCode();
                        if (responseCode == 200) {
                            image = BitmapFactory.decodeStream(connection.getInputStream());


                        }
                    }

                    FileOutputStream fOut = null;
                    try {
                        fOut = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                        image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                        fOut.flush();
                        fOut.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


                }
                catch (JSONException je) {
                    Log.e("JSONException", je.getMessage());
                }
                catch (IOException ioe) {
                    Log.e("Connection error:", ioe.getMessage());
                }
            }); //end of newThread.execute()




        });


    }



    /** This function checks if the password has Upper case, Lower Case, Number and Special Character.
     *
     * @param pw The String object that we are checking
     * @return True if all conditions are met, otherwise false.
     */
    boolean checkPasswordComplexity(String pw) {
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;

        //Star Looping.
        for(int i = 0; i < pw.length(); i++) {
            char c = pw.charAt(i);
            Log.i("Looking at char:", ""+c);
            if(Character.isLowerCase(c))
                foundLowerCase = true;
            if (Character.isUpperCase(c))
                foundUpperCase = true;
            if (Character.isDigit(c))
                foundNumber = true;
            else if(isSpecialCharacter(c)) {
                foundSpecial = true;
            }
        }

        if(!foundLowerCase) {
            Toast.makeText(MainActivity.this, "Missing lower case character", Toast.LENGTH_LONG).show();
            return false;
        }
        if(!foundUpperCase) {
            Toast.makeText(MainActivity.this, "Missing Upper case character", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!foundNumber) {
            Toast.makeText(MainActivity.this, "Missing number", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!foundSpecial) {
            Toast.makeText(MainActivity.this, "Missing special character", Toast.LENGTH_LONG).show();
            return false;
        }
        //else
            //return true;

        // If anything is false, then it is not in the password.
        return foundLowerCase && foundUpperCase && foundNumber && foundSpecial;
    }

    /** This returns if one of this character {#$%^&*!@?}, false otherwise
     *
     * @param c The character we are checking
     * @return True if c is one of {#$%^&*!@?}, false otherwise
     */
    boolean isSpecialCharacter(char c) {
        switch(c) {
            case'#':
            case'$':
            case'%':
            case'^':
            case'&':
            case'*':
            case'!':
            case'@':
            case'?':
                return true;
            default:
                return false;
        }
    }
}