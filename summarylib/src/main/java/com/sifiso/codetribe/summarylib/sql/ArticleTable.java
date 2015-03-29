package com.sifiso.codetribe.summarylib.sql;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sifiso.codetribe.summarylib.sql.SummaryContract.CategoryEntry;
import com.sifiso.codetribe.summarylib.sql.SummaryContract.ArticleEntry;

/**
 * Created by CodeTribe1 on 2015-02-14.
 */
public class ArticleTable {

    static String TAG = ArticleTable.class.getSimpleName();
    private static final String DATABASE_CREATE = "create table " + ArticleEntry.TABLE_NAME + " (" +
            ArticleEntry._ID + " integer primary key autoincrement, " +
            ArticleEntry.COLUMN_ARTICLE_AUTHOR + " TEXT NOT NULL, " +
            ArticleEntry.COLUMN_ARTICLE_URL + " TEXT NOT NULL, " +
            ArticleEntry.COLUMN_ARTICLE_PUBLISH_DATE + " TEXT NOT NULL unique, " +
            ArticleEntry.COLUMN_ARTICLE_MEDIA_TYPE + " TEXT, " +
            ArticleEntry.COLUMN_ARTICLE_SUMMARY + " TEXT NOT NULL, " +
            ArticleEntry.COLUMN_ARTICLE_URI + " TEXT, " +
            ArticleEntry.COLUMN_CATEGORY_ID + " integer, " +
            ArticleEntry.COLUMN_ARTICLE_TITLE + " TEXT NOT NULL, " +
            "FOREIGN KEY (" + ArticleEntry.COLUMN_CATEGORY_ID + ") REFERENCES " +
            CategoryEntry.TABLE_NAME + " (" + CategoryEntry._ID + "));";

    public static void onCreate(SQLiteDatabase database) {
        Log.i(TAG, "############# --> onCreate UsersTable\n");
        database.execSQL(DATABASE_CREATE);
        // database.execSQL(DDL_CREATE_TRIGGER_DEL_ITEMS);
        Log.d(TAG, "--> Done creating indexes on UsersTable");
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(TAG, "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + ArticleEntry.TABLE_NAME);
        // database.execSQL(DDL_CREATE_TRIGGER_DEL_ITEMS);
        onCreate(database);
    }

    static String DDL_CREATE_TRIGGER_DEL_ITEMS =
            "CREATE TRIGGER delete_article DELETE ON article \n"
                    + "begin\n"
                    + "  delete from category where category_id = old._id;\n"
                    + "end\n";
}
