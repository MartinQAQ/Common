package com.z_martin.common.base.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;

import com.vise.log.ViseLog;
import com.z_martin.common.R;
import com.z_martin.common.base.annotations.ContentView;
import com.z_martin.common.base.annotations.DialogGravity;
import com.z_martin.common.base.annotations.DialogHeight;
import com.z_martin.common.base.annotations.DialogWidth;
import com.z_martin.common.base.annotations.ResId;
import com.z_martin.common.widgets.FastBaseAdapter;

import java.lang.reflect.Method;

public class InjectManager {

    public static void inject(Activity activity) {
        injectLayout(activity);
    }
    
    public static int inject(Fragment fragment) {
        return injectLayout(fragment);
    }

    public static int inject(FastBaseAdapter adapter) {
       return injectLayout(adapter);
    }

    public static void inject(Dialog dialog, Context context) {
        injectLayoutDialog(dialog, context);
    }

    private static void injectLayout(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
       
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        if(contentView != null) {
            int layoutId = contentView.value();
            if (layoutId == ResId.DEFAULT_VALUE) {
                log(clazz);
                throw new RuntimeException(getClassName(clazz) + activity.getString(R.string.layout_id_error_activity));
            }
            if (layoutId == -2) {
                
            } else {
                activity.setContentView(layoutId);
//                try {
//                    Method method = clazz.getMethod("setContentView", int.class);
//                    method.invoke(activity, layoutId);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        } else {
            log(clazz);
            throw new NullPointerException(getClassName(clazz) + activity.getString(R.string.content_view_empty_activity));
        }
    }

    private static int injectLayout(Fragment fragment) {
        Class<? extends Fragment> clazz = fragment.getClass();

        ContentView contentView = clazz.getAnnotation(ContentView.class);
        if(contentView != null) {
            int layoutId = contentView.value();
            if (layoutId == ResId.DEFAULT_VALUE) {
                log(clazz);
                throw new RuntimeException(getClassName(clazz) + fragment.getString(R.string.layout_id_error_fragment));
            }
            return layoutId;
        } else {
            log(clazz);
            throw new NullPointerException(getClassName(clazz) + fragment.getString(R.string.content_view_empty_activity));
        }
    }
    private static int injectLayout(FastBaseAdapter adapter) {
        Class<? extends FastBaseAdapter> clazz = adapter.getClass();

        ContentView contentView = clazz.getAnnotation(ContentView.class);
        if(contentView != null) {
            int layoutId = contentView.value();
            if (layoutId == ResId.DEFAULT_VALUE) {
                log(clazz);
                ViseLog.e(R.string.layout_id_error_activity);
                throw new RuntimeException(getClassName(clazz) + BaseApp.getContext().getString(R.string.layout_id_error_adapter));
            }
            return layoutId;
//            fragment.setLayoutId(layoutId);
        } else {
            log(clazz);
            ViseLog.e(R.string.layout_id_error_activity);
            throw new NullPointerException(getClassName(clazz) + BaseApp.getContext().getString(R.string.content_view_empty_adapter));
        }
    }


    private static void injectLayoutDialog(Dialog dialog, Context context) {
        Class<? extends Dialog> clazz = dialog.getClass();
       
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        if(contentView != null) {
            int layoutId = contentView.value();
            if (layoutId == ResId.DEFAULT_VALUE) {
                log(clazz);
                throw new RuntimeException(getClassName(clazz) + context.getString(R.string.dialog_layout_id_error));
            }
            // activity.setContentView(layoutId);

            try {
                Method method = clazz.getMethod("setContentView", int.class);
                method.invoke(dialog, layoutId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            log(clazz);
            throw new NullPointerException(getClassName(clazz) + context.getString(R.string.dialog_content_view_empty));
        }

        DialogHeight dialogHeight = clazz.getAnnotation(DialogHeight.class);
        Window dialogWindow = dialog.getWindow();
        assert dialogWindow != null;
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); 
        lp.height = d.heightPixels;
        lp.width = d.widthPixels;
        if(null != dialogHeight) {
            double height = dialogHeight.value();
            lp.height = (int) (d.heightPixels * height);
        }
        DialogWidth dialogWidth = clazz.getAnnotation(DialogWidth.class);
        if(null != dialogWidth) {
            double width = dialogWidth.value();
            lp.width = (int) (d.widthPixels * width); 
        }

        DialogGravity dialogGravity = clazz.getAnnotation(DialogGravity.class);
        if(null != dialogGravity) {
            int gravity = dialogGravity.value();
            if (gravity == Gravity.TOP || gravity == Gravity.BOTTOM || gravity == Gravity.LEFT || gravity == Gravity.RIGHT || gravity == Gravity.CENTER) {
                dialogWindow.getAttributes().gravity = dialogGravity.value(); 
            } else {
                throw new RuntimeException(getClassName(clazz) + context.getString(R.string.dialog_gravity_error));
            }
        }
        
        dialogWindow.setAttributes(lp);
    }

    private static void log(Class<?> clazz) {
//        StackTraceElement[] s = Thread.currentThread().getStackTrace();
//        for (StackTraceElement value : s) {
//            if (value.getClassName().startsWith("lambda")) {
//                return;
//            }
//        }
        String TAG = "Error Activity" + "(" + getClassName(clazz) + ".java:" + 1 + ")";
        Log.d(TAG, "\t");
    }
    
    private static String getClassName(Class clazz) {
        String className = clazz.getName();
        if (className.contains("$")) { 
            className = className.substring(className.lastIndexOf(".") + 1, className.indexOf("$"));
        } else {
            className = className.substring(className.lastIndexOf(".") + 1, className.length());
        }
        return className;
    }
}
