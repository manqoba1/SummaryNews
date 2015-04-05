package com.sifiso.codetribe.summarylib.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
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
        final Article article = mList.get(position);
        String date=article.getPublish_date();
        holder.FC_date.setText(date);

        holder.FC_Summary.setText(article.getSummary());
        holder.FC_title.setText(article.getTitle());
        String image = article.getUri();
        Log.d(LOG, " " + article.getUri());
        holder.FC_author.setText(article.getAuthor());
        holder.FC_more.setVisibility(View.GONE);
        holder.FC_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onArticleView(article);
            }
        });
        holder.FC_Summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onArticleView(article);
            }
        });
        if(article.getSummary().trim().isEmpty() || article == null){
            holder.cardView.setVisibility(View.GONE);
           // mList.remove(article);

        }
        if (article.getUri() != null) {
            if (article.getMedia_type().equals("image/jpeg")) {
                //holder.FC_url.setImageUrl(article.getUri(),new com.android.volley.toolbox.ImageLoader());
                holder.FC_url.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(article.getUri(), holder.FC_url);
            } else {
                holder.FC_url.setVisibility(View.GONE);
            }
        } else {
            holder.FC_url.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class NewsFeedViewHolder extends RecyclerView.ViewHolder {
        protected ImageView FC_url;
        protected TextView FC_title, FC_date, FC_Summary, FC_more, FC_author;
        protected CardView cardView;
        protected int position;


        public NewsFeedViewHolder(View itemView) {
            super(itemView);
            FC_url = (ImageView) itemView.findViewById(R.id.FC_url);
            FC_date = (TextView) itemView.findViewById(R.id.FC_date);
            FC_Summary = (TextView) itemView.findViewById(R.id.FC_Summary);
            FC_title = (TextView) itemView.findViewById(R.id.FC_title);
            FC_more = (TextView) itemView.findViewById(R.id.FC_more);
            FC_author = (TextView) itemView.findViewById(R.id.FC_author);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
        }

    }

    static final String LOG = NewsFeedsAdapter.class.getSimpleName();
}
