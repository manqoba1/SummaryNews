package com.sifiso.codetribe.summarylib.sql;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by CodeTribe1 on 2015-02-14.
 */
public class CategoryTable {
    static String TAG = CategoryTable.class.getSimpleName();
    private static final String DATABASE_CREATE = "create table "
            + SummaryContract.CategoryEntry.CATEGORY_TABLE + " (" + SummaryContract.CategoryEntry.CATEGORY_ID + " integer, " + SummaryContract.CategoryEntry.DISPLAY_NAME
            + " text, " + SummaryContract.CategoryEntry.ENGLISH_NAME + " text, " + SummaryContract.CategoryEntry.URL_CATEGORY + " text);";

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
        database.execSQL("DROP TABLE IF EXISTS " + SummaryContract.CategoryEntry.CATEGORY_TABLE);
       // database.execSQL(DDL_CREATE_TRIGGER_DEL_ITEMS);
        onCreate(database);
    }
    static String DDL_CREATE_TRIGGER_DEL_ITEMS =
            "CREATE TRIGGER delete_article DELETE ON article \n"
                    + "begin\n"
                    + "  delete from category where category_id = old._id;\n"
                    + "end\n";
}
