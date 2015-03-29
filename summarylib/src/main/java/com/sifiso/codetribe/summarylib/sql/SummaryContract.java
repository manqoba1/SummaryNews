package com.sifiso.codetribe.summarylib.sql;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by sifiso on 3/28/2015.
 */
public final class SummaryContract {
    public static final String CONTENT_AUTHORITY = "com.sifiso.codetribe.summarylib";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String DATABASE_NAME="summary.db";
    public static final String PATH_ARTICLE = "article";
    public static final String PATH_CATEGORY = "category";

    public SummaryContract() {
    }

    public static class ArticleEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ARTICLE).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_ARTICLE;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_ARTICLE;

        public static final String TABLE_NAME = "article";
        public static final String _ID = "article_id";
        public static final String COLUMN_CATEGORY_ID = "category_id";
        public static final String COLUMN_ARTICLE_AUTHOR = "article_author";
        public static final String COLUMN_ARTICLE_PUBLISH_DATE = "article_publish_date";
        public static final String COLUMN_ARTICLE_SUMMARY = "article_summary";
        public static final String COLUMN_ARTICLE_MEDIA_TYPE = "article_media_type";
        public static final String COLUMN_ARTICLE_URI = "article_uri";
        public static final String COLUMN_ARTICLE_URL = "article_url";
        public static final String COLUMN_ARTICLE_TITLE = "article_title";

        public static Uri buildArticleUriId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }


    public static class CategoryEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CATEGORY).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_CATEGORY;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_CATEGORY;

        public static final String TABLE_NAME = "category";
        public static final String _ID = "category_id";
        public static final String ROW_ID = "_id";
        public static final String COLUMN_CATEGORY_NAME = "category_name";

        public static Uri buildCategoryUriId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
