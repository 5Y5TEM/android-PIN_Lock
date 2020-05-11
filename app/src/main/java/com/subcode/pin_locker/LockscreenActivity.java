package com.subcode.pin_locker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class LockscreenActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int REQUEST_READ_PHONE_STATE = 0;
    private int currentApiVersion;

//    ComponentName devAdminReceiver;


    /**
     * Request permission to access system wallpaper
     * @param requestCode
     * @param permissions
     * @param grantResults
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
     * Requires API 21 LOLLIPOP to work (startLockTask())
     * @param savedInstanceState
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeItFullScreen();


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






        /**
         * Smart lock function
         */
//        devAdminReceiver = new ComponentName(getApplicationContext(), LockscreenActivity.class);
//        String[] paramDevice = new String[]{"com.subcode.pin_locker"};
//        setLockTaskPackages(devAdminReceiver, paramDevice);
        startLockTask();


        setContentView(R.layout.activity_lockscreen);


        /**
         * Get Buttons and Textview
         */

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

        Button ok = findViewById(R.id.btn_ok);
        ok.setOnClickListener(this);

        Button emergency = findViewById(R.id.btn_emergency);
        emergency.setOnClickListener(this);

        TextView input = findViewById(R.id.input);

    }


    @Override
    protected void onPause() {
        super.onPause();

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
    }


    @Override
    protected void onResume() {
        super.onResume();

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED, WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
    }




    /**
     * Back Button functionality
     */
    @Override
    public void onBackPressed() {
        return; //Do nothing!
    }


    @Override

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_HOME)
        {
            Log.i("Home Button","Clicked");
        }
        return false;
    };


    /**
     * When the Unlock Button is pressed, the following function is called:
     */
    public void unlockScreen(View view) {
        //Instead of using finish(), this totally destroys the process
        android.os.Process.killProcess(android.os.Process.myPid());
//        finish();
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
                // do your code
                break;

            case R.id.btn_emergency:
                // do your code
                break;

            default:
                break;
        }

    }

}
