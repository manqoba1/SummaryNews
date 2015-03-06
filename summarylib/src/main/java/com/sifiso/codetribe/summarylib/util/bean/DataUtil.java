package com.sifiso.codetribe.summarylib.util.bean;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.sifiso.codetribe.summarylib.model.Article;
import com.sifiso.codetribe.summarylib.model.Category;
import com.sifiso.codetribe.summarylib.model.RequestData;
import com.sifiso.codetribe.summarylib.toolbox.BaseVolley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CodeTribe1 on 2015-02-14.
 */
public class DataUtil {
    static ArrayList<Category> categories;
    static ArrayList<Article> articles;

    public static ArrayList<Article> getArticles(int categoryID, String searcher, Context ctx) {

        RequestData data = new RequestData();
        data.setArticleByCategory(categoryID, searcher);
        BaseVolley.getRemoteData(data, ctx, new BaseVolley.BohaVolleyListener() {
            @Override
            public void onResponseReceived(JSONArray r) {
                try {
                    articles = new ArrayList<Article>();
                    for (int i = 0; i < r.length(); i++) {

                        Article ar = new Article();
                        JSONObject js = new JSONObject(r.optString(i));
                        Log.d(TAG,js.toString());
                        ar.setSummary(js.optString("summary"));
                        ar.setUrl(js.optString("url"));
                        ar.setTitle(js.optString("title"));
                        ar.setSource_url(js.optString("source_url"));
                        ar.setSource(js.optString("source"));
                        ar.setPublish_date(js.optString("publish_date"));
                        ar.setAuthor(js.optString("author"));

                        Log.i(TAG, ar.getPublish_date());
                        if (js.optJSONArray("enclosures") != null) {
                            for (int x = 0; x < js.optJSONArray("enclosures").length(); x++) {
                                JSONObject encl = (JSONObject) js.optJSONArray("enclosures").get(x);
                                Log.i(TAG, encl.toString() + " hello");
                                ar.setMedia_type(encl.optString("media_type"));
                                ar.setUri(encl.optString("uri"));
                            }
                        }
                        articles.add(ar);

                    }
                    Log.i(TAG, articles.size()+"");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onVolleyError(VolleyError error) {

            }
        });

        return articles;
    }

    public static ArrayList<Category> getCategories(Context ctx) {
        categories = new ArrayList<Category>();
        RequestData data = new RequestData();
        data.setCategoryURL();
        BaseVolley.getRemoteData(data, ctx, new BaseVolley.BohaVolleyListener() {
            @Override
            public void onResponseReceived(JSONArray r) {
                try {
                    for (int i = 0; i < r.length(); i++) {

                        // jsonObject.getString("category_id")
                        Category ar = new Category();
                        JSONObject jsonObject = new JSONObject(r.optString(i));
                        ar.setCategory_id(jsonObject.optInt("category_id"));
                        ar.setDisplay_category_name(jsonObject.optString("display_category_name"));
                        ar.setUrl_category_name(jsonObject.optString("url_category_name"));
                        ar.setEnglish_category_name(jsonObject.optString("english_category_name"));
                        Log.e(TAG, "hello : " + ar.getCategory_id());
                        categories.add(ar);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onVolleyError(VolleyError error) {

            }
        });
        return categories;
    }

    static Gson gson = new Gson();
    static String TAG = DataUtil.class.getSimpleName();
}
