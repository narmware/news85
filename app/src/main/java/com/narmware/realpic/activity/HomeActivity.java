package com.narmware.realpic.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.narmware.realpic.R;
import com.narmware.realpic.fragments.AboutFragment;
import com.narmware.realpic.fragments.NewsFragment;
import com.narmware.realpic.support.Support;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.yarolegovich.slidingrootnav.callback.DragStateListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements DragStateListener, NewsFragment.OnFragmentInteractionListener,
        AboutFragment.OnFragmentInteractionListener{

    private SlidingRootNav slidingRootNav;
    private FragmentManager mFragmentManager;
    protected Button test;
    protected Button test2;

    private void init() {
      test = findViewById(R.id.test);
      test.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              FragmentTransaction transaction = mFragmentManager.beginTransaction();
              transaction.replace(R.id.fragment_container, NewsFragment.newInstance(null, null));
              transaction.commit();
              slidingRootNav.closeMenu();
          }
      });

        test2 = findViewById(R.id.test2);
        test2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Support.mt("About", HomeActivity.this);
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, AboutFragment.newInstance(null, null));
                transaction.commit();
                slidingRootNav.closeMenu();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

    @Override
    public void onDragStart() {

    }

    @Override
    public void onDragEnd(boolean isMenuOpened) {
        Support.mt("Menu staus : " + isMenuOpened, HomeActivity.this);
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
