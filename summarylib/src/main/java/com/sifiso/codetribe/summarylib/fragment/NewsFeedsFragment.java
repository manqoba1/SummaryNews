package com.sifiso.codetribe.summarylib.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;
import com.sifiso.codetribe.summarylib.R;
import com.sifiso.codetribe.summarylib.adapter.NewsFeedsAdapter;
import com.sifiso.codetribe.summarylib.model.Article;
import com.sifiso.codetribe.summarylib.model.Category;
import com.sifiso.codetribe.summarylib.model.RequestData;
import com.sifiso.codetribe.summarylib.sql.UtilProvider;
import com.sifiso.codetribe.summarylib.toolbox.BaseVolley;
import com.sifiso.codetribe.summarylib.util.DividerItemDecoration;
import com.sifiso.codetribe.summarylib.util.Util;
import com.sifiso.codetribe.summarylib.util.WebCheck;
import com.sifiso.codetribe.summarylib.util.WebCheckResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsFeedsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * <p/>
 * create an instance of this fragment.
 */
public class NewsFeedsFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private Context ctx;
    private RecyclerView NW_list;
    private NewsFeedsAdapter adapter;
    private Category category;
    private ProgressBar progressBar;
    private UtilProvider utilProvider;
    private int itemIndex;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public static NewsFeedsFragment newInstance(Category category) {
        NewsFeedsFragment fragment = new NewsFeedsFragment();
        Bundle args = new Bundle();
        args.putSerializable("category", category);
        fragment.setArguments(args);
        return fragment;
    }

    public NewsFeedsFragment() {
        // Required empty public constructor
    }

    ArrayList<Article> articles;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Toast.makeText(ctx, articles.get(0).getTitle(), Toast.LENGTH_SHORT).show();
    }

    View v;
    WebCheckResult wr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_news_feeds, container, false);
        wr = WebCheck.checkNetworkAvailability(ctx);
        ctx = getActivity().getApplicationContext();

        category = (Category) getArguments().getSerializable("category");

        itemIndex = getArguments().getInt("itemIndex");
        utilProvider = new UtilProvider(ctx);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        NW_list = (RecyclerView) v.findViewById(R.id.NW_list);
        NW_list.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        NW_list.setItemAnimator(new DefaultItemAnimator());

        NW_list.addItemDecoration(new DividerItemDecoration(ctx, RecyclerView.VERTICAL));

        if (category != null) {
            setSelect();
        }
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
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onArticleClicked(Article article);

        public void onError();
    }

    static String TAG = NewsFeedsFragment.class.getSimpleName();

    private void getDataArticle(final int categoryID, String searcher) {
        RequestData data = new RequestData();
        data.setArticleByCategory(categoryID, searcher);
        progressBar.setVisibility(View.VISIBLE);
        Log.d(TAG, "id 1 : " + categoryID);
        BaseVolley.getRemoteData(data, ctx, new BaseVolley.BohaVolleyListener() {
            @Override
            public void onResponseReceived(final JSONArray r) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            articles = new ArrayList<Article>();
                            //Log.d(TAG, c.getEnglish_category_name());
                            // utilProvider.insertCategory(c);
                            // do back ground here
                            for (int i = 0; i < r.length(); i++) {

                                Article ar = articleModel(new JSONObject(r.optString(i)), categoryID);
                                articles.add(ar);

                                utilProvider.insertArticle(ar);
                            }
                            getArticleByCategoryData(categoryID);
                            progressBar.setVisibility(View.GONE);

                            //setFragment();

                            Log.i(TAG, "The articles : " + articles.size());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

            @Override
            public void onVolleyError(VolleyError error) {

            }
        });

    }

    private Article articleModel(JSONObject js, int categoryID) throws JSONException {
        Article ar = new Article();
        ar.setSummary(Util.prettifyString(js.optString("summary")));
        ar.setUrl(js.optString("url"));
        ar.setTitle(js.optString("title"));
        ar.setSource_url(js.optString("source_url"));
        ar.setSource(js.optString("source"));
        ar.setPublish_date(js.optString("publish_date"));
        ar.setAuthor(js.optString("author"));
        ar.setCategory_id(categoryID);
        // Log.i(TAG, ar.getPublish_date());
        if (js.optJSONArray("enclosures") != null) {
            for (int x = 0; x < js.optJSONArray("enclosures").length(); x++) {
                JSONObject encl = (JSONObject) js.optJSONArray("enclosures").get(x);
                // Log.i(TAG, encl.toString() + " hello");
                ar.setMedia_type(encl.optString("media_type"));
                ar.setUri(encl.optString("uri"));
            }
        }
        return ar;
    }

    private void getArticleByCategoryData(int id) {
        articles = new ArrayList<>();
        // articles
        utilProvider = new UtilProvider(ctx);
        utilProvider.getArticleByCategoryID(ctx.getContentResolver(), id, new UtilProvider.UtilProviderInterface() {
            @Override
            public void onCategoryList(ArrayList<Category> categories) {

            }

            @Override
            public void onArticleList(ArrayList<Article> art) {
                articles = art;
                wr = WebCheck.checkNetworkAvailability(ctx);
                if (articles == null || articles.isEmpty()) {

                    Log.d(TAG, "is mobile connected s : " + category.getCategory_id());
                    //getDataArticle(category.getCategory_id(), null);
                    if (wr.isWifiConnected()) {
                        Log.d(TAG, "is mobile connected : " + category.getCategory_id());
                        getDataArticle(category.getCategory_id(), null);
                    }
                    return;
                }

                Log.d(TAG, "is mobile connected");
                progressBar.setVisibility(View.GONE);
                adapter = new NewsFeedsAdapter(ctx, articles, 1, new NewsFeedsAdapter.NewsFeedsAdapterListener() {
                    @Override
                    public void onArticleView(Article article) {
                        mListener.onArticleClicked(article);
                    }
                });
                NW_list.setAdapter(adapter);


            }

            @Override
            public void onError() {

            }
        });


    }

    boolean isFirst = true;

    private void setSelect() {

        Log.d(TAG, "id art : " + category.getCategory_id());
        getArticleByCategoryData(category.getCategory_id());
        isFirst = false;

    }

    NewsFeedsFragment newsFeeds;

    EmptyFragment emptyFragment;


}
