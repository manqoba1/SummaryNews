package com.sifiso.codetribe.summarylib.sql;


import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.sifiso.codetribe.summarylib.model.Article;
import com.sifiso.codetribe.summarylib.model.Category;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class SummaryProvider extends ContentProvider {

    DBHelper dbHelper;
    // used for the UriMacher
    private static final int CATEGORIES = 1;
    private static final int CATEGORIES_ID = 2;

    private static final int ARTICLE = 3;
    private static final int ARTICLE_ID = 4;


    public static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(SummaryContract.AUTHORITY, SummaryContract.BASE_PATH_CAT, CATEGORIES);
        sURIMatcher.addURI(SummaryContract.AUTHORITY, SummaryContract.BASE_PATH_CAT + "/#", CATEGORIES_ID);

        sURIMatcher.addURI(SummaryContract.AUTHORITY, SummaryContract.BASE_PATH_ART, ARTICLE);
        sURIMatcher.addURI(SummaryContract.AUTHORITY, SummaryContract.BASE_PATH_ART + "/#", ARTICLE_ID);

    }

    public SummaryProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        int rowsDeleted = 0;
        String id = null;
        switch (uriType) {

            case ARTICLE:
                rowsDeleted = sqlDB.delete(SummaryContract.ArticleEntry.ARTICLE_TABLE, selection,
                        selectionArgs);
                break;
            case ARTICLE_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(SummaryContract.ArticleEntry.ARTICLE_TABLE,
                            SummaryContract.ArticleEntry._ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(SummaryContract.ArticleEntry.ARTICLE_TABLE,
                            SummaryContract.ArticleEntry._ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            case CATEGORIES:
                rowsDeleted = sqlDB.delete(SummaryContract.CategoryEntry.CATEGORY_TABLE, selection,
                        selectionArgs);
                break;
            case CATEGORIES_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(SummaryContract.CategoryEntry.CATEGORY_TABLE,
                            SummaryContract.CategoryEntry.CATEGORY_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(SummaryContract.CategoryEntry.CATEGORY_TABLE,
                            SummaryContract.CategoryEntry.CATEGORY_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        switch (sURIMatcher.match(uri)) {
            case ARTICLE:
                return SummaryContract.ArticleEntry.CONTENT_TYPE;
            case ARTICLE_ID:
                return SummaryContract.ArticleEntry.CONTENT_ITEM_TYPE;
            case CATEGORIES:
                return SummaryContract.CategoryEntry.CONTENT_TYPE;
            case CATEGORIES_ID:
                return SummaryContract.CategoryEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        Uri uri_resp = null;
        long id = 0;
        switch (uriType) {
            case CATEGORIES:
                id = sqlDB.insert(SummaryContract.CategoryEntry.CATEGORY_TABLE, null, values);
                uri_resp = Uri.parse(SummaryContract.BASE_PATH_CAT + "/" + id);
                break;
            case ARTICLE:
                id = sqlDB.insert(SummaryContract.ArticleEntry.ARTICLE_TABLE, null, values);
                uri_resp = Uri.parse(SummaryContract.BASE_PATH_ART + "/" + id);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return uri_resp;
    }

    @Override
    public boolean onCreate() {
        dbHelper = DBHelper.getHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
        checkColumns(projection);

        // Set the table
        queryBuilder.setTables(SummaryContract.CategoryEntry.CATEGORY_TABLE + " INNER JOIN " + SummaryContract.ArticleEntry.ARTICLE_TABLE + " ON " +
                SummaryContract.CategoryEntry.CATEGORY_TABLE + "." + SummaryContract.CategoryEntry.CATEGORY_ID + " = " + SummaryContract.ArticleEntry.ARTICLE_TABLE + "." + SummaryContract.CategoryEntry.CATEGORY_ID);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case CATEGORIES:
                break;
            case CATEGORIES_ID:
                // adding the ID to the original query
                queryBuilder.appendWhere(SummaryContract.CategoryEntry.CATEGORY_ID + "="
                        + uri.getLastPathSegment());
                break;
            case ARTICLE:
                break;
            case ARTICLE_ID:
                // adding the ID to the original query
                queryBuilder.appendWhere(SummaryContract.ArticleEntry._ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        int rowsUpdated = 0;
        String id = null;
        switch (uriType) {
            case CATEGORIES:
                rowsUpdated = sqlDB.update(SummaryContract.CategoryEntry.CATEGORY_TABLE,
                        values,
                        selection,
                        selectionArgs);
                break;
            case CATEGORIES_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(SummaryContract.CategoryEntry.CATEGORY_TABLE,
                            values,
                            SummaryContract.CategoryEntry.CATEGORY_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(SummaryContract.CategoryEntry.CATEGORY_TABLE,
                            values,
                            SummaryContract.CategoryEntry.CATEGORY_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            case ARTICLE:
                rowsUpdated = sqlDB.update(SummaryContract.ArticleEntry.ARTICLE_TABLE,
                        values,
                        selection,
                        selectionArgs);
                break;
            case ARTICLE_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(SummaryContract.ArticleEntry.ARTICLE_TABLE,
                            values,
                            SummaryContract.ArticleEntry._ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(SummaryContract.ArticleEntry.ARTICLE_TABLE,
                            values,
                            SummaryContract.ArticleEntry._ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {
        String[] available = {SummaryContract.CategoryEntry.CATEGORY_ID, SummaryContract.CategoryEntry.DISPLAY_NAME,
                SummaryContract.CategoryEntry.ENGLISH_NAME, SummaryContract.CategoryEntry.URL_CATEGORY,
                SummaryContract.ArticleEntry._ID, SummaryContract.ArticleEntry.AUTHOR,
                SummaryContract.ArticleEntry.PUBLISH_DATE, SummaryContract.ArticleEntry.SOURCE, SummaryContract.ArticleEntry.URL,
                SummaryContract.ArticleEntry.SOURCE_URL, SummaryContract.ArticleEntry.SUMMARY, SummaryContract.ArticleEntry.TITLE
                , SummaryContract.ArticleEntry.MEDIA_TYPE, SummaryContract.ArticleEntry.IMAGE_URI};

        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }


    public static ArrayList<Category> getCategoryList(ContentResolver contentResolver) {
        ArrayList<Category> categories = new ArrayList<Category>();
        Log.d(TAG, SummaryContract.CategoryEntry.CONTENT_URI.toString());
        Cursor cursor = contentResolver.query(SummaryContract.CategoryEntry.CONTENT_URI, null, null, null, null);
        if (cursor == null) {
            return null;
        }
        Log.d(TAG, SummaryContract.CategoryEntry.CONTENT_URI.toString() + cursor.toString() + "sjdfnsdf");
        Log.d(TAG, "int " + cursor.getCount());
        while (cursor.moveToNext()) {
            Log.d(TAG, "int " + cursor.getString(cursor.getColumnIndex(SummaryContract.CategoryEntry.DISPLAY_NAME)));
            int category_id = cursor.getInt(cursor.getColumnIndex(SummaryContract.CategoryEntry.CATEGORY_ID));
            String display_name = cursor.getString(cursor.getColumnIndex(SummaryContract.CategoryEntry.DISPLAY_NAME));
            String english_name = cursor.getString(cursor.getColumnIndex(SummaryContract.CategoryEntry.ENGLISH_NAME));
            String url_category = cursor.getString(cursor.getColumnIndex(SummaryContract.CategoryEntry.URL_CATEGORY));
            Category category = new Category(category_id, display_name, english_name, url_category);
            Log.i(TAG, "Category :" + category.toString());
            categories.add(category);
        }
        cursor.close();
        return categories;
    }


    public static ArrayList<Article> getArticles(ContentResolver contentResolver) {
        ArrayList<Article> articles = new ArrayList<Article>();
        Cursor cursor = contentResolver.query(SummaryContract.ArticleEntry.CONTENT_URI, null, null, null, SummaryContract.ArticleEntry.SORT_ORDER_DEFAULT);
        if (cursor == null) {
            return null;
        }
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(SummaryContract.ArticleEntry._ID));
            String author = cursor.getString(cursor.getColumnIndex(SummaryContract.ArticleEntry.AUTHOR));
            String publish_date = cursor.getString(cursor.getColumnIndex(SummaryContract.ArticleEntry.PUBLISH_DATE));
            String source = cursor.getString(cursor.getColumnIndex(SummaryContract.ArticleEntry.SOURCE));
            String source_url = cursor.getString(cursor.getColumnIndex(SummaryContract.ArticleEntry.SOURCE_URL));
            String summary = cursor.getString(cursor.getColumnIndex(SummaryContract.ArticleEntry.SUMMARY));
            String title = cursor.getString(cursor.getColumnIndex(SummaryContract.ArticleEntry.TITLE));
            String media_url = cursor.getString(cursor.getColumnIndex(SummaryContract.ArticleEntry.MEDIA_TYPE));
            String uri = cursor.getString(cursor.getColumnIndex(SummaryContract.ArticleEntry.IMAGE_URI));
            String url = cursor.getString(cursor.getColumnIndex(SummaryContract.ArticleEntry.URL));

            Article article = new Article(id, author, url, publish_date, source, source_url, summary, title, media_url, uri);
            Log.i(TAG, "Article :" + article.toString());
            articles.add(article);
        }
        cursor.close();
        return articles;
    }

    static String TAG = SummaryProvider.class.getSimpleName();
}
