package com.narmware.realpic;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by rohitsavant on 23/05/18.
 */

public class MyApplication extends MultiDexApplication {

    public static void mt(String text, Context c) {
        Toast.makeText(c, text, Toast.LENGTH_SHORT).show();
    }

}
