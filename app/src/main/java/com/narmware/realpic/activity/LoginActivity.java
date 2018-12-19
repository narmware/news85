package com.narmware.realpic.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.RotateDownTransformer;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;
import com.narmware.realpic.Manifest;
import com.narmware.realpic.MyApplication;
import com.narmware.realpic.R;
import com.narmware.realpic.fragments.IntroductionFragment;
import com.narmware.realpic.pojo.Login;
import com.narmware.realpic.pojo.LoginData;
import com.narmware.realpic.support.DatabaseAccess;
import com.narmware.realpic.support.EndPoint;
import com.narmware.realpic.support.SharedPreferenceHelper;
import com.narmware.realpic.support.SupportFunctions;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,IntroductionFragment.OnFragmentInteractionListener {
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 007;
    private static final String TAG = LoginActivity.class.getSimpleName();

    String personName;
    String personPhotoUrl ;
    String email;
    DatabaseAccess databaseAccess;

    @BindView(R.id.btn_signin) protected Button mBtnSignIn;
    @BindView(R.id.intro_pager) protected ViewPager mViewPager;
    PagerAdapter mAdapter;
    RequestQueue mVolleyRequest;

    private void requestPermission() {
       /* Dexter.withActivity(this)
                .withPermissions(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE

                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {*//* ... *//*}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {*//* ... *//*}
        }).check();*/
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        requestPermission();

        init();

        mBtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void init() {
        ButterKnife.bind(this);
        mVolleyRequest = Volley.newRequestQueue(LoginActivity.this);

        databaseAccess= DatabaseAccess.getInstance(LoginActivity.this);
        databaseAccess.open();

        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .playOn(mViewPager);

        mAdapter=new PagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setPageTransformer(true, new RotateDownTransformer());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {

            switch (index) {
                case 0:
                    return new IntroductionFragment().newInstance("title",R.drawable.bol85);
                case 1:
                    return new IntroductionFragment().newInstance("title1",R.drawable.bol85);
                case 2:
                    return new IntroductionFragment().newInstance("title2",R.drawable.bol85);
            }
            return null;
        }

        @Override
        public int getCount() {
            // get item count - equal to number of tabs
            return 3;
        }
    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            try {
                personName = acct.getDisplayName();
                email = acct.getEmail();
                personPhotoUrl = acct.getPhotoUrl().toString();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            Log.e(TAG, "Name: " + personName + ", email: " + email
                    + ", Image: " + personPhotoUrl);

            if(personName==null || email==null)
            {
                Toast.makeText(this, "Unsupported format", Toast.LENGTH_SHORT).show();
            }

            else{

                if(personPhotoUrl==null)
                {
                    personPhotoUrl="http://narmware.com/kp/avatar.png";
                }
                else {

                }

                if(SharedPreferenceHelper.getIsLogin(LoginActivity.this)==false)
                {
                    databaseAccess.setUserLogin(personName, email, personPhotoUrl);
                    registerUser();
                    SharedPreferenceHelper.setIsLogin(true,LoginActivity.this);
                    SharedPreferenceHelper.setUserImage(personPhotoUrl,LoginActivity.this);
                    SharedPreferenceHelper.setUserName(email,LoginActivity.this);

                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                }

            }


        } else {
            // Signed out, show unauthenticated UI.
            SharedPreferenceHelper.setIsLogin(false,LoginActivity.this);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        }
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            //showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    //hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void registerUser() {


        Gson gson=new Gson();

        Login login=new Login();
        login.setName(personName);
        login.setEmail(email);
        login.setPhoto(personPhotoUrl);

        String json_string=gson.toJson(login);

        HashMap<String,String> param = new HashMap();
        param.put(EndPoint.JSON_STRING,json_string);

        //url with params
        String url= SupportFunctions.appendParam(EndPoint.LOGIN,param);

        //url without params
        //String url= MyApplication.GET_CATEGORIES;

        Log.e("Login url",url);
        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.GET,url,null,
                // The third parameter Listener overrides the method onResponse() and passes
                //JSONObject as a parameter
                new Response.Listener<JSONObject>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject response) {

                        try
                        {
                            //getting test master array
                            // testMasterDetails = testMasterArray.toString();

                            Log.e("Login Json_string",response.toString());
                            Gson gson = new Gson();


                        } catch (Exception e) {

                            e.printStackTrace();
                            //Toast.makeText(NavigationActivity.this, "Invalid album id", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                // The final parameter overrides the method onErrorResponse() and passes VolleyError
                //as a parameter
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Test Error");
                        // showNoConnectionDialog();

                    }
                }
        );
        mVolleyRequest.add(obreq);
    }

}
