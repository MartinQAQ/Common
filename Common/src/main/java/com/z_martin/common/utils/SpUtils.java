package com.z_martin.common.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class SpUtils {
    private static SharedPreferences sp;
    private static String mPreferencesName = "share_preference_default";

    private void setPreferencesName(String preferencesName) {
        mPreferencesName = preferencesName;
    }

    public static void putBoolean(Context ctx, String key, boolean value) {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = ctx.getSharedPreferences(mPreferencesName, Context
                    .MODE_PRIVATE);
        }
        sp.edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(Context ctx, String key, boolean defValue) {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = ctx.getSharedPreferences(mPreferencesName, Context
                    .MODE_PRIVATE);
        }
        return sp.getBoolean(key, defValue);
    }

    public static void putString(Context ctx, String key, String value) {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = ctx.getSharedPreferences(mPreferencesName, Context
                    .MODE_PRIVATE);
        }
        sp.edit().putString(key, value).apply();
    }

    public static String getString(Context ctx, String key, String defValue) {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = ctx.getSharedPreferences(mPreferencesName, Context
                    .MODE_PRIVATE);
        }
        return sp.getString(key, defValue);
    }


    public static void putInt(Context ctx, String key, int value) {
        if (sp == null) {
            sp = ctx.getSharedPreferences(mPreferencesName, Context
                    .MODE_PRIVATE);
        }
        sp.edit().putInt(key, value).apply();
    }

    public static int getInt(Context ctx, String key, int defValue) {
        if (sp == null) {
            sp = ctx.getSharedPreferences(mPreferencesName, Context
                    .MODE_PRIVATE);
        }
        return sp.getInt(key, defValue);
    }


    public static void remove(Context ctx, String key) {
        if (sp == null) {
            sp = ctx.getSharedPreferences(mPreferencesName, Context
                    .MODE_PRIVATE);
        }
        sp.edit().remove(key).apply();
    }

    public static <T> void setDataList(String key, List<T> datalist) {
        if (null == datalist || datalist.size() <= 0)
            return;

        Gson gson = new Gson();
        String strJson = gson.toJson(datalist);
        SpUtils.putString(AppUtils.getContext(), key, strJson);
    }

    public static <T> List<T> getDataList(String key, Class<T> cls) {
        List<T> datalist = new ArrayList<T>();
        String strJson = SpUtils.getString(AppUtils.getContext(), key, null);

        if (null == strJson) {
            return datalist;
        }

        try {
            Gson gson = new Gson();
            JsonArray array = new JsonParser().parse(strJson).getAsJsonArray();
            for (final JsonElement elem : array) {
                datalist.add(gson.fromJson(elem, cls));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return datalist;
    }
    
    public static <T> void setDataEntity(String key, T data) {
        if (null == data)
            return;

        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(data);
        SpUtils.putString(AppUtils.getContext(), key, strJson);
    }
    
    public static <T> T getDataEntity(String key, Class<T> cls) {
        String strJson = SpUtils.getString(AppUtils.getContext(), key, null);
        if (null == strJson) {
            return null;
        }
        try {
            Gson gson = new Gson();
            JsonObject obj = new JsonParser().parse(strJson).getAsJsonObject();
                return gson.fromJson(obj, cls);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

   

    public static int getThemeIndex(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt("ThemeIndex", 5);
    }

    public static void setThemeIndex(Context context, int index) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putInt("ThemeIndex", index).apply();
    }

    public static boolean getNightModel(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("pNightMode", false);
    }

    public static void setNightModel(Context context, boolean nightModel) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean("pNightMode", nightModel).apply();
    }
}
