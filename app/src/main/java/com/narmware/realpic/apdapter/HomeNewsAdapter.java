package com.narmware.realpic.apdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.ImageViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.narmware.realpic.R;
import com.narmware.realpic.activity.DetailedNewsActivity;
import com.narmware.realpic.activity.SingleVideoActivity;
import com.narmware.realpic.pojo.HomeNews;
import com.narmware.realpic.pojo.VideoPojo2;
import com.narmware.realpic.support.SharedPreferenceHelper;
import com.narmware.realpic.support.Support;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ashwini Palve on 15/05/2018.
 */

public class HomeNewsAdapter extends BaseAdapter
{
    ArrayList<HomeNews> mItemList = new ArrayList<>();
    Context mContext;
    @BindView(R.id.news_img) protected ImageView mImgNews;
    @BindView(R.id.news_item_title) protected TextView mTitle;
    @BindView(R.id.news_item_desc)protected TextView mDescription;
    @BindView(R.id.news_item_date)protected TextView mDateStamp;
    @BindView(R.id.img_play)protected ImageView mImgPlay;
    String mVideoId;


    public HomeNewsAdapter(ArrayList<HomeNews> mItemList, Context mContext) {
        this.mItemList = mItemList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mItemList.size();
    }

    @Override
    public Object getItem(int position) {

        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_news_item, parent, false);

        //Toast.makeText(mContext, position+"", Toast.LENGTH_SHORT).show();

        ButterKnife.bind(this, itemView);

        mTitle.setText(mItemList.get(position).getTitle());
        mDescription.setText(mItemList.get(position).getDescription());
        mDateStamp.setText(mItemList.get(position).getSrc());

        if(mItemList.get(position).getType().equals(Support.NEWS_TYPE_IMAGE)) {
            mImgPlay.setVisibility(View.INVISIBLE);

            Picasso.with(mContext)
                    .load(mItemList.get(position).getImage_url())
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(mImgNews);

        }
        else {
            mImgPlay.setVisibility(View.VISIBLE);
            String videoId = VideoPojo2.getVideoId(mItemList.get(position).getImage_url());

            Picasso.with(mContext)
                    .load("https://img.youtube.com/vi/" + videoId + "/0.jpg")
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(mImgNews);
        }
        SharedPreferenceHelper.setLatestNewsId(Integer.parseInt(mItemList.get(position).getId()), mContext);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mItemList.get(position).getType().equals(Support.NEWS_TYPE_IMAGE)) {
                    Toast.makeText(mContext,"url: "+mItemList.get(position).getNews_url(), Toast.LENGTH_LONG).show();

                    Intent i = new Intent(mContext, DetailedNewsActivity.class);
                    i.putExtra(Support.NEWS_URL, mItemList.get(position).getNews_url() );
                    mContext.startActivity(i);
                }
                else {
                    mVideoId=VideoPojo2.getVideoId(mItemList.get(position).getImage_url());

                    Intent i = new Intent(mContext, SingleVideoActivity.class);
                    i.putExtra(Support.VIDEO_ID, mVideoId );
                    mContext.startActivity(i);
                }

            }
        });


        return itemView;

    }
}
