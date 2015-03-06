package com.sifiso.codetribe.summarylib.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sifiso.codetribe.summarylib.R;
import com.sifiso.codetribe.summarylib.model.Article;
import com.sifiso.codetribe.summarylib.util.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by CodeTribe1 on 2015-02-23.
 */
public class NewsFeedsAdapter extends RecyclerView.Adapter<NewsFeedsAdapter.NewsFeedViewHolder> {

    public interface NewsFeedsAdapterListener {
        public void onArticleView(Article article);
    }

    private Context mContext;
    private ArrayList<Article> mList;
    private int rowLayout;
    private NewsFeedsAdapterListener listener;

    public NewsFeedsAdapter(Context mContext, ArrayList<Article> mList, int rowLayout, NewsFeedsAdapterListener listener) {
        this.mContext = mContext;
        this.mList = mList;
        this.rowLayout = rowLayout;
        this.listener = listener;
    }

    @Override
    public NewsFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feeds_card_item, parent, false);
        return new NewsFeedViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final NewsFeedViewHolder holder, int position) {
        Article article = mList.get(position);
        holder.FC_date.setText(article.getPublish_date());
        holder.FC_more.setText(article.getUrl());
        holder.FC_Summary.setText(article.getSummary());
        holder.FC_title.setText(article.getTitle());
        String image = article.getUri();
        Log.d(LOG, " " + article.getUri());


        ImageLoader.getInstance().displayImage(article.getUri(), holder.FC_url, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
               // holder.FC_url.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_launcher));
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
               // holder.FC_url.setMaxHeight(bitmap.getWidth());
//                Log.e(LOG,bitmap.toString());
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class NewsFeedViewHolder extends RecyclerView.ViewHolder {
        protected ImageView FC_url;
        protected TextView FC_title, FC_date, FC_Summary, FC_more;
        protected int position;


        public NewsFeedViewHolder(View itemView) {
            super(itemView);
            FC_url = (ImageView) itemView.findViewById(R.id.FC_url);
            FC_date = (TextView) itemView.findViewById(R.id.FC_date);
            FC_Summary = (TextView) itemView.findViewById(R.id.FC_Summary);
            FC_title = (TextView) itemView.findViewById(R.id.FC_title);
            FC_more = (TextView) itemView.findViewById(R.id.FC_more);
        }

    }

    static final String LOG = NewsFeedsAdapter.class.getSimpleName();
}
