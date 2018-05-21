package com.narmware.realpic.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alexvasilkov.foldablelayout.FoldableListLayout;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.narmware.realpic.R;
import com.narmware.realpic.activity.HomeActivity;
import com.narmware.realpic.apdapter.HomeNewsAdapter;
import com.narmware.realpic.pojo.HomeNews;
import com.narmware.realpic.pojo.HomePojoResponse;
import com.narmware.realpic.support.EndPoint;
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
        fetchNews();

        init(mRoot);
        return mRoot;
    }

    private void init(View view) {
        ButterKnife.bind(this,view);
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

    public void fetchNews()
    {
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(getContext());
        }

        String url= EndPoint.NEWS_URL+"?id";
        Log.e("url",url);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>(){



                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("urlr", response.toString());

                        Gson gson=new Gson();
                        HomePojoResponse pojo1=gson.fromJson(response.toString(),HomePojoResponse.class);

                        final HomeNews[] array=pojo1.getData();
                        for(HomeNews item:array)
                        {
                            // String ImgId=item.getId();

                            homeNewsPojos.add(item);
                            Log.d("info",item.getId());
                            Log.d("details ",item.getUrl());

                        }

                            mHomeNewsAdapter = new HomeNewsAdapter(homeNewsPojos, getActivity());
                            mNewsList.setAdapter(mHomeNewsAdapter);
                            mHomeNewsAdapter.notifyDataSetChanged();


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
                    }
                });

        mQueue.add(jsObjRequest);

    }
}
