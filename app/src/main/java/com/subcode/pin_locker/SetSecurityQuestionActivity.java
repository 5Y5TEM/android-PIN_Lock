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
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SetSecurityQuestionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private static final int REQUEST_READ_PHONE_STATE = 0;
    private static String selected_question;
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
        makeFullScreen();
        setContentView(R.layout.activity_set_security_question);


        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
            R.array.security_questions, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);



        Button btn_continue = (Button) findViewById(R.id.btn_continue);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selected_question = parent.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_continue:
                setSecurityQuestion();
                break;

            default:
                break;
        }
    }

    private void setSecurityQuestion() {
        EditText answer = findViewById(R.id.answer);
        String input = answer.getText().toString();


        if(input.equals("")){
            //If no answer set
            Toast.makeText(SetSecurityQuestionActivity.this, "You have to set a security question!", Toast.LENGTH_SHORT).show();

        }

        else{
            //If answer set, save question and answer to shared preferences
            SharedPreferences settings = getSharedPreferences("PREPS", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("security_question", selected_question);
            editor.putString("security_answer", input);
            editor.apply();


            // Open Main Activity
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();

        }
    }


}
