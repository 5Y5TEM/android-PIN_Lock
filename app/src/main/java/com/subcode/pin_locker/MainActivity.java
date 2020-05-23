package com.subcode.pin_locker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.ActivityManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private int currentApiVersion;
    private String password;
    public static int width, height;
    private static final int REQUEST_READ_PHONE_STATE = 0;
    private static final int SYSTEM_ALERT_WINDOW_PERMISSION = 0;

    public static boolean service_is_running;
    public static String pw_type = "password";
    public static Drawable wallpaperDrawable1;



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


    /**
     * OnCreate
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Get Screen Size of Device
        // This is for the Lockscreen Overlay in LockScreenActivity
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;


        // Function to make the app fullscreen and hide navigation and toolbar
        makeFullScreen();



        /**
         * On initial start, let user set security question
         * Check if variable is set
         */
        // load the password
        SharedPreferences settings = getSharedPreferences("PREPS", 0);
        String answer = settings.getString("security_answer", "");

        if(answer.equals("")){
            //If there is no security question set, start the activity
            Intent intent = new Intent(getApplicationContext(), SetSecurityQuestionActivity.class);
            startActivity(intent);
            finish();
        }

        else {
            // Security question is set, start main activity
            setContentView(R.layout.activity_main);


            // Check if service is running
            if (isMyServiceRunning(LockScreenService.class)) service_is_running = true;


            // Grab buttons
            Button set_pw = findViewById(R.id.set_pw);
            set_pw.setOnClickListener(this);

            Button forgot_pw = findViewById(R.id.fgt_pw);
            forgot_pw.setOnClickListener(this);

            Button set_dest = findViewById(R.id.btn_set_dest);
            set_dest.setOnClickListener(this);

            Button forgot_dest = findViewById(R.id.btn_forgot_dest);
            forgot_dest.setOnClickListener(this);

            // If service is currently running, set the button to active
            final Switch sw = findViewById(R.id.switch2);
            if (service_is_running) sw.setChecked(true);

            // Toggle button for service ON OFF
            sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    SharedPreferences settings = getSharedPreferences("PREPS", 0);
                    String password = settings.getString("password", "");

                    if (isChecked) {
                        if(password.equals("")){
                            // If no password set, don't allow to start the lock service
                            sw.setChecked(false);
                            Toast.makeText(MainActivity.this, "Set a lock password first!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            // Ask for permission for overlay
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getApplicationContext())) {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                        Uri.parse("package:" + getPackageName()));
                                startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION);
                            }

                            startService();
                        }

                    } else {
                        stopService();
                    }
                }
            });


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
            wallpaperDrawable1 = wallpaperManager1.getDrawable();

            if (wallpaperDrawable1 == null) {
                Resources res = getResources();
                wallpaperDrawable1 = res.getDrawable(R.color.colorPrimaryDark);

            }

        }
        getWindow().setBackgroundDrawable(wallpaperDrawable1);
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


    /**
     * OnClick Button Functionalities
     */
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);

        switch (v.getId()) {

            case R.id.set_pw:
                pw_type = "password";
                setPassword(v, "password");
                break;

            case R.id.fgt_pw:
                pw_type = "password";
                startActivity(intent);
                finish();
                break;

            case R.id.btn_set_dest:
                pw_type = "destroy_pw";
                setPassword(v, "destroy_pw");
                break;

            case R.id.btn_forgot_dest:
                pw_type = "destroy_pw";
                startActivity(intent);
                finish();
                break;

            default:
                break;

        }
    }


    /**
     * Open Window to set password
     */
    public void setPassword(View view, String pw) {

        // Load the password
        SharedPreferences settings = getSharedPreferences("PREPS", 0);
        password = settings.getString(pw, "");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (password.equals("")){
        // If there is no password
                    Intent intent = new Intent(getApplicationContext(), CreatePasswordActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
        //If there is a password
                    Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },0);
    }


    /**
     * Start the foreground service
     */
    public void startService() {
        Intent serviceIntent = new Intent(this, LockScreenService.class);
        serviceIntent.putExtra("inputExtra", "input");
        ContextCompat.startForegroundService(this, serviceIntent);
    }


    /**
     * Stop the foreground service
     */
    public void stopService() {
        Intent serviceIntent = new Intent(this, LockScreenService.class);
        stopService(serviceIntent);
    }


    /**
     * Check if service is running
     */
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
