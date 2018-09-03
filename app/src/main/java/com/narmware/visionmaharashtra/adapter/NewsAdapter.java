package com.narmware.visionmaharashtra.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.narmware.visionmaharashtra.R;
import com.narmware.visionmaharashtra.activity.SingleVideoActivity;
import com.narmware.visionmaharashtra.pojo.NewsItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by rohitsavant on 03/09/18.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {
    private Context mContext;
    private ArrayList<NewsItem> mData;

    public NewsAdapter(Context mContext, ArrayList<NewsItem> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.news_item, parent, false);
        return new NewsHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder holder, int position) {
        NewsItem news = mData.get(position);
        holder.mTitle.setText(news.getV_title());
        holder.mDate.setText(news.getV_date());
        String videoId = news.getVideo_id(news.getV_link());
        holder.id=videoId;
       /* Picasso.with(mContext)
                .load(news.getV_link())
                .into(holder.mImage);*/

        Picasso.with(mContext)
                .load("https://img.youtube.com/vi/" + videoId + "/0.jpg")
                .fit()
                .centerCrop()
                .error(R.drawable.ic_launcher_background)
                .into(holder.mImage);

        holder.id = news.getV_id();

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class NewsHolder extends RecyclerView.ViewHolder {
        TextView mTitle, mDate;
        ImageView mImage;
        String id;
        public NewsHolder(View v) {
            super(v);

            mTitle = v.findViewById(R.id.item_news_title);
            mDate = v.findViewById(R.id.item_news_date);
            mImage = v.findViewById(R.id.item_news_image);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext, SingleVideoActivity.class);
                    i.putExtra("VIDEO_ID", id);
                    mContext.startActivity(i);
                }
            });
        }
    }
}
