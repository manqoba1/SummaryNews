package com.sifiso.codetribe.summarylib.sql;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.sifiso.codetribe.summarylib.sql.SummaryContract.CategoryEntry;
import com.sifiso.codetribe.summarylib.sql.SummaryContract.ArticleEntry;

/**
 * Created by sifiso on 3/28/2015.
 */
public class SummaryProvider extends ContentProvider {
    private static SQLiteQueryBuilder liteQueryBuilder;
    private static final int CATEGORY_ID = 300;
    private static final int CATEGORIES = 301;
    private static final int ARTICLES = 302;
    private static final int ARTICLE_BY_CATEGORY_ID = 303;
    private SQLiteOpenHelper mOpenHelper;
    private static final UriMatcher uriMatcher = buildUriMatcher();//assign

    @Override
    public boolean onCreate() {
        mOpenHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        liteQueryBuilder = new SQLiteQueryBuilder();

        int type = uriMatcher.match(uri);
        switch (type) {

            case CATEGORIES:
                // looking up all categories
                liteQueryBuilder.setTables(CategoryEntry.TABLE_NAME);
                break;
            case ARTICLE_BY_CATEGORY_ID:
                // looking up all articles by category id
                liteQueryBuilder.setTables(ArticleEntry.TABLE_NAME);
                liteQueryBuilder.appendWhere(ArticleEntry.COLUMN_CATEGORY_ID + " = " + uri.getLastPathSegment());
                break;
        }
        if (mOpenHelper == null) {
            return null;
        }
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = liteQueryBuilder.query(db, projection, null, null, null, null, null);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int type = uriMatcher.match(uri);
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        Uri returnUri;
        switch (type) {
            case CATEGORIES:
                long id = db.insert(CategoryEntry.TABLE_NAME, null, values);
                returnUri = CategoryEntry.buildCategoryUriId(id);
                break;
            case ARTICLES:
                long ids = db.insert(ArticleEntry.TABLE_NAME, null, values);
                returnUri = ArticleEntry.buildArticleUriId(ids);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(returnUri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    // star(*) for anything string or date
    public static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = SummaryContract.CONTENT_AUTHORITY;

        //uri to get all categories
        matcher.addURI(authority, SummaryContract.PATH_CATEGORY, CATEGORIES);

        //uri to get all articles
        matcher.addURI(authority, SummaryContract.PATH_ARTICLE, ARTICLES);

        //get articles by category id article/category/21
        matcher.addURI(authority, SummaryContract.PATH_ARTICLE + "/" + SummaryContract.PATH_CATEGORY + "/#", ARTICLE_BY_CATEGORY_ID);
        return matcher;
    }


}
