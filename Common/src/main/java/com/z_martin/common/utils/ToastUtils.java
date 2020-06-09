package com.z_martin.common.utils;

import android.content.Context;
import android.widget.Toast;


public class ToastUtils {
    private static Toast mToast = null;

    public static void showToast(Context context, int resourcesId) {
        showToast(context.getResources().getString(resourcesId));
    }

    public static void showToast(String text) {
        showToast(text, Toast.LENGTH_SHORT);
    }

    public static void showToast(String text, int duration) {
        showToast(AppUtils.getContext(), text, duration);
    }

    public static void showToast(final Context context, final String text, final int duration) {
        if(!StringUtils.isEmpty(text)) {
            AppUtils.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    if (mToast == null) {
                        mToast = Toast.makeText(context, text, duration);
                    } else {
                        mToast.cancel();
                        mToast = Toast.makeText(context, text, duration);
                        mToast.setDuration(duration);
                    }
                    mToast.show();
                }
            });
        }
    }
}
