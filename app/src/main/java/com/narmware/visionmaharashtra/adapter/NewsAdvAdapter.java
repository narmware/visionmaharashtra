package com.narmware.visionmaharashtra.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.narmware.visionmaharashtra.R;
import com.narmware.visionmaharashtra.pojo.NewsItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsAdvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    private ArrayList<NewsItem> mData;

    public NewsAdvAdapter(Context mContext, ArrayList<NewsItem> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    class ViewHolder0 extends RecyclerView.ViewHolder {

        TextView mTitle, mDate;
        ImageView mImage;
        String id;

        public ViewHolder0(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.item_news_title);
            mDate = itemView.findViewById(R.id.item_news_date);
            mImage = itemView.findViewById(R.id.item_news_image);
        }
    }

    class ViewHolder2 extends RecyclerView.ViewHolder {

        TextView mTitle, mDate;
        ImageView mImage;

        public ViewHolder2(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.item_news_title);
            mDate = itemView.findViewById(R.id.item_news_date);
            mImage = itemView.findViewById(R.id.item_news_image);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        Log.e("Adapter size",mData.size()+"");

        if(position%2 == 0)
        {
            return 2;
        }
       else {
            return 0;
        }
        //return position % 2 * 2;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new ViewHolder0(LayoutInflater.from(mContext).inflate(R.layout.news_item, parent, false));
            case 2:
                return new ViewHolder2(LayoutInflater.from(mContext).inflate(R.layout.add_item, parent, false));

        }

        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case 0:
                ViewHolder0 viewHolder0 = (ViewHolder0)holder;
                viewHolder0.mTitle.setText(mData.get(position).getV_title());
                viewHolder0.mDate.setText(mData.get(position).getV_date());
                String videoId = mData.get(position).getVideo_id(mData.get(position).getV_link());
                viewHolder0.id=videoId;

                Picasso.with(mContext)
                        .load("https://img.youtube.com/vi/" + videoId + "/0.jpg")
                        .fit()
                        .centerCrop()
                        .error(R.drawable.ic_launcher_background)
                        .into(viewHolder0.mImage);

                viewHolder0.id = mData.get(position).getV_id();

                break;

            case 2:
                break;
        }
    }
}