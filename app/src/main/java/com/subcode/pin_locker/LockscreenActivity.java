package com.subcode.pin_locker;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.IntentCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.locks.Lock;

import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
import static android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
import static com.subcode.pin_locker.MainActivity.wallpaperDrawable1;

public class LockscreenActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    private static final int REQUEST_READ_PHONE_STATE = 0;
    private static final int SYSTEM_ALERT_WINDOW_PERMISSION = 1;
    private int currentApiVersion;
    public WindowManager winManager;
    public RelativeLayout wrapperView;
    public View home_button_view;
    WindowManager wm;


//    ComponentName devAdminReceiver;


    /**
     * Request permission to access system wallpaper
     *
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

    public class MyView extends View {
        public MyView(Context context) {
            super(context);
        }
    }


    /**
     * Requires API 21 LOLLIPOP to work (startLockTask())
     *
     * @param savedInstanceState
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeItFullScreen();


        /**
         * Permission for Overlay
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            askPermission();
        }



        makeItFullScreen();
        setContentView(R.layout.activity_lockscreen);





        WindowManager.LayoutParams home_params = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        home_button_view = layoutInflater.inflate(R.layout.activity_lockscreen, null);




        home_button_view.findViewById(R.id.button0).setOnClickListener(this);
        home_button_view.findViewById(R.id.button1).setOnClickListener(this);
        home_button_view.findViewById(R.id.button2).setOnClickListener(this);
        home_button_view.findViewById(R.id.button3).setOnClickListener(this);
        home_button_view.findViewById(R.id.button4).setOnClickListener(this);
        home_button_view.findViewById(R.id.button5).setOnClickListener(this);
        home_button_view.findViewById(R.id.button6).setOnClickListener(this);
        home_button_view.findViewById(R.id.button7).setOnClickListener(this);
        home_button_view.findViewById(R.id.button8).setOnClickListener(this);
        home_button_view.findViewById(R.id.button9).setOnClickListener(this);
        home_button_view.findViewById(R.id.btn_delete).setOnClickListener(this);
        home_button_view.findViewById(R.id.btn_ok).setOnClickListener(this);
        home_button_view.findViewById(R.id.btn_emergency).setOnClickListener(this);

        home_button_view.findViewById(R.id.btn_delete).setOnLongClickListener(this);


        home_button_view.setBackground(wallpaperDrawable1);



        wm.addView(home_button_view, home_params);




        currentApiVersion = android.os.Build.VERSION.SDK_INT;
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_IMMERSIVE;
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            home_button_view.setSystemUiVisibility(flags);

            final View decorView = getWindow().getDecorView();
            home_button_view.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        home_button_view.setSystemUiVisibility(flags);
                    }
                }
            });
        }

    }

    @TargetApi(23)
    public void askPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION);
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

    }




    public void onDestroy() {
        super.onDestroy();
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

        if (keyCode == KeyEvent.KEYCODE_HOME) {
            Log.i("Home Button", "Clicked");
            Toast.makeText(LockscreenActivity.this, "Click", Toast.LENGTH_SHORT).show();

        }
        return false;
    }

    ;


    /**
     * When the Unlock Button is pressed, the following function is called:
     */
//    public void unlockScreen(View view) {
    //Instead of using finish(), this totally destroys the process
//        android.os.Process.killProcess(android.os.Process.myPid());
//        finish();
//    }

    /**
     * A simple method that sets the screen to fullscreen.  It removes the Notifications bar,
     * the Actionbar and the virtual keys (if they are on the phone)
     */
    public void makeItFullScreen() {
        currentApiVersion = android.os.Build.VERSION.SDK_INT;
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_IMMERSIVE;
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {

        // Haptic Feedback
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
        TextView input = home_button_view.findViewById(R.id.input);

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

                if (!(string.equals(""))) {
                    int lastIndex = string.length() - 1;
                    string = string.substring(0, lastIndex);
                    input.setText(string);
                }
                break;

            case R.id.btn_ok:
                // load the password
                SharedPreferences settings = getSharedPreferences("PREPS", 0);
                String password = settings.getString("password", "");
                String destroy_password = settings.getString("destroy_pw", "");

                String text = input.getText().toString();

                if (text.equals(password)) {
                    // password correct, close overlay window

                    finishAffinity();
                    wm.removeView(home_button_view);


                } else if (text.equals(destroy_password)) {
                    // DO DESTROY MAGIC HERE
                    Toast.makeText(this.home_button_view.getContext(), "DESTROYING!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Wrong password!", Toast.LENGTH_SHORT).show();
                    input.setText("");
                }
                break;

            case R.id.btn_emergency:
                // do your code
                break;

            default:
                break;
        }

    }

    @Override
    public boolean onLongClick(View v) {
        TextView input = home_button_view.findViewById(R.id.input);


        switch (v.getId()) {

            case R.id.btn_delete:
                    input.setText("");
                return true;

            default:
                return false;

        }
    }

}
