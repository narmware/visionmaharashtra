package com.narmware.visionmaharashtra.support;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by comp16 on 12/19/2017.
 */

public class SharedPreferencesHelper {

    private static final String IS_LOGIN="login";
    private static final String USER_ID="user_id";
    private static final String USER_NAME="user_name";
    private static final String USER_MOBILE="user_mobile";
    private static final String USER_PHOTO_URL="user_photo_url";



    public static void setIsLogin(boolean login, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putBoolean(IS_LOGIN,login);
        edit.commit();
    }

    public static boolean getIsLogin(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        boolean login=pref.getBoolean(IS_LOGIN,false);
        return login;
    }

    public static void setUserId(String user_id, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(USER_ID,user_id);
        edit.commit();
    }

    public static String getUserId(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String user_id=pref.getString(USER_ID,null);
        return user_id;
    }

    public static void setUserName(String user_name, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(USER_NAME,user_name);
        edit.commit();
    }

    public static String getUserName(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String user_name=pref.getString(USER_NAME,null);
        return user_name;
    }

    public static void setUserMobile(String user_email, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(USER_MOBILE,user_email);
        edit.commit();
    }

    public static String getUserMobile(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String user_email=pref.getString(USER_MOBILE,null);
        return user_email;
    }

    public static void setUserPhotoUrl(String user_photo_url, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(USER_PHOTO_URL,user_photo_url);
        edit.commit();
    }

    public static String getUserPhotoUrl(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String user_photo_url=pref.getString(USER_PHOTO_URL,null);
        return user_photo_url;
    }

}

