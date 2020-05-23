package com.subcode.pin_locker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

/**
 * To start the service after boot
 */
public class autostart extends BroadcastReceiver {
    public static boolean service_is_running = false;

    public void onReceive(Context context, Intent arg1)
    {
        Intent intent = new Intent(context,LockScreenService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
            // Load service state
            SharedPreferences settings = context.getSharedPreferences("PREPS", 0);
            service_is_running = settings.getBoolean("service_is_running", false);


            if (service_is_running) {
                //If the screen was just turned on or it just booted up, start your Lock Activity
                Intent i = new Intent(context, LockscreenActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        } else {
            context.startService(intent);
        }
        Log.i("Autostart", "started");
    }
}
