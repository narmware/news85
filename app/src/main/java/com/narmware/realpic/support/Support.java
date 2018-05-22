package com.narmware.realpic.support;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by rohitsavant on 20/05/18.
 */

public class Support {

    public static final String NEWS_TYPE_IMAGE = "0";
    public static final String NEWS_TYPE_YOUTUBE = "1";

    public static final String VIDEO_ID = "VIDEO_ID";
    public static final String NEWS_URL = "news_url";

    public static void mt(String text, Context context) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
