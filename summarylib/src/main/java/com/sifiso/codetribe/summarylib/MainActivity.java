package com.sifiso.codetribe.summarylib;

import android.content.Context;
import android.content.Intent;
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
import com.sifiso.codetribe.summarylib.util.WebCheck;
import com.sifiso.codetribe.summarylib.util.WebCheckResult;
import com.sifiso.codetribe.summarylib.util.bean.ArticleReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
                categoryName = categories.get(i - 1).getEnglish_category_name();
                getArticleByCategoryID(categories.get(i - 1).getCategory_id());
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

        if (articles == null) {
            Toast.makeText(ctx, "No articles found", Toast.LENGTH_LONG).show();

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
            public void onResponseReceived(JSONArray r) {
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
                            categoryName = categories.get(i - 1).getEnglish_category_name();
                            getDataArticle(categories.get(i - 1).getCategory_id(), null);
                            drawerListView.setSelection(i);
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
    String categoryName;

    private void setSelect() {
        if (isFirst) {
            categoryName = categories.get(2).getEnglish_category_name();
            setTitle(categories.get(2).getEnglish_category_name());
            Log.d(TAG, "id : " + categories.get(2).getCategory_id());
            getArticleByCategoryID(categories.get(2).getCategory_id());
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

    private void getDataArticle(final int categoryID, String searcher) {
        RequestData data = new RequestData();
        data.setArticleByCategory(categoryID, searcher);
        progressBar.setVisibility(View.GONE);
        Log.d(TAG, "id : " + categoryID);
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


    @Override
    public void onArticleClicked(Article article) {
        Intent intent = new Intent(MainActivity.this, BrowserActivity.class);
        intent.putExtra("article", article);
        intent.putExtra("header", categoryName);
        startActivity(intent);
    }
}
