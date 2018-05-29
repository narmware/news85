package com.narmware.realpic.support;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by rohitsavant on 22/05/18.
 */

public class SharedPreferenceHelper {
    private static String LAST_NEWS_ID = "news_id";
    private static String IS_LOGIN = "login";


    public static void setLatestNewsId(int id, Context context) {
        SharedPreferences manager = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = manager.edit();
        editor.putInt(LAST_NEWS_ID, id);
        editor.commit();
    }

    public static int getLatestNewsId(Context context) {
        SharedPreferences manager = PreferenceManager.getDefaultSharedPreferences(context);
        return manager.getInt(LAST_NEWS_ID, 0);
    }


    public static void setIsLogin(boolean login, Context context) {
        SharedPreferences manager = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = manager.edit();
        editor.putBoolean(IS_LOGIN, login);
        editor.commit();
    }

    public static boolean getIsLogin(Context context) {
        SharedPreferences manager = PreferenceManager.getDefaultSharedPreferences(context);
        return manager.getBoolean(IS_LOGIN, false);
    }
}
