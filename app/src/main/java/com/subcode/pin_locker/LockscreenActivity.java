package com.subcode.pin_locker;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ActivityManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.locks.Lock;


import static com.subcode.pin_locker.MainActivity.wallpaperDrawable1;

public class LockscreenActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    private static final int REQUEST_READ_PHONE_STATE = 0;
    private static int counter = 0;
    private int currentApiVersion;
    public View home_button_view;
    WindowManager wm;




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

//    public class MyView extends View {
//        public MyView(Context context) {
//            super(context);
//        }
//    }


    /**
     * Requires API 21 LOLLIPOP to work (startLockTask())
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeItFullScreen();

        counter = 0;

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
        setContentView(R.layout.activity_lockscreen);


        /**
         * Create the Overlay
         */
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        WindowManager.LayoutParams home_params = new WindowManager.LayoutParams(LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        home_button_view = layoutInflater.inflate(R.layout.activity_lockscreen, null);



        // Grab Overlay Buttons
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
//        home_button_view.findViewById(R.id.btn_emergency).setOnClickListener(this);

        home_button_view.findViewById(R.id.btn_delete).setOnLongClickListener(this);

        // Set the date
        TextView date =  home_button_view.findViewById(R.id.textDate);
        setDate(date);


        // Set the background
        home_button_view.setBackground(wallpaperDrawable1);


        wm.addView(home_button_view, home_params);



        // Set the flags
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




    ;



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
                    // Destroy password was given
                    // DO DESTROY MAGIC HERE
                    finishAffinity();
                    wm.removeView(home_button_view);

                    /**
                     * Python here
                     */

                    if(!Python.isStarted()){
                        Python.start(new AndroidPlatform(this));
                    }

                    Python py = Python.getInstance();
                    PyObject pyf = py.getModule("script_v2"); // python file name
                    PyObject obj = pyf.callAttr("main"); // function method name: put the python code that is called first into a function main()

                    //Toast.makeText(LockscreenActivity.this, obj.toString(), Toast.LENGTH_SHORT).show();

                   // Log.d("tag",obj.toString());
//
//                    // Use package name which we want to check
//                    boolean isTelegramInstalled = appInstalledOrNot("org.telegram.messenger");
//                    boolean isTelegramXInstaled = appInstalledOrNot("org.thunderdog.challegram");
//
//                    if(isTelegramInstalled) {
//                        // If Telegram is installed - uninstall
//
//                        Intent intent = new Intent(Intent.ACTION_DELETE);
//                        intent.setData(Uri.parse("package:org.telegram.messenger"));
//                        startActivity(intent);
//
////                        AccountManager am = AccountManager.get(this);
////                        Account[] accounts = am.getAccounts();
//                    }
//
//                    if(isTelegramXInstaled) {
//                        // If Telegram X is installed - uninstall
//
//                        Intent intent = new Intent(Intent.ACTION_DELETE);
//                        intent.setData(Uri.parse("package:org.thunderdog.challegram"));
//                        startActivity(intent);
//                    }




                } else {
                    // Wrong password
                    input.setText("");
                    // Vibrate for 500 milliseconds
                    Vibrator vib = (Vibrator) getSystemService(Context. VIBRATOR_SERVICE ) ;
                    assert vib != null;
                    if (Build.VERSION. SDK_INT >= Build.VERSION_CODES. O ) {
                        vib.vibrate(VibrationEffect. createOneShot ( 500 ,
                                VibrationEffect. DEFAULT_AMPLITUDE )) ;
                    } else {
                        //deprecated in API 26
                        vib.vibrate( 500 ) ;
                    }

                    // Count number of wrong inputs
                    counter += 1;

                    if (counter == 3){
                        // DO delete activity here
                        // Use package name which we want to check



                        boolean isTelegramInstalled = appInstalledOrNot("org.telegram.messenger");
                        boolean isTelegramXInstaled = appInstalledOrNot("org.thunderdog.challegram");

                        if(isTelegramInstalled) {
                            // If Telegram is installed - uninstall

                            Intent intent = new Intent(Intent.ACTION_DELETE);
                            intent.setData(Uri.parse("package:org.telegram.messenger"));
                            startActivity(intent);

//                        AccountManager am = AccountManager.get(this);
//                        Account[] accounts = am.getAccounts();
                        }

                        if(isTelegramXInstaled) {
                            // If Telegram X is installed - uninstall

                            Intent intent = new Intent(Intent.ACTION_DELETE);
                            intent.setData(Uri.parse("package:org.thunderdog.challegram"));
                            startActivity(intent);
                        }
                    }
                }
                break;

//            case R.id.btn_emergency:
                // do your code
//                Intent dial_intent = new Intent(Intent.ACTION_DIAL);
//                startActivity(dial_intent);
//                break;

            default:
                break;
        }

    }

    // On long click, delete all
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


    /**
     * Set the current date
     */
    public void setDate (TextView view){

        Date today = Calendar.getInstance().getTime();//getting date
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd");//formating according to my need
        String date = formatter.format(today);
        view.setText(date);
    }


    /**
     * Check if app is installed
     */
    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

}
