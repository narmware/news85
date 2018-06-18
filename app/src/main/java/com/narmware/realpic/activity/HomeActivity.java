package com.narmware.realpic.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.narmware.realpic.fragments.SingleNewsFragment;
import com.narmware.realpic.fragments.WebviewFragment;
import com.narmware.realpic.support.EndPoint;
import com.narmware.realpic.support.Support;
import com.squareup.picasso.Picasso;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.yarolegovich.slidingrootnav.callback.DragStateListener;

import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements DragStateListener, NewsFragment.OnFragmentInteractionListener,
        AboutFragment.OnFragmentInteractionListener, WebviewFragment.OnFragmentInteractionListener,SingleNewsFragment.OnFragmentInteractionListener{

    private SlidingRootNav slidingRootNav;
    private FragmentManager mFragmentManager;
    protected Button mBtnNavNews;
    protected Button mBtnNavAbout;
    protected Button mBtnNavPrivacy;
    protected ImageView mBackImage;
    private FirebaseAnalytics mFirebaseAnalytics;    //   @BindView(R.id.fab)protected FloatingActionButton mFabShare;

    private void init() {
        ButterKnife.bind(this);
        mBackImage = findViewById(R.id.menu_background);
        subscribeToPushService();

        Picasso.with(this)
                .load(Support.MENU_BACKGROUND_URL)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.menu_bg)
                .into(mBackImage);

        mBtnNavNews = findViewById(R.id.btn_nav_news);
        mBtnNavNews.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              FragmentTransaction transaction = mFragmentManager.beginTransaction();
              transaction.replace(R.id.fragment_container, NewsFragment.newInstance(null, null));
              transaction.commit();
              slidingRootNav.closeMenu();
          }
      });

        mBtnNavAbout = findViewById(R.id.btn_nav_about);
        mBtnNavAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Support.mt("About", HomeActivity.this);
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, WebviewFragment.newInstance(EndPoint.ABOUT_US_URL, null));
                transaction.commit();
                slidingRootNav.closeMenu();
            }
        });

        mBtnNavPrivacy = findViewById(R.id.btn_nav_privacy);
        mBtnNavPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Support.mt("About", HomeActivity.this);
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, WebviewFragment.newInstance(EndPoint.PRIVACY_POLICY_URL, null));
                transaction.commit();
                slidingRootNav.closeMenu();
            }
        });

        /*mFabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareBody = "Hello,this is share msg";
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent,"Share Using"));
            }
        });*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        // Obtain the shared Tracker instance.
        MyApplication application = (MyApplication) getApplication();

        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.fragment_container, NewsFragment.newInstance(null, null));
        transaction.commit();

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_layout)
                .addDragStateListener(this)
                .inject();
        init();

    }

    private void subscribeToPushService() {
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        Log.d("AndroidBash", "Subscribed");
        String token = FirebaseInstanceId.getInstance().getToken();
        // Log and toast
   //     Log.d("AndroidBash", token);
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
        //Support.mt("Menu staus : " + isMenuOpened, HomeActivity.this);
       /* if(isMenuOpened == true ) {
            test.setVisibility(View.VISIBLE);
        }
        else {
            test.setVisibility(View.INVISIBLE);
        }*/
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
