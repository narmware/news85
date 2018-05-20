package com.narmware.realpic.support;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by rohitsavant on 20/05/18.
 */

public class Support {

    public static void mt(String text, Context context) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
