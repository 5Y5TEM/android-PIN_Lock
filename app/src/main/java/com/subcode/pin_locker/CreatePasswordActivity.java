package com.subcode.pin_locker;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import static com.subcode.pin_locker.MainActivity.pw_type;
import static com.subcode.pin_locker.MainActivity.wallpaperDrawable1;

public class CreatePasswordActivity extends AppCompatActivity {

    EditText txt_setPw, txt_confirmPw;
    TextView txt_info;
    Button btn_cancel, btn_save;

    private int currentApiVersion;


    /**
     * OnCreate
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);
        makeItFullScreen();




        /**
         * Set system wallpaper as background
         */
        if (wallpaperDrawable1==null)
        {
            Resources res = getResources();
            Drawable drawable1=res.getDrawable(R.color.colorPrimaryDark);
            getWindow().setBackgroundDrawable(drawable1);

        }
        else getWindow().setBackgroundDrawable(wallpaperDrawable1);



        txt_setPw = findViewById(R.id.setNewPw);
        txt_confirmPw = findViewById(R.id.confirmNewPw);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_save = findViewById(R.id.btn_savePw);
        txt_info = findViewById(R.id.txt_setPw);

        // Set text according to password type
        if(pw_type.equals("password")){
            txt_info.setText("Set a LOCK password");
        }
        else if(pw_type.equals("destroy_pw")){
            txt_info.setText("Set a DESTROY password");
        }


        btn_save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String text1 = txt_setPw.getText().toString();
                String text2 = txt_confirmPw.getText().toString();

                if(text1.equals("") || text2.equals("")){
                    // If one of the two textviews is left empty
                    Toast.makeText(CreatePasswordActivity.this, "No password entered!", Toast.LENGTH_SHORT).show();
                }

                else{
                    // Both fields set

                    if(text1.equals(text2)){
                        // Both fields identical


                        if(text1.length()<4){
                            // Pw shorter than 4 digits
                            Toast.makeText(CreatePasswordActivity.this, "Password should be longer than 4 digits!", Toast.LENGTH_SHORT).show();
                        }

                        else if(text1.length()>8){
                            // Pw longer than 8 digits
                            Toast.makeText(CreatePasswordActivity.this, "Passwords should not be longer than 8 digits!", Toast.LENGTH_SHORT).show();
                        }

                        else{
                            // Pw has correct length

                            if(pw_type.equals("password")){
                                // If the pw to be set is lock password, check if destroy pw is the same
                                SharedPreferences settings = getSharedPreferences("PREPS", 0);
                                String dest_pw = settings.getString("destroy_pw", "");

                                if(dest_pw.equals(text1)){
                                    // Lock pw = destroy pw
                                    Toast.makeText(CreatePasswordActivity.this, "Lock password and destroy password cannot be the same!", Toast.LENGTH_SHORT).show();
                                }

                                else{
                                    // Lock pw is not destroy pw
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putString(pw_type, text1);
                                    editor.apply();

                                    Toast.makeText(CreatePasswordActivity.this, "Password set successfully!", Toast.LENGTH_SHORT).show();


                                    // Open Main Activity again
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }

                            else if(pw_type.equals("destroy_pw")){
                                // If the pw to be set is destroy password, check if lock pw is the same
                                SharedPreferences settings = getSharedPreferences("PREPS", 0);
                                String dest_pw = settings.getString("password", "");

                                if(dest_pw.equals(text1)){
                                    // Lock pw = destroy pw
                                    Toast.makeText(CreatePasswordActivity.this, "Lock password and destroy password cannot be the same!", Toast.LENGTH_SHORT).show();
                                }

                                else{
                                    // Lock pw is not destroy pw
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putString(pw_type, text1);
                                    editor.apply();

                                    Toast.makeText(CreatePasswordActivity.this, "Password set successfully!", Toast.LENGTH_SHORT).show();


                                    // Open Main Activity again
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                        }
                    }
                    else{
                        // Passwords do not match
                        Toast.makeText(CreatePasswordActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                    }
                }

            }



        });




        btn_cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Open Main Activity again
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
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
