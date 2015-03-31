package com.sifiso.codetribe.summarylib.sql;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.sifiso.codetribe.summarylib.model.Article;
import com.sifiso.codetribe.summarylib.model.Category;
import com.sifiso.codetribe.summarylib.sql.SummaryContract.CategoryEntry;
import com.sifiso.codetribe.summarylib.sql.SummaryContract.ArticleEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sifiso on 3/28/2015.
 */
public class UtilProvider {
    Context ctx;
    int rowInserted;
    private static final String LOG = UtilProvider.class.getSimpleName();

    public UtilProvider(Context ctx) {
        this.ctx = ctx;
    }

    public void insertCategories(List<Category> list) {
        if (list == null) {
            return;
        }

        for (Category c : list) {
            Uri uriCat = ctx.getContentResolver().insert(CategoryEntry.CONTENT_URI, createCategoryContentValues(c));
            long row = ContentUris.parseId(uriCat);
            Log.d(LOG, "id : " + row);
        }
    }

    public void insertArticle(Article c) {
        Uri uriCat = ctx.getContentResolver().insert(ArticleEntry.CONTENT_URI, createArticleContentValues(c));
        long row = ContentUris.parseId(uriCat);
        Log.d(LOG, "id art : " + row);
    }

    public void insertArticles(List<Article> list) {
        if (list == null) {
            return;
        }

        for (Article c : list) {
            Uri uriCat = ctx.getContentResolver().insert(ArticleEntry.CONTENT_URI, createArticleContentValues(c));
            long row = ContentUris.parseId(uriCat);
            Log.d(LOG, "id : " + row);
        }
    }

    public void insertCategory(Category c) {
        Uri uriCat = ctx.getContentResolver().insert(CategoryEntry.CONTENT_URI, createCategoryContentValues(c));
        long row = ContentUris.parseId(uriCat);
        Log.d(LOG, "id : " + row);
    }

    public ContentValues createCategoryContentValues(Category c) {
        ContentValues value = new ContentValues();
        value.put(CategoryEntry._ID, c.getCategory_id());
        value.put(CategoryEntry.COLUMN_CATEGORY_NAME, c.getEnglish_category_name());

        return value;
    }

    public ContentValues createArticleContentValues(Article c) {
        ContentValues value = new ContentValues();
        value.put(ArticleEntry.COLUMN_CATEGORY_ID, c.getCategory_id());
        value.put(ArticleEntry.COLUMN_ARTICLE_AUTHOR, c.getAuthor());
        value.put(ArticleEntry.COLUMN_ARTICLE_MEDIA_TYPE, c.getMedia_type());
        value.put(ArticleEntry.COLUMN_ARTICLE_URI, c.getUri());
        value.put(ArticleEntry.COLUMN_ARTICLE_URL, c.getUrl());
        value.put(ArticleEntry.COLUMN_ARTICLE_TITLE, c.getTitle());
        value.put(ArticleEntry.COLUMN_ARTICLE_SUMMARY, c.getSummary());
        value.put(ArticleEntry.COLUMN_ARTICLE_PUBLISH_DATE, c.getPublish_date());

        return value;
    }

    public ArrayList<Article> getArticleByCategoryID(ContentResolver contentResolver, int id) {
        ArrayList<Article> list = new ArrayList<>();
        try {
            Uri getArticlesOfCategoryUri = Uri.parse(ArticleEntry.CONTENT_URI.toString() + "/" + SummaryContract.PATH_CATEGORY + "/" + id);
            Cursor cursor = contentResolver.query(getArticlesOfCategoryUri, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    list.add(fromCursorArticle(cursor));
                }
            } else {
                return null;
            }
        } catch (Exception e) {

        }
        return list;
    }
    public ArrayList<Category> getAllCategory(ContentResolver contentResolver) {
        ArrayList<Category> list = new ArrayList<>();
        try {
            Uri getCategoryUri = Uri.parse(CategoryEntry.CONTENT_URI.toString());
            Cursor cursor = contentResolver.query(getCategoryUri, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    list.add(fromCursorCategory(cursor));
                }
            } else {
                return null;
            }
        } catch (Exception e) {

        }
        return list;
    }

    private static Article fromCursorArticle(Cursor cursor) {
        Article article = new Article();
        article.setAuthor(cursor.getString(cursor.getColumnIndex(ArticleEntry.COLUMN_ARTICLE_AUTHOR)));
        article.setMedia_type(cursor.getString(cursor.getColumnIndex(ArticleEntry.COLUMN_ARTICLE_MEDIA_TYPE)));
        article.setPublish_date(cursor.getString(cursor.getColumnIndex(ArticleEntry.COLUMN_ARTICLE_PUBLISH_DATE)));
        article.setSummary(cursor.getString(cursor.getColumnIndex(ArticleEntry.COLUMN_ARTICLE_SUMMARY)));
        article.setTitle(cursor.getString(cursor.getColumnIndex(ArticleEntry.COLUMN_ARTICLE_TITLE)));
        article.setUri(cursor.getString(cursor.getColumnIndex(ArticleEntry.COLUMN_ARTICLE_URI)));
        article.setUrl(cursor.getString(cursor.getColumnIndex(ArticleEntry.COLUMN_ARTICLE_URL)));
        return article;
    }
    private static Category fromCursorCategory(Cursor cursor) {
        Category category = new Category();
        category.setCategory_id(cursor.getInt(cursor.getColumnIndex(CategoryEntry._ID)));
        category.setEnglish_category_name(cursor.getString(cursor.getColumnIndex(CategoryEntry.COLUMN_CATEGORY_NAME)));
        return category;
    }
}
