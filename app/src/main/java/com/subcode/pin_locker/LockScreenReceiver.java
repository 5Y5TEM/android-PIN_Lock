package com.subcode.pin_locker;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.WindowManager;

//public class LockScreenReceiver extends BroadcastReceiver {
//    public static boolean service_is_running;
//    @SuppressLint("WrongConstant")
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        String action = intent.getAction();

        // Load service state
//        SharedPreferences settings = context.getSharedPreferences("PREPS", 0);
//        service_is_running = settings.getBoolean("service_is_running", false);


//        if (service_is_running) {
            //If the screen was just turned on or it just booted up, start your Lock Activity
//            if (action.equals(Intent.ACTION_SCREEN_OFF) || action.equals(Intent.ACTION_BOOT_COMPLETED)) {
//                Intent i = new Intent(context, LockscreenActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                i.addFlags(WindowManager.LayoutParams.TYPE_SYSTEM_ERROR);
//                context.startActivity(i);

//            }
//        }
//    }

//}