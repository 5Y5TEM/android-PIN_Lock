package com.subcode.pin_locker;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import static com.subcode.pin_locker.App.Channel_ID;


/**
 * This is the Foreground Service
 * so the program runs even if the app is closed from the overview
 * - implement a Broadcastreceiver into the service that listens to Screen Off events and
 * starts the lockscreen activity if it happens
 */
public class LockScreenService extends JobIntentService {

    public static boolean service_is_running = false;

    /**
     * We have to override this method.
     * But as our Service is not bound, we return null
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private final BroadcastReceiver breceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // Load service state
            SharedPreferences settings = context.getSharedPreferences("PREPS", 0);
            service_is_running = settings.getBoolean("service_is_running", false);


            if (service_is_running) {
                //If the screen was just turned on or it just booted up, start your Lock Activity
                if (action.equals(Intent.ACTION_SCREEN_OFF) || action.equals(Intent.ACTION_BOOT_COMPLETED)) {
                    Intent i = new Intent(context, LockscreenActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(i);

                }
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startID){

        // Load service state
        SharedPreferences settings = getSharedPreferences("PREPS", 0);
        service_is_running = settings.getBoolean("service_is_running", false);
        // Set service is running flag
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("service_is_running", true);
        editor.apply();

//        String input = intent.getStringExtra("inputExtra");

            Intent notificationIntent = new Intent(this, MainActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

            Notification notification = new NotificationCompat.Builder(this, Channel_ID)
                    .setContentTitle("Lockscreen")
                    .setContentText("active")
                    .setSmallIcon(R.drawable.lock_icon)
                    .build();

            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Activity.KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
            lock.disableKeyguard();


            //Start listening for the Screen On, Screen Off, and Boot completed actions
            IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_BOOT_COMPLETED);

            //Set up a receiver to listen for the Intents in this Service
             registerReceiver(breceiver, filter);


        startForeground(1, notification);
            return START_STICKY;

    }

    @Override
    @SuppressWarnings("deprecation")
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(breceiver);

        SharedPreferences settings = getSharedPreferences("PREPS", 0);

        // Set service is running flag
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("service_is_running", false);
        editor.apply();

        super.onDestroy();
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

    }
}
