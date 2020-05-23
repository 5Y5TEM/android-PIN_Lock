package com.subcode.pin_locker;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import static com.subcode.pin_locker.MainActivity.pw_type;
import static com.subcode.pin_locker.MainActivity.wallpaperDrawable1;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private int currentApiVersion;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        makeItFullScreen();




        /**
         * Set System Wallpaper as background
         */
        if (wallpaperDrawable1==null)
        {
            Resources res = getResources();
            Drawable drawable1=res.getDrawable(R.color.colorPrimaryDark);
            getWindow().setBackgroundDrawable(drawable1);

        }
        else getWindow().setBackgroundDrawable(wallpaperDrawable1);


        // Grab Buttons and Textview

        TextView input = findViewById(R.id.input);

        TextView txt = findViewById(R.id.textView3);

        Button zero = findViewById(R.id.button0);
        zero.setOnClickListener(this);

        Button one = findViewById(R.id.button1);
        one.setOnClickListener(this);

        Button two = findViewById(R.id.button2);
        two.setOnClickListener(this);

        Button three = findViewById(R.id.button3);
        three.setOnClickListener(this);

        Button four = findViewById(R.id.button4);
        four.setOnClickListener(this);

        Button five = findViewById(R.id.button5);
        five.setOnClickListener(this);

        Button six = findViewById(R.id.button6);
        six.setOnClickListener(this);

        Button seven = findViewById(R.id.button7);
        seven.setOnClickListener(this);

        Button eight = findViewById(R.id.button8);
        eight.setOnClickListener(this);

        Button nine = findViewById(R.id.button9);
        nine.setOnClickListener(this);

        Button delete = findViewById(R.id.btn_delete);
        delete.setOnClickListener(this);
        delete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                TextView input = findViewById(R.id.input);
                input.setText("");
                return true;
            }
        });

        Button ok = findViewById(R.id.btn_ok);
        ok.setOnClickListener(this);

        if(pw_type.equals("password")){
            txt.setText("Please enter your current LOCK password");
        }
        else if(pw_type.equals("destroy_pw")){
            txt.setText("Please enter your current DESTROY password");
        }


    }



    /**
     * Handle Button Click events
     */

    @Override
    public void onClick(View v) {

        // Haptic Feedback
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
        TextView input = findViewById(R.id.input);

        switch (v.getId()) {

            case R.id.button0:
                input.append("0");
                break;

            case R.id.button1:
                input.append("1");
                break;

            case R.id.button2:
                input.append("2");
                break;

            case R.id.button3:
                input.append("3");
                break;

            case R.id.button4:
                input.append("4");
                break;

            case R.id.button5:
                input.append("5");
                break;

            case R.id.button6:
                input.append("6");
                break;

            case R.id.button7:
                input.append("7");
                break;

            case R.id.button8:
                input.append("8");
                break;

            case R.id.button9:
                input.append("9");
                break;

            case R.id.btn_delete:
                String string = input.getText().toString();

                if(string.equals("Error!"))
                    input.setText("");

                string = input.getText().toString();

                if(!(string.equals(""))){
                    int lastIndex = string.length()-1;
                    string = string.substring(0, lastIndex);
                    input.setText(string);
                }
                break;

            case R.id.btn_ok:

                // load the password
                SharedPreferences settings = getSharedPreferences("PREPS", 0);
                String password = settings.getString(pw_type, "");

                String text = input.getText().toString();

                if(text.equals(password)){
                    Intent intent = new Intent(getApplicationContext(), CreatePasswordActivity.class);
                    startActivity(intent);
                    finish();
                }

                else{
                    Toast.makeText(ChangePasswordActivity.this, "Wrong password!", Toast.LENGTH_SHORT).show();
                    input.setText("");
                }

                break;


            default:
                break;
        }

    }

    /**
     * Back Button functionality
     */
    @Override
    public void onBackPressed() {
        // Open Main Activity again
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }


    /**
     * A simple method that sets the screen to fullscreen.  It removes the Notifications bar,
     *   the Actionbar and the virtual keys (if they are on the phone)
     */
    public void makeItFullScreen() {
        currentApiVersion = android.os.Build.VERSION.SDK_INT;
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        decorView.setSystemUiVisibility(flags);
                    }
                }
            });
        }
    }



}
