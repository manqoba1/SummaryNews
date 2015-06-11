package com.sifiso.codetribe.summarylib;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.sifiso.codetribe.summarylib.fragment.EmptyFragment;
import com.sifiso.codetribe.summarylib.fragment.NewsFeedsFragment;
import com.sifiso.codetribe.summarylib.model.Article;
import com.sifiso.codetribe.summarylib.model.Category;
import com.sifiso.codetribe.summarylib.util.WebCheck;
import com.sifiso.codetribe.summarylib.util.WebCheckResult;


public class MainPaneArticle extends FragmentActivity implements NewsFeedsFragment.OnFragmentInteractionListener {
    NewsFeedsFragment fragmentItemDetail;
    Category category;
    WebCheckResult wr;
    Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pane_article);
        ctx = getApplicationContext();
        wr = WebCheck.checkNetworkAvailability(ctx);
        category = (Category) getIntent().getSerializableExtra("category");
        if (savedInstanceState == null) {

        }
        fragmentItemDetail = NewsFeedsFragment.newInstance(category);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flDetailContainer, fragmentItemDetail);
        ft.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("category",category);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_pane_article, menu);
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
    public void onArticleClicked(Article article) {

        if (wr.isWifiConnected()||wr.isMobileConnected()) {
            Intent intent = new Intent(MainPaneArticle.this, BrowserActivity.class);
            intent.putExtra("article", article);
            intent.putExtra("header", category.getEnglish_category_name());
            startActivity(intent);

        }
    }

    @Override
    public void onError() {
        setEmptyError();
    }

    EmptyFragment emptyFragment;

    private void setEmptyError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                emptyFragment = new EmptyFragment();

                getSupportFragmentManager().beginTransaction().replace(R.id.flDetailContainer, emptyFragment).commit();
                // fm.add(R.id.content_frame, newsFeeds, null).commit();
            }
        });

    }
}
