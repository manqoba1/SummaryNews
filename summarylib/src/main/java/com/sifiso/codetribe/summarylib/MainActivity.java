package com.sifiso.codetribe.summarylib;

import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.sifiso.codetribe.summarylib.adapter.DrawerAdapter;
import com.sifiso.codetribe.summarylib.fragment.NewsFeeds;
import com.sifiso.codetribe.summarylib.model.Article;
import com.sifiso.codetribe.summarylib.model.Category;
import com.sifiso.codetribe.summarylib.model.RequestData;
import com.sifiso.codetribe.summarylib.model.ResponseData;
import com.sifiso.codetribe.summarylib.sql.SummaryContract;
import com.sifiso.codetribe.summarylib.sql.SummaryDataLoader;
import com.sifiso.codetribe.summarylib.sql.SummaryProvider;
import com.sifiso.codetribe.summarylib.toolbox.BaseVolley;
import com.sifiso.codetribe.summarylib.util.SummaryIntentService;
import com.sifiso.codetribe.summarylib.util.TimerUtil;
import com.sifiso.codetribe.summarylib.util.bean.ArticleReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity implements ArticleReceiver.Receiver {

    private ArticleReceiver mReceiver;
    Gson gson = new Gson();
    private Uri todoUri, todoUri1;

    private DrawerLayout mDrawerLayout;
    private DrawerAdapter mDrawerAdapter;
    ProgressBar progressBar;
    private Context ctx;
    private ListView drawerListView;
    private String[] titles;
    private List<String> sTitles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);
        ctx = getApplicationContext();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //progressBar.setVisibility(View.GONE);
        drawerListView = (ListView) findViewById(R.id.left_drawer);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this a
        // dds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //getDataArticle(26, null);

        getData();

        //getDataArticle(26, null);
        // refresher();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        TimerUtil.killTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TimerUtil.killTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //getDataArticle(26, null);

        //getData();

        //getDataArticle(26, null);
        // refresher();

    }

    ResponseData responseData, response;

    @Override
    public void onReceiveResult(int resultCode, Bundle bundle) {
        switch (resultCode) {
            case SummaryIntentService.STATUS_RUNNING:

                setProgressBarIndeterminateVisibility(true);
                break;
            case SummaryIntentService.STATUS_FINISHED:
                /* Hide progress & extract result from bundle */
                setProgressBarIndeterminateVisibility(false);

                // response = (ArrayList<Article>) bundle.getSerializable("article");
                Log.d("Main2", "int :" + ((ArrayList<Article>) bundle.getSerializable("article")).size());
                saveState(((ArrayList<Article>) bundle.getSerializable("article")));
                break;
            case SummaryIntentService.STATUS_ERROR:
                /* Handle the error */
                String error = bundle.getString(Intent.EXTRA_TEXT);
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                break;
        }
    }

    private ArrayList<Article> articles;
    private ArrayList<Category> categories;

    static String TAG = MainActivity.class.getSimpleName();

    private void getData() {
        RequestData data = new RequestData();
        data.setCategoryURL();
        progressBar.setVisibility(View.VISIBLE);
        BaseVolley.getRemoteData(data, getApplicationContext(), new BaseVolley.BohaVolleyListener() {
            @Override
            public void onResponseReceived(JSONArray r) {
                try {
                    getContentResolver().delete(SummaryContract.CategoryEntry.CONTENT_URI, null, null);
                    categories = new ArrayList<Category>();

                    for (int i = 0; i < r.length(); i++) {

                        // jsonObject.getString("category_id")
                        Category ar = new Category();
                        JSONObject jsonObject = new JSONObject(r.optString(i));
                        ar.setCategory_id(jsonObject.optInt("category_id"));
                        ar.setDisplay_category_name(jsonObject.optString("display_category_name"));
                        ar.setUrl_category_name(jsonObject.optString("url_category_name"));
                        ar.setEnglish_category_name(jsonObject.optString("english_category_name"));
                        // Log.e(TAG, "hello : " + ar.getCategory_id());
                        saveCategory(ar);
                        categories.add(ar);
                        sTitles.add(ar.getEnglish_category_name());
                    }
                    mDrawerAdapter = new DrawerAdapter(getApplicationContext(), R.layout.drawer_items, sTitles);
                    drawerListView.setAdapter(mDrawerAdapter);
                    LayoutInflater in = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v = in.inflate(R.layout.hero_drawer, null);
                    View footer = in.inflate(R.layout.drawer_footer, null);
                    Button img = (Button) v.findViewById(R.id.button);
                    //img.setImageDrawable(Util.getRandomHeroImage(ctx));
                    drawerListView.addHeaderView(v);
                    drawerListView.addFooterView(footer);


                    drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            setTitle(categories.get(i - 1).getEnglish_category_name());
                            getDataArticle(categories.get(i - 1).getCategory_id(), null);
                            drawerListView.setSelection(i - 1);
                            // mDrawerLayout.setDrawerTitle(i, sTitles.get(i));
                            mDrawerLayout.closeDrawers();
                        }
                    });

                    setSelect();
                    progressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onVolleyError(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    int index = 0;
    boolean isFirst = true;

    private void setSelect() {
        if (isFirst) {
            setTitle(categories.get(2).getEnglish_category_name());
            getDataArticle(categories.get(2).getCategory_id(), null);
            isFirst = false;
        }
    }

    NewsFeeds newsFeeds;

    private void setFragment() {
        newsFeeds = new NewsFeeds();
        Bundle b = new Bundle();
        b.putSerializable("articles", articles);
        newsFeeds.setArguments(b);
        getSupportFragmentManager().beginTransaction().add(R.id.content_frame, newsFeeds).commit();
        // fm.add(R.id.content_frame, newsFeeds, null).commit();
    }

    private void getDataArticle(int categoryID, String searcher) {
        RequestData data = new RequestData();
        data.setArticleByCategory(categoryID, searcher);
        progressBar.setVisibility(View.GONE);
        BaseVolley.getRemoteData(data, getApplicationContext(), new BaseVolley.BohaVolleyListener() {
            @Override
            public void onResponseReceived(JSONArray r) {
                try {
                    articles = new ArrayList<Article>();

                    for (int i = 0; i < r.length(); i++) {

                        Article ar = new Article();
                        JSONObject js = new JSONObject(r.optString(i));
                        //Log.d(TAG, js.toString());
                        ar.setSummary(js.optString("summary"));
                        ar.setUrl(js.optString("url"));
                        ar.setTitle(js.optString("title"));
                        ar.setSource_url(js.optString("source_url"));
                        ar.setSource(js.optString("source"));
                        ar.setPublish_date(js.optString("publish_date"));
                        ar.setAuthor(js.optString("author"));

                        // Log.i(TAG, ar.getPublish_date());
                        if (js.optJSONArray("enclosures") != null) {
                            for (int x = 0; x < js.optJSONArray("enclosures").length(); x++) {
                                JSONObject encl = (JSONObject) js.optJSONArray("enclosures").get(x);
                                // Log.i(TAG, encl.toString() + " hello");
                                ar.setMedia_type(encl.optString("media_type"));
                                ar.setUri(encl.optString("uri"));
                            }
                        }
                        // response.setArticle(ar);

                        articles.add(ar);
                    }
                    progressBar.setVisibility(View.GONE);
                    saveState(articles);
                    setFragment();
                    Log.i(TAG, articles.size() + "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onVolleyError(VolleyError error) {

            }
        });

    }

    private void refresher() {
        TimerUtil.startTimer(new TimerUtil.TimerListener() {
            @Override
            public void onRefreshData() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        mReceiver = new ArticleReceiver(new Handler());
                        mReceiver.setReceiver(MainActivity.this);
                        Intent intent = new Intent(Intent.ACTION_SYNC, null, MainActivity.this, SummaryIntentService.class);
                        Log.d("Main", "timer");
                        // Send optional extras to Download IntentService
                        intent.putExtra("receiver", mReceiver);
                        intent.putExtra("categoryID", 26);

                        startService(intent);
                    }
                });
            }
        });
    }


    private void saveCategory(Category category) {
        if (category == null) {
            return;
        }


        ContentValues values = new ContentValues();
        values.put(SummaryContract.CategoryEntry.CATEGORY_ID, category.getCategory_id());
        values.put(SummaryContract.CategoryEntry.DISPLAY_NAME, category.getDisplay_category_name());
        values.put(SummaryContract.CategoryEntry.ENGLISH_NAME, category.getEnglish_category_name());
        values.put(SummaryContract.CategoryEntry.URL_CATEGORY, category.getUrl_category_name());

        if (todoUri == null) {
            // New todo
            //getContentResolver().delete(SummaryContract.CategoryEntry.CONTENT_URI, null, null);
            todoUri = getContentResolver().insert(SummaryContract.CategoryEntry.CONTENT_URI, values);
            Log.e(TAG, todoUri.toString());
            todoUri = null;
        }


    }

    private void saveState(List<Article> articles) {

        if (articles == null) {
            return;
        }
        getContentResolver().delete(SummaryContract.ArticleEntry.CONTENT_URI, null, null);
        for (Article article : articles) {
            ContentValues values = new ContentValues();
            values.put(SummaryContract.ArticleEntry.AUTHOR, article.getAuthor());
            values.put(SummaryContract.ArticleEntry.PUBLISH_DATE, article.getPublish_date());
            values.put(SummaryContract.ArticleEntry.SOURCE, article.getSource());
            values.put(SummaryContract.ArticleEntry.SOURCE_URL, article.getSource_url());
            values.put(SummaryContract.ArticleEntry.SUMMARY, article.getSummary());
            values.put(SummaryContract.ArticleEntry.TITLE, article.getTitle());
            values.put(SummaryContract.ArticleEntry.MEDIA_TYPE, article.getMedia_type());
            values.put(SummaryContract.ArticleEntry.IMAGE_URI, article.getUri());

            if (todoUri1 == null) {
                // New todo
                int rows = getContentResolver().delete(SummaryContract.ArticleEntry.CONTENT_URI, null, null);
                todoUri1 = getContentResolver().insert(SummaryContract.ArticleEntry.CONTENT_URI, values);
                Log.e(TAG, rows + " hello " + todoUri1);
                todoUri1 = null;
            }
        }

    }
}
