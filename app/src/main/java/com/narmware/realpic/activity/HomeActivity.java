package com.narmware.realpic.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.narmware.realpic.MyApplication;
import com.narmware.realpic.R;
import com.narmware.realpic.fragments.AboutFragment;
import com.narmware.realpic.fragments.NewsFragment;
import com.narmware.realpic.fragments.SettingsFragment;
import com.narmware.realpic.fragments.SingleNewsFragment;
import com.narmware.realpic.fragments.WebviewFragment;
import com.narmware.realpic.support.EndPoint;
import com.narmware.realpic.support.ScreenshotHelper;
import com.narmware.realpic.support.Support;
import com.squareup.picasso.Picasso;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.yarolegovich.slidingrootnav.callback.DragStateListener;

import java.io.File;

import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements DragStateListener, NewsFragment.OnFragmentInteractionListener,
        AboutFragment.OnFragmentInteractionListener, WebviewFragment.OnFragmentInteractionListener,
        SingleNewsFragment.OnFragmentInteractionListener,SettingsFragment.OnFragmentInteractionListener{

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private FirebaseAnalytics mFirebaseAnalytics;
    BottomNavigationView navigation;

    private void init() {
        ButterKnife.bind(this);
        subscribeToPushService();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    setFragment(new NewsFragment());
                    return true;

                case R.id.nav_share:

                    if(navigation.getSelectedItemId()==R.id.nav_home)
                    {
                        String bottom = "आत्ता बातम्या वाचण्याचा एक नवीन अंदाज... ८५ शब्दामध्ये आपल्या BOL 85 ऍप वर... त्वरीत Download करा\n\n" +
                                "https://goo.gl/FLD5wW";
                        String shareText =  bottom;

                        File path = ScreenshotHelper.takeScreenshot(HomeActivity.this);
                        Intent share = new Intent(Intent.ACTION_SEND_MULTIPLE );

                        // If you want to share a png image only, you can do:
                        // setType("image/png"); OR for jpeg: setType("image/jpeg");
                        share.setType("image*//*");

                        Uri uri = Uri.fromFile(path);
                       // share.putExtra(Intent.EXTRA_STREAM, uri);
                        share.putExtra(Intent.EXTRA_TEXT,shareText);

                        startActivity(Intent.createChooser(share, "Share Now"));
                    }

                    return true;

                case R.id.nav_about:
                    //setFragment(WebviewFragment.newInstance(EndPoint.ABOUT_US_URL, null));
                    setFragment(new SettingsFragment());
                    return true;
            }
            return false;
        }
    };

    public void setFragment(Fragment fragment)
    {
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        // Obtain the shared Tracker instance.
        MyApplication application = (MyApplication) getApplication();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setFragment(new NewsFragment());

        init();

    }

    private void subscribeToPushService() {
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        Log.d("AndroidBash", "Subscribed");
        String token = FirebaseInstanceId.getInstance().getToken();

    }


    @Override
    protected void onResume() {
        super.onResume();
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "0");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Activity ON");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "0");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Activity OFF");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    @Override
    public void onDragStart() {

    }

    @Override
    public void onDragEnd(boolean isMenuOpened) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
