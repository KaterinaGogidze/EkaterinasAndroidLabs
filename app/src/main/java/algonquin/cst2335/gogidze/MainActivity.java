package algonquin.cst2335.gogidze;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/** This page lets the user type in a pass....
 *
 * @author Ekaterina Gogidze
 * @versin 1.0
 */
public class MainActivity extends AppCompatActivity {

    /** This holds the text at the centre of the screen */
    TextView tv = null;

    /** This holds the password text at the centre of the screen */
    EditText et = null;

    /** This holds the Login button  at the button of the screen */
    Button btn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView);
        et = findViewById(R.id.editText);
        btn = findViewById(R.id.button);

        btn.setOnClickListener( clk -> {
            String password = et.getText().toString();

            if (checkPasswordComplexity(password))
                tv.setText("Your password meets the requirements");
          else
              tv.setText("You shall not pass!");


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