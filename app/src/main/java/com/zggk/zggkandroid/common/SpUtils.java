package com.zggk.zggkandroid.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.zggk.zggkandroid.MainApplication;

/**
 * Created by Aaron on 16/3/24.
 */
public class SpUtils {
    public static SharedPreferences getSharedPreferences(){
        SharedPreferences sharedPreferences = MainApplication.mContext.getSharedPreferences("zggk", Context.MODE_PRIVATE);
        return sharedPreferences;
    }
    public static  void putString(String key,String value){
        getSharedPreferences().edit().putString(key,value).commit();

    }
    public static  String getString(String key,String defValue){
        return  getSharedPreferences().getString(key,defValue);
    }

    public static  void putFloat(String key,float value){
        getSharedPreferences().edit().putFloat(key, value).commit();

    }
    public static  float getFloat(String key,float defValue){
        return  getSharedPreferences().getFloat(key, defValue);
    }

    public static  void putInt(String key,int value){
        getSharedPreferences().edit().putInt(key, value).commit();

    }
    public static  void putBoolean(String key,boolean value){
        getSharedPreferences().edit().putBoolean(key, value).commit();

    }
    public static  boolean getBoolean(String key,boolean defaultValue){
        return getSharedPreferences().getBoolean(key, defaultValue);

    }
    public static  int getInt(String key,int defValue){
        return  getSharedPreferences().getInt(key, defValue);
    }

    public static  void putLong(String key,long value){
        getSharedPreferences().edit().putLong(key, value).commit();

    }
    public static  long getLong(String key,long defValue){
        return  getSharedPreferences().getLong(key, defValue);
    }
}
