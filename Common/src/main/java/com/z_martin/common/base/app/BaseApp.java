package com.z_martin.common.base.app;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.bumptech.glide.Glide;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.vise.log.ViseLog;
import com.vise.log.inner.LogcatTree;
import com.z_martin.common.BuildConfig;

public class BaseApp extends Application {
    protected static Context context;
    protected static Handler handler;
    protected static int mainThreadId;
    private static BaseApp mApp;

    private RefWatcher refWatcher;
    @Override
    public void onCreate() {
        super.onCreate();
        initViseLog();
        context = getApplicationContext();
        handler = new Handler();
        mainThreadId = android.os.Process.myTid();
    }
    
    private void initViseLog() {
        ViseLog.getLogConfig()
//                .configAllowLog(BuildConfig.IS_SHOW_LOG)
                .configAllowLog(true)
                .configShowBorders(true)
                .configTagPrefix("ystn_log")
                .configFormatTag("%d{HH:mm:ss:SSS} %t %c{-5}")
                .configLevel(Log.VERBOSE);
        ViseLog.plant(new LogcatTree());
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            Glide.get(this).clearMemory();
        }
        Glide.get(this).trimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }


    private RefWatcher setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return RefWatcher.DISABLED;
        }
        return LeakCanary.install(this);
    }

    public static RefWatcher getRefWatcher(Context context) {
        BaseApp leakApplication = (BaseApp) context.getApplicationContext();
        return leakApplication.refWatcher;
    }

    public static Context getContext() {
        return context;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static int getMainThreadId() {
        return mainThreadId;
    }
}
