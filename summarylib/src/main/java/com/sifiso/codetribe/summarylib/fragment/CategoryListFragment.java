package com.sifiso.codetribe.summarylib.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;
import com.sifiso.codetribe.summarylib.R;
import com.sifiso.codetribe.summarylib.adapter.DrawerAdapter;
import com.sifiso.codetribe.summarylib.model.Article;
import com.sifiso.codetribe.summarylib.model.Category;
import com.sifiso.codetribe.summarylib.model.RequestData;
import com.sifiso.codetribe.summarylib.sql.UtilProvider;
import com.sifiso.codetribe.summarylib.toolbox.BaseVolley;
import com.sifiso.codetribe.summarylib.util.WebCheck;
import com.sifiso.codetribe.summarylib.util.WebCheckResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategoryListFragment.CategoryListFragmentListener} interface
 * to handle interaction events.
 * <p/>
 * create an instance of this fragment.
 */
public class CategoryListFragment extends Fragment {


    private CategoryListFragmentListener mListener;

    private View v;
    private ListView drawerListView;
    private ProgressBar progressBar;
    private DrawerAdapter mDrawerAdapter;
    private UtilProvider utilProvider;
    int itemIndex = 1;
    private Context ctx;
    private Activity activity;
    private ArrayList<Article> articles;
    private ArrayList<Category> categories;
    WebCheckResult wr;


    private void setFields() {
        ctx = getActivity().getApplicationContext();
        activity = getActivity();
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        drawerListView = (ListView) v.findViewById(R.id.left_drawer);
        getLocalCategories();
    }

    static String TAG = CategoryListFragment.class.getSimpleName();

    private void getLocalCategories() {
        utilProvider = new UtilProvider(ctx);
        utilProvider.getAllCategory(ctx.getContentResolver(), new UtilProvider.UtilProviderInterface() {
            @Override
            public void onCategoryList(ArrayList<Category> cat) {
                categories = cat;
                if (wr.isMobileConnected() || wr.isWifiConnected()) {
                    Log.d(TAG, "is mobile connected ");
                     getData();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onArticleList(ArrayList<Article> articles) {

            }

            @Override
            public void onError() {
                return;
            }
        });
        mDrawerAdapter = new DrawerAdapter(ctx, R.layout.drawer_items, categories);
        drawerListView.setAdapter(mDrawerAdapter);

        drawerListView.setSelection(itemIndex);
        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemIndex = i;
                mListener.onFragmentInteraction(categories.get(itemIndex));
            }
        });
        if (!categories.isEmpty()) {
            setSelect();
        }


    }
    private void getData() {
        RequestData data = new RequestData();
        data.setCategoryURL();
        Log.d(TAG, "id : ");
        progressBar.setVisibility(View.VISIBLE);

        BaseVolley.getRemoteData(data, ctx, new BaseVolley.BohaVolleyListener() {
            @Override
            public void onResponseReceived(final JSONArray r) {

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            categories = new ArrayList<Category>();
                            for (int i = 0; i < r.length(); i++) {
                                Category ar = categoryModel(new JSONObject(r.optString(i)));

                                categories.add(ar);
                                utilProvider.insertCategory(ar);
                            }
                           // getLocalCategories();
                            progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

            @Override
            public void onVolleyError(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private Category categoryModel(JSONObject jsonObject) {
        Category ar = new Category();
        ar.setCategory_id(jsonObject.optInt("category_id"));
        ar.setDisplay_category_name(jsonObject.optString("display_category_name"));
        ar.setUrl_category_name(jsonObject.optString("url_category_name"));
        ar.setEnglish_category_name(jsonObject.optString("english_category_name"));
        return ar;
    }

    boolean isFirst = true;

    private void setSelect() {
        if (isFirst) {
            drawerListView.setSelection(itemIndex);
            Log.d(TAG, "id : " + categories.get(itemIndex).getCategory_id());
            mListener.onFragmentInteraction(categories.get(itemIndex));
            isFirst = false;
        }
    }

    public CategoryListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_category_list, container, false);
        wr = WebCheck.checkNetworkAvailability(ctx);

        setFields();
        return v;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (CategoryListFragmentListener) activity;
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
    public interface CategoryListFragmentListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Category category);
    }

}
