package com.narmware.realpic.fragments;

import android.app.Activity;
import android.app.admin.SystemUpdatePolicy;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.narmware.realpic.R;
import com.narmware.realpic.activity.DetailedNewsActivity;
import com.narmware.realpic.activity.SingleVideoActivity;
import com.narmware.realpic.pojo.VideoPojo2;
import com.narmware.realpic.support.ScreenshotHelper;
import com.narmware.realpic.support.Support;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SingleNewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SingleNewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SingleNewsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String NEWS_TITLE = "title";
    private static final String NEWS_DESC = "desc";
    private static final String NEWS_IMG = "img";
    private static final String NEWS_DATE = "date";
    private static final String NEWS_TYPE= "type";
    private static final String NEWS_WEB_URL= "url";

    // TODO: Rename and change types of parameters
    private String mTitle;
    private String mDesc;
    private String mImg,mDate,mType,mUrl;

    private OnFragmentInteractionListener mListener;
    @BindView(R.id.news_item_title) TextView mTxtNewsTitle;
    @BindView(R.id.news_item_desc) TextView mTxtNewsDesc;
    @BindView(R.id.news_img) ImageView mImgNews;
    @BindView(R.id.news_item_date)protected TextView mDateStamp;
    @BindView(R.id.img_play)protected ImageView mImgPlay;
    //@BindView(R.id.share) protected ImageButton mShare;
    @BindView(R.id.rltiv) protected LinearLayout mLinLay;

    String mVideoId;

    public SingleNewsFragment() {
        // Required empty public constructor
    }


    public static SingleNewsFragment newInstance(String mTitle, String mDesc,String mImg,String mDate,String mType,String mUrl) {
        SingleNewsFragment fragment = new SingleNewsFragment();
        Bundle args = new Bundle();
        args.putString(NEWS_TITLE, mTitle);
        args.putString(NEWS_DESC, mDesc);
        args.putString(NEWS_IMG, mImg);
        args.putString(NEWS_DATE, mDate);
        args.putString(NEWS_TYPE, mType);
        args.putString(NEWS_WEB_URL, mUrl);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(NEWS_TITLE);
            mDesc = getArguments().getString(NEWS_DESC);
            mImg = getArguments().getString(NEWS_IMG);
            mDate = getArguments().getString(NEWS_DATE);
            mType = getArguments().getString(NEWS_TYPE);
            mUrl = getArguments().getString(NEWS_WEB_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_single_news, container, false);
        ButterKnife.bind(this,view);

        mTxtNewsTitle.setText(mTitle);
        mTxtNewsDesc.setText(mDesc);
        mDateStamp.setText(mDate);

        if(mType.equals(Support.NEWS_TYPE_IMAGE))
        {
            mImgPlay.setVisibility(View.INVISIBLE);

            Picasso.with(getContext())
                    .load(mImg)
                    .fit()
                    .placeholder(R.drawable.image_placeholder)
                    .into(mImgNews);

        }
        else {
            mImgPlay.setVisibility(View.VISIBLE);

            String videoId = VideoPojo2.getVideoId(mImg);

            Picasso.with(getContext())
                    .load("https://img.youtube.com/vi/" + videoId + "/0.jpg")
                    .fit()
                    .placeholder(R.drawable.image_placeholder)
                    .into(mImgNews);
        }
      /*  mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String top =mTitle;
                String bottom = "आत्ता बातम्या वाचण्याचा एक नवीन अंदाज... ८५ शब्दामध्ये आपल्या BOL 85 ऍप वर... त्वरीत Download करा\n\n" +
                        "https://goo.gl/FLD5wW";
                String shareText =  bottom;

                File path = ScreenshotHelper.takeScreenshot(getActivity());
                Intent share = new Intent(Intent.ACTION_SEND);

                // If you want to share a png image only, you can do:
                // setType("image/png"); OR for jpeg: setType("image/jpeg");
                share.setType("image*//*");

                Uri uri = Uri.fromFile(path);
                share.putExtra(Intent.EXTRA_STREAM, uri);
                share.putExtra(Intent.EXTRA_TEXT,shareText);

                getActivity().startActivity(Intent.createChooser(share, "Share Now"));
            }
        });*/

        mLinLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(),mTitle,Toast.LENGTH_SHORT).show();

                if(mType.equals(Support.NEWS_TYPE_IMAGE)) {
                    //Toast.makeText(mContext,"url: "+mItemList.get(position).getNews_url(), Toast.LENGTH_LONG).show();

                    if(mUrl.equals(""))
                    {

                    }
                    if(!mUrl.equals("")){
                        Intent i = new Intent(getContext(), DetailedNewsActivity.class);
                        i.putExtra(Support.NEWS_URL, mUrl );
                        getContext().startActivity(i);
                    }

                }
                else {
                    mVideoId=VideoPojo2.getVideoId(mImg);

                    Intent i = new Intent(getContext(),SingleVideoActivity.class);
                    i.putExtra(Support.VIDEO_ID, mVideoId );
                    getContext().startActivity(i);
                }

            }
        });
        return view;
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
}
