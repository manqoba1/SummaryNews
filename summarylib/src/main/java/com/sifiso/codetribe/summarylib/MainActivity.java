package com.sifiso.codetribe.summarylib;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;


import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.sifiso.codetribe.summarylib.adapter.DrawerAdapter;
import com.sifiso.codetribe.summarylib.fragment.EmptyFragment;
import com.sifiso.codetribe.summarylib.fragment.NewsFeeds;
import com.sifiso.codetribe.summarylib.model.Article;
import com.sifiso.codetribe.summarylib.model.Category;
import com.sifiso.codetribe.summarylib.model.RequestData;
import com.sifiso.codetribe.summarylib.model.ResponseData;
import com.sifiso.codetribe.summarylib.sql.SummaryContract;
import com.sifiso.codetribe.summarylib.sql.UtilProvider;
import com.sifiso.codetribe.summarylib.toolbox.BaseVolley;
import com.sifiso.codetribe.summarylib.util.SummaryIntentService;
import com.sifiso.codetribe.summarylib.util.TimerUtil;
import com.sifiso.codetribe.summarylib.util.Util;
import com.sifiso.codetribe.summarylib.util.WebCheck;
import com.sifiso.codetribe.summarylib.util.WebCheckResult;
import com.sifiso.codetribe.summarylib.util.bean.ArticleReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements ArticleReceiver.Receiver, NewsFeeds.OnFragmentInteractionListener {

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
    private UtilProvider utilProvider;
    int itemIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);
        ctx = getApplicationContext();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //progressBar.setVisibility(View.GONE);
        utilProvider = new UtilProvider(ctx);
        drawerListView = (ListView) findViewById(R.id.left_drawer);
        LayoutInflater in = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = in.inflate(R.layout.hero_drawer, null);
        View footer = in.inflate(R.layout.drawer_footer, null);
        Button img = (Button) v.findViewById(R.id.button);
        drawerListView.addHeaderView(v);
        if (savedInstanceState != null) {
            itemIndex = savedInstanceState.getInt("itemIndex");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this a
        // dds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //getDataArticle(26, null);


        //getDataArticle(26, null);
        // refresher();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("article", articles);
        outState.putInt("itemIndex", itemIndex);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
    }

    WebCheckResult wr;

    @Override
    protected void onStart() {
        super.onStart();
        wr = WebCheck.checkNetworkAvailability(getApplicationContext());

        Log.d(TAG, "is onStart connected");
        if (wr.isMobileConnected()) {
            Log.d(TAG, "is mobile connected");
            getData();
            return;
        }
        if (wr.isWifiConnected()) {
            Log.d(TAG, "is wifi connected");
            getData();
            return;
        }

        getLocalCategories();

    }

    private void getLocalCategories() {
        categories = utilProvider.getAllCategory(getContentResolver());
        mDrawerAdapter = new DrawerAdapter(getApplicationContext(), R.layout.drawer_items, categories);
        drawerListView.setAdapter(mDrawerAdapter);
       /* LayoutInflater in = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = in.inflate(R.layout.hero_drawer, null);
        View footer = in.inflate(R.layout.drawer_footer, null);
        Button img = (Button) v.findViewById(R.id.button);
        //img.setImageDrawable(Util.getRandomHeroImage(ctx));
        drawerListView.addHeaderView(v);
        drawerListView.addFooterView(footer);*/

        drawerListView.setSelection(itemIndex);
        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemIndex = i - 1;
                setTitle(categories.get(itemIndex).getEnglish_category_name());
                categoryName = categories.get(itemIndex).getEnglish_category_name();
                getArticleByCategoryID(categories.get(itemIndex).getCategory_id());
                drawerListView.setSelection(i);
                // mDrawerLayout.setDrawerTitle(i, sTitles.get(i));
                mDrawerLayout.closeDrawers();
            }
        });

        setSelect();

    }

    private void getArticleByCategoryID(int id) {
        articles = new ArrayList<>();
        articles = utilProvider.getArticleByCategoryID(getContentResolver(), id);
        //  Toast.makeText(ctx, "No articles found {0}"+id, Toast.LENGTH_LONG).show();

        if (articles == null || articles.isEmpty()) {
            // Toast.makeText(ctx, "No articles found, please connect to internet/mobile network", Toast.LENGTH_LONG).show();
            setEmptyError();
            return;
        }
        setFragment();
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
        Log.d(TAG, "id : ");
        progressBar.setVisibility(View.VISIBLE);

        BaseVolley.getRemoteData(data, getApplicationContext(), new BaseVolley.BohaVolleyListener() {
            @Override
            public void onResponseReceived(final JSONArray r) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
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


                                categories.add(ar);
                                sTitles.add(ar.getEnglish_category_name());
                                utilProvider.insertCategory(ar);
                            }
                            mDrawerAdapter = new DrawerAdapter(getApplicationContext(), R.layout.drawer_items, categories);
                            drawerListView.setAdapter(mDrawerAdapter);


                            drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    itemIndex = i - 1;
                                    setTitle(categories.get(itemIndex).getEnglish_category_name());
                                    categoryName = categories.get(itemIndex).getEnglish_category_name();
                                    getDataArticle(categories.get(itemIndex).getCategory_id(), null, categories.get(itemIndex));
                                    drawerListView.setSelection(i);
                                    // mDrawerLayout.setDrawerTitle(i, sTitles.get(i));
                                    mDrawerLayout.closeDrawers();
                                    isFirst=false;
                                }
                            });

                            setSelect();
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

    boolean isFirst = true;
    String categoryName;
    EmptyFragment emptyFragment;

    private void setSelect() {
        if (isFirst) {
            categoryName = categories.get(itemIndex).getEnglish_category_name();
            setTitle(categories.get(itemIndex).getEnglish_category_name());
            drawerListView.setSelection(itemIndex);
            Log.d(TAG, "id : " + categories.get(itemIndex).getCategory_id());
            getArticleByCategoryID(categories.get(itemIndex).getCategory_id());
            isFirst = false;
        }
    }

    NewsFeeds newsFeeds;

    private void setFragment() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                newsFeeds = new NewsFeeds();
                Bundle b = new Bundle();
                b.putSerializable("articles", articles);
                newsFeeds.setArguments(b);
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, newsFeeds).commit();
                // fm.add(R.id.content_frame, newsFeeds, null).commit();
            }
        });

    }

    private void setEmptyError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                emptyFragment = new EmptyFragment();

                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, emptyFragment).commit();
                // fm.add(R.id.content_frame, newsFeeds, null).commit();
            }
        });

    }

    private void getDataArticle(final int categoryID, String searcher, final Category c) {
        RequestData data = new RequestData();
        data.setArticleByCategory(categoryID, searcher);
        //progressBar.setVisibility(View.GONE);
        Log.d(TAG, "id : " + categoryID);
        BaseVolley.getRemoteData(data, getApplicationContext(), new BaseVolley.BohaVolleyListener() {
            @Override
            public void onResponseReceived(final JSONArray r) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            articles = new ArrayList<Article>();
                            Log.d(TAG, c.getEnglish_category_name());
                            // utilProvider.insertCategory(c);
                            for (int i = 0; i < r.length(); i++) {

                                Article ar = new Article();
                                JSONObject js = new JSONObject(r.optString(i));
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
                                // response.setArticle(ar);

                                articles.add(ar);
                                utilProvider.insertArticle(ar);
                            }
                            progressBar.setVisibility(View.GONE);

                            setFragment();

                            Log.i(TAG, articles.size() + "");
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

    @Override
    public void onBackPressed() {
        if(isFirst){
            super.onBackPressed();
        }
        isFirst=true;
        mDrawerLayout.openDrawer(drawerListView);


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


    @Override
    public void onArticleClicked(Article article) {
        wr = WebCheck.checkNetworkAvailability(ctx);
        if (wr.isWifiConnected()) {
            Intent intent = new Intent(MainActivity.this, BrowserActivity.class);
            intent.putExtra("article", article);
            intent.putExtra("header", categoryName);
            startActivity(intent);

            return;
        }
        if (wr.isMobileConnected()) {
            Intent intent = new Intent(MainActivity.this, BrowserActivity.class);
            intent.putExtra("article", article);
            intent.putExtra("header", categoryName);
            startActivity(intent);

            return;
        }
        Toast.makeText(ctx, R.string.connect_to_browser, Toast.LENGTH_LONG).show();
    }
}
