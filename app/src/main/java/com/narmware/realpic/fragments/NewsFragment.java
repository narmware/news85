package com.narmware.realpic.fragments;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alexvasilkov.foldablelayout.FoldableListLayout;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.narmware.realpic.R;
import com.narmware.realpic.activity.HomeActivity;
import com.narmware.realpic.apdapter.HomeNewsAdapter;
import com.narmware.realpic.pojo.HomeNews;
import com.narmware.realpic.pojo.HomePojoResponse;
import com.narmware.realpic.support.EndPoint;
import com.narmware.realpic.support.SharedPreferenceHelper;
import com.narmware.realpic.support.Support;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.empty_linear)protected LinearLayout mLinearEmpty;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mRoot;
   protected FoldableListLayout mNewsList;
    ArrayList<HomeNews> homeNewsPojos = new ArrayList<>();

    private OnFragmentInteractionListener mListener;
    private HomeNewsAdapter mHomeNewsAdapter;
    private RequestQueue mQueue;

    protected Dialog mNoConnectionDialog;
    private AdView mAdView;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRoot = inflater.inflate(R.layout.fragment_news, container, false);
        mNewsList = mRoot.findViewById(R.id.foldable_list);
        mNewsList.setOnFoldRotationListener(new FoldableListLayout.OnFoldRotationListener() {
            @Override
            public void onFoldRotation(float rotation, boolean isFromUser) {
                Log.d("rotation", Float.toString(rotation) + " " + isFromUser);

                Log.d("PositionTop"," Position " + mNewsList.getPosition()) ;

                if (rotation % 180 == 0) {
                    try {
                        int position = mNewsList.getPosition();
                        int count = mNewsList.getCount();

                        Log.d("cnt_pos", "Count " + count + " Position " + position);
                        if (position == count - 5) {
                            int id = SharedPreferenceHelper.getLatestNewsId(getActivity());
                            Log.d("Shared id", id + " ");
                            fetchNews(id);
                            //Support.mt("loaded " + id, getActivity());
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else {
                    //TODO
                }
            }
        });

        fetchNews(0);

        init(mRoot);
        return mRoot;
    }

    private void init(View view) {
        ButterKnife.bind(this,view);
        mAdView = (AdView) view.findViewById(R.id.adView);
        setAds();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /*
    @params id Id of the latest news item
     */
    public void fetchNews(final int id)
    {
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(getContext());
        }

        String url= EndPoint.NEWS_URL+"?id="+id;
        Log.e("url",url);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>(){



                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.d("urlr", response.toString());

                            Gson gson = new Gson();
                            HomePojoResponse pojo1 = gson.fromJson(response.toString(), HomePojoResponse.class);

                            final HomeNews[] array = pojo1.getData();
                            for (HomeNews item : array) {
                                // String ImgId=item.getId();

                                homeNewsPojos.add(item);
                                Log.d("info", item.getId());
                                Log.d("details ", item.getImage_url());

                            }

                            mHomeNewsAdapter = new HomeNewsAdapter(homeNewsPojos, getActivity());
                            mNewsList.setAdapter(mHomeNewsAdapter);
                            mHomeNewsAdapter.notifyDataSetChanged();

                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }


                },new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("Details", error.toString());

                        if(homeNewsPojos.size()==0)
                        {
                            mLinearEmpty.setVisibility(View.VISIBLE);
                        }
                        showNoConnectionDialog(id);
                    }
                });

        mQueue.add(jsObjRequest);

    }

    private void showNoConnectionDialog(final int id) {
        mNoConnectionDialog = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        mNoConnectionDialog.setContentView(R.layout.dialog_noconnectivity);
        mNoConnectionDialog.setCancelable(false);
        mNoConnectionDialog.show();

        Button exit = mNoConnectionDialog.findViewById(R.id.dialog_no_connec_exit);
        Button tryAgain = mNoConnectionDialog.findViewById(R.id.dialog_no_connec_try_again);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchNews(id);
                mNoConnectionDialog.dismiss();
            }
        });
    }

    public void setAds()
    {

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the LogCat to get your test device ID
                .addTestDevice("C04B1BFFB0774708339BC273F8A43708")
                .build();

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdClosed() {
                Toast.makeText(getContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Toast.makeText(getContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                Toast.makeText(getContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });

        mAdView.loadAd(adRequest);
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
