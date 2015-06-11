package com.sifiso.codetribe.summarylib;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.sifiso.codetribe.summarylib.fragment.CategoryListFragment;
import com.sifiso.codetribe.summarylib.fragment.EmptyFragment;
import com.sifiso.codetribe.summarylib.fragment.NewsFeedsFragment;
import com.sifiso.codetribe.summarylib.model.Article;
import com.sifiso.codetribe.summarylib.model.Category;
import com.sifiso.codetribe.summarylib.util.WebCheck;
import com.sifiso.codetribe.summarylib.util.WebCheckResult;


public class MainPaneCategory extends FragmentActivity implements CategoryListFragment.CategoryListFragmentListener,NewsFeedsFragment.OnFragmentInteractionListener{
    private boolean isTwoPane = false;
    Category category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dual_pane);
        wr = WebCheck.checkNetworkAvailability(getApplicationContext());
        determinePaneLayout();

    }
    private void determinePaneLayout() {
        FrameLayout fragmentItemDetail = (FrameLayout) findViewById(R.id.flDetailContainer);
        if (fragmentItemDetail != null) {
            isTwoPane = true;
            CategoryListFragment fragmentItemsList =
                    (CategoryListFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentCategoryList);
            /*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(fragmentItemsList,"");
            ft.commit();*/

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_dual_pane, menu);
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
    public void onFragmentInteraction(Category cat) {
        category = cat;
        if (isTwoPane) { // single activity with list and detail
            // Replace frame layout with correct detail fragment
            NewsFeedsFragment fragmentItem = NewsFeedsFragment.newInstance(category);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flDetailContainer, fragmentItem);
            ft.commit();
        } else { // separate activities
            // launch detail activity using intent
            Intent i = new Intent(this, MainPaneArticle.class);
            i.putExtra("category", category);
            startActivity(i);
        }
    }
    WebCheckResult wr;
    @Override
    public void onArticleClicked(Article article) {
        if (wr.isWifiConnected() || wr.isMobileConnected()) {
            Intent intent = new Intent(MainPaneCategory.this, BrowserActivity.class);
            intent.putExtra("article", article);
            intent.putExtra("header", category.getDisplay_category_name());
            startActivity(intent);

            return;
        }

        Toast.makeText(getApplicationContext(), R.string.connect_to_browser, Toast.LENGTH_LONG).show();
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

                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, emptyFragment).commit();
                // fm.add(R.id.content_frame, newsFeeds, null).commit();
            }
        });

    }
}
