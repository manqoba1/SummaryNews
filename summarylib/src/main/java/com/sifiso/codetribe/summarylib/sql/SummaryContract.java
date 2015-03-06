package com.sifiso.codetribe.summarylib.sql;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class SummaryContract {
    public static final String DATABASE_NAME = "newssummary.db";

    public static final int SUMMARY_LOADER_ID=1;

    public static final String AUTHORITY = "com.sifiso.codetribe.summarylib.sql";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String BASE_PATH_CAT = "category";
    public static final String BASE_PATH_ART = "article";


    public static class ArticleEntry implements BaseColumns {
        public static final String ARTICLE_TABLE = "article";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(BASE_PATH_ART).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + AUTHORITY + "/" + BASE_PATH_ART;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + AUTHORITY + "/" + BASE_PATH_ART;

        public static final String _ID = "id";
        public static final String URL = "url";
        public static final String AUTHOR = "author";
        public static final String PUBLISH_DATE = "publish_date";
        public static final String SOURCE = "source";
        public static final String SOURCE_URL = "source_url";
        public static final String SUMMARY = "summary";
        public static final String TITLE = "title";
        public static final String MEDIA_TYPE = "media_type";
        public static final String IMAGE_URI = "image_uri";

        public static final String SORT_ORDER_DEFAULT = _ID + " DESC";


        public static final String[] DEFAULT_ARTICLE_QUERY_VALUE = new String[]{_ID, AUTHOR, URL,PUBLISH_DATE, SOURCE,
                SOURCE_URL, SUMMARY, TITLE
                , MEDIA_TYPE, IMAGE_URI};
    }


    /*Category entry*/
    public static class CategoryEntry implements BaseColumns {
        public static final String CATEGORY_TABLE = "category";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(BASE_PATH_CAT).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + AUTHORITY + "/" + BASE_PATH_CAT;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + AUTHORITY + "/" + BASE_PATH_CAT;


        public static final String CATEGORY_ID = "category_id";
        public static final String DISPLAY_NAME = "display_category_name";
        public static final String ENGLISH_NAME = "english_category_name";
        public static final String URL_CATEGORY = "url_category_name";

        public static final String[] DEFAULT_CATEGORY_QUERY_VALUE = new String[]{CATEGORY_ID, DISPLAY_NAME, ENGLISH_NAME,
                URL_CATEGORY};

    }


}
