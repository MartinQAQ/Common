package com.z_martin.common.base.app;

import android.app.Activity;

import com.z_martin.common.utils.AppUtils;
import com.z_martin.common.utils.SpUtils;

public class AppConfig {
    private static boolean isInit = false;
    private static String mBaseUrl;
    private static String mPhotoUrl;
    private static Class<? extends Activity> mSplashActivity;
    private static Class<? extends Activity> mTokenErrorActivity;
    private static int mTokenErrorStatus;
    private static int mTokenErrorStatus2;
    private static int mRequestSuccessStatus;
    public static final String AUTHORIZATION = "authorization";
    public static final String AUTHORIZATION_FINAL = "authorization_final";
    private static int authorizationType = 0;
    public static void init(Class<? extends Activity> splashActivity, Class<? extends Activity> tokenErrorActivity, 
                            String baseUrl, String photoUrl, int requestSuccessStatus, int tokenErrorStatus, int tokenErrorStatus2) {
        isInit = true;
        mSplashActivity = splashActivity;
        mTokenErrorActivity = tokenErrorActivity;
        mBaseUrl = baseUrl;
        mPhotoUrl = photoUrl;
        mRequestSuccessStatus = requestSuccessStatus;
        mTokenErrorStatus = tokenErrorStatus;
        mTokenErrorStatus2 = tokenErrorStatus2;
    }

    public static void setUrl(String baseUrl, String photoUrl) {
        mBaseUrl = baseUrl;
        mPhotoUrl = photoUrl;
    }

    public static void setAuthorization(String authorization) {
        SpUtils.putString(AppUtils.getContext(), AUTHORIZATION, authorization);
    }

    public static String getAuthorization() {
        return SpUtils.getString(AppUtils.getContext(), AUTHORIZATION, "");
    }


    public static void setAuthorizationFinal(String authorizationFinal) {
        SpUtils.putString(AppUtils.getContext(), AUTHORIZATION_FINAL, authorizationFinal);
    }

    public static String getAuthorizationFinal() {
        return SpUtils.getString(AppUtils.getContext(), AUTHORIZATION_FINAL, "");
    }

    public static Class getSplashActivity() {
        if(!isInit)
            throw new RuntimeException("AppConfig not init");
        return mSplashActivity;
    }
    
    public static Class getTokenErrorActivity() {
        if(!isInit)
            throw new RuntimeException("AppConfig not init");
        return mTokenErrorActivity;
    }

    public static String getBaseUrl() {
        if(!isInit)
            throw new RuntimeException("AppConfig not init");
        return mBaseUrl;
    }

    public static String getPhotoUrl() {
        if(!isInit)
            throw new RuntimeException("AppConfig not init");
        return mPhotoUrl;
    }

    public static int getTokenErrorStatus() {
        if(!isInit)
            throw new RuntimeException("AppConfig not init");
        return mTokenErrorStatus;
    }
    
    public static int getTokenErrorStatus2() {
        if(!isInit)
            throw new RuntimeException("AppConfig not init");
        return mTokenErrorStatus2;
    }

    public static int getRequestSuccessStatus() {
        if(!isInit)
            throw new RuntimeException("AppConfig not init");
        return mRequestSuccessStatus;
    }

    public static int getAuthorizationType() {
        return authorizationType;
    }

    public static void setAuthorizationType(int authorizationType) {
        AppConfig.authorizationType = authorizationType;
    }
}
