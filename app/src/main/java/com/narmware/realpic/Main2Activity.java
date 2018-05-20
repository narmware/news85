package com.narmware.realpic;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.narmware.realpic.fragments.AboutFragment;
import com.narmware.realpic.fragments.NewsFragment;
import com.narmware.realpic.support.Support;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.yarolegovich.slidingrootnav.callback.DragListener;
import com.yarolegovich.slidingrootnav.callback.DragStateListener;

public class Main2Activity extends AppCompatActivity implements DragStateListener, NewsFragment.OnFragmentInteractionListener,
        AboutFragment.OnFragmentInteractionListener{

    private SlidingRootNav slidingRootNav;
    private FragmentManager mFragmentManager;
    private Button test, test2;

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
                Support.mt("About", Main2Activity.this);
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
        Support.mt("Menu staus : " + isMenuOpened, Main2Activity.this);
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
