package com.narmware.realpic.fragments;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.DepthPageTransformer;
import com.ToxicBakery.viewpager.transforms.RotateDownTransformer;
import com.ToxicBakery.viewpager.transforms.StackTransformer;
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
import com.narmware.realpic.activity.LoginActivity;
import com.narmware.realpic.apdapter.HomeNewsAdapter;
import com.narmware.realpic.pojo.HomeNews;
import com.narmware.realpic.pojo.HomePojoResponse;
import com.narmware.realpic.support.EndPoint;
import com.narmware.realpic.support.SharedPreferenceHelper;
import com.narmware.realpic.support.Support;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.avi)protected AVLoadingIndicatorView avi;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mRoot;
   //protected FoldableListLayout mNewsList;
    ArrayList<HomeNews> homeNewsPojos = new ArrayList<>();
    ViewPager newsPager;
    PagerAdapter mAdapter;

    private OnFragmentInteractionListener mListener;
    private HomeNewsAdapter mHomeNewsAdapter;
    private RequestQueue mQueue;

    protected Dialog mNoConnectionDialog;
   // private AdView mAdView;

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
        ButterKnife.bind(this,mRoot);

        newsPager = mRoot.findViewById(R.id.news_pager);

       mAdapter=new PagerAdapter(getActivity().getSupportFragmentManager(),getContext());
       newsPager.setAdapter(mAdapter);
       newsPager.setPageTransformer(true, new DepthPageTransformer());
       mAdapter.notifyDataSetChanged();
        newsPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
           @Override
           public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

           }

           @Override
           public void onPageSelected(int position) {

               if(position == homeNewsPojos.size()-1) {
                   fetchNews(Integer.parseInt(homeNewsPojos.get(position).getId()));
                   //Toast.makeText(getContext(), "Id: " + homeNewsPojos.get(position).getId(), Toast.LENGTH_SHORT).show();
               }
           }

           @Override
           public void onPageScrollStateChanged(int state) {

           }
       });

        fetchNews(0);

        init(mRoot);
        return mRoot;
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        Context context;
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public PagerAdapter(FragmentManager fm,Context mContext) {
            super(fm);
            this.context=mContext;
        }

        @Override
        public Fragment getItem(int index) {

            return mFragmentList.get(index);
        }

        @Override
        public int getCount() {
            // get item count - equal to number of tabs
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }
    }

    private void init(View view) {
        ButterKnife.bind(this,view);
        //mAdView = (AdView) view.findViewById(R.id.adView);
        //setAds();
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
        avi.show();

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
                                mAdapter.addFragment(SingleNewsFragment.newInstance(item.getTitle(),item.getDescription(),item.getImage_url(),
                                        item.getSrc(),item.getType(),item.getNews_url()));
                                Log.d("pojo details ", item.getTitle()+" "+item.getId());
                            }

                            mAdapter.notifyDataSetChanged();
                            avi.hide();
                            //mHomeNewsAdapter = new HomeNewsAdapter(homeNewsPojos, getActivity());
                            //mNewsList.setAdapter(mHomeNewsAdapter);
                            //mHomeNewsAdapter.notifyDataSetChanged();

                        }catch (Exception e)
                        {
                            e.printStackTrace();
                            avi.hide();

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
                            avi.hide();
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

   /* public void setAds()
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
*/
    @Override
    public void onPause() {
       /* if (mAdView != null) {
            mAdView.pause();
        }*/
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
     /*   if (mAdView != null) {
            mAdView.resume();
        }*/
    }

    @Override
    public void onDestroy() {
     /*   if (mAdView != null) {
            mAdView.destroy();
        }*/
        super.onDestroy();
    }
}
