package com.subcode.pin_locker;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
import static android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
import static android.view.WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
import static android.view.WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
import static com.subcode.pin_locker.MainActivity.width;
import static com.subcode.pin_locker.MainActivity.height;


/**
 * Created by shaobin on 2014/3/22.
 */
public class HomeKeyLocker {
    private OverlayDialog mOverlayDialog;

    public void lock(Activity activity) {
        if (mOverlayDialog == null) {
            mOverlayDialog = new OverlayDialog(activity);
            mOverlayDialog.show();
        }
    }

    public void unlock() {
        if (mOverlayDialog != null) {
            mOverlayDialog.dismiss();
            mOverlayDialog = null;
        }
    }

    private static class OverlayDialog extends AlertDialog {

        public OverlayDialog(Activity activity) {
            super(activity, R.style.OverlayDialog);

            WindowManager.LayoutParams params = getWindow().getAttributes();
//            params.type = TYPE_SYSTEM_ERROR;
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            params.dimAmount = 0.0F; // transparent
            params.width = 100;
            params.height = 100;
//            params.gravity = Gravity.BOTTOM;
            getWindow().setAttributes(params);
//            getWindow().setFlags(FLAG_SHOW_WHEN_LOCKED | FLAG_NOT_TOUCH_MODAL, 0xffffff);
            getWindow().setFlags(FLAG_NOT_TOUCH_MODAL, 0xffffff);

            setOwnerActivity(activity);
            setCancelable(false);

        }

        public final boolean dispatchTouchEvent(MotionEvent motionevent) {
            return true;
        }

        protected final void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            FrameLayout framelayout = new FrameLayout(getContext());
            framelayout.setBackgroundColor(0);
            setContentView(framelayout);
        }
    }
}
