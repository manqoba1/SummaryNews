package com.sifiso.codetribe.summarylib.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.sifiso.codetribe.summarylib.R;
import com.sifiso.codetribe.summarylib.adapter.NewsFeedsAdapter;
import com.sifiso.codetribe.summarylib.model.Article;
import com.sifiso.codetribe.summarylib.util.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsFeeds.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * <p/>
 * create an instance of this fragment.
 */
public class NewsFeeds extends Fragment {


    private OnFragmentInteractionListener mListener;
    private Context ctx;
    private RecyclerView NW_list;
    private NewsFeedsAdapter adapter;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public NewsFeeds() {
        // Required empty public constructor
    }
    ArrayList<Article> articles;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = getActivity().getApplicationContext();

        articles = (ArrayList<Article>) getArguments().getSerializable("articles");

        Toast.makeText(ctx, articles.get(0).getTitle(), Toast.LENGTH_SHORT).show();
    }

    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_news_feeds, container, false);
        NW_list = (RecyclerView) v.findViewById(R.id.NW_list);
        NW_list.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        NW_list.setItemAnimator(new DefaultItemAnimator());
        NW_list.addItemDecoration(new DividerItemDecoration(ctx, RecyclerView.VERTICAL));
        adapter = new NewsFeedsAdapter(ctx, articles, 1, new NewsFeedsAdapter.NewsFeedsAdapterListener() {
            @Override
            public void onArticleView(Article article) {

            }
        });
        NW_list.setAdapter(adapter);
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Article article) {
        if (mListener != null) {
            mListener.onArticleClicked(article);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
       /* try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onArticleClicked(Article article);
    }

}
