package com.subcode.pin_locker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
public static String question;
public static String answer;
private static final int REQUEST_READ_PHONE_STATE = 0;
private int currentApiVersion;

    /**
     * Request permission to access system wallpaper
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                }
                break;

            default:
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // load the password
        SharedPreferences settings = getSharedPreferences("PREPS", 0);
        question = settings.getString("security_question", "");
        answer = settings.getString("security_answer", "");

        setContentView(R.layout.activity_forgot_password);


        TextView txt_question = findViewById(R.id.txt_question);
        txt_question.setText(question);

        makeFullScreen();

        Button btn_continue = findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(this);

        /**
         * Check permission to access system wallpaper
         */
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_PHONE_STATE);
        } else {
            //TODO
        }


        /**
         * Access system wallpaper
         */
        WallpaperManager wallpaperManager1 = WallpaperManager
                .getInstance(getApplicationContext());
        final Drawable wallpaperDrawable1 = wallpaperManager1.getDrawable();

        if (wallpaperDrawable1==null)
        {
            Resources res = getResources();
            Drawable drawable1=res.getDrawable(R.color.colorPrimaryDark);
            getWindow().setBackgroundDrawable(drawable1);

        }
        else
        {
            getWindow().setBackgroundDrawable(wallpaperDrawable1);
        }

    }

    /**
     * A simple method that sets the screen to fullscreen.  It removes the Notifications bar,
     *   the Actionbar and the virtual keys (if they are on the phone)
     */
    public void makeFullScreen() {
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

    @Override
    public void onClick(View v) {
        EditText input = findViewById(R.id.answer);
        String input_string = input.getText().toString();

        switch (v.getId()) {

            case R.id.btn_continue:

                if(input_string.equals("")){
                    //If input string is empty
                    Toast.makeText(ForgotPasswordActivity.this, "Please answer the question!", Toast.LENGTH_SHORT).show();
                }

                else{
                    //input string not empty
                    if(input_string.equals(answer)){
                        //correct
                        Intent intent = new Intent(getApplicationContext(), CreatePasswordActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        //incorrect answer
                        Toast.makeText(ForgotPasswordActivity.this, "Answer is not correct", Toast.LENGTH_SHORT).show();
                    }

                }

                break;

            default:
                break;

        }
    }
}
