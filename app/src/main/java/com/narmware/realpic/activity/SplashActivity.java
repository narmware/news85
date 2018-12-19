package com.narmware.realpic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.narmware.realpic.R;
import com.narmware.realpic.support.SharedPreferenceHelper;

public class SplashActivity extends AppCompatActivity {
    private static int TIMEOUT = 1400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                finish();

               /* if(SharedPreferenceHelper.getIsLogin(SplashActivity.this)==false)
                {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
                else {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();
                }*/


            }
        }, TIMEOUT);
    }

}
