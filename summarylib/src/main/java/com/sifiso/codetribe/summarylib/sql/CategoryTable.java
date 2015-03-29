package com.sifiso.codetribe.summarylib.sql;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sifiso.codetribe.summarylib.sql.SummaryContract.CategoryEntry;

/**
 * Created by CodeTribe1 on 2015-02-14.
 */
public class CategoryTable {
    static String TAG = CategoryTable.class.getSimpleName();
    private static final String DATABASE_CREATE = "create table " +
            CategoryEntry.TABLE_NAME + " (" +
            CategoryEntry.ROW_ID + " integer primary key autoincrement , " +
            CategoryEntry._ID + " integer unique, " +
            CategoryEntry.COLUMN_CATEGORY_NAME + " text);";

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
        database.execSQL("DROP TABLE IF EXISTS " + CategoryEntry.TABLE_NAME);
        // database.execSQL(DDL_CREATE_TRIGGER_DEL_ITEMS);
        onCreate(database);
    }

    static String DDL_CREATE_TRIGGER_DEL_ITEMS =
            "CREATE TRIGGER delete_article DELETE ON article \n"
                    + "begin\n"
                    + "  delete from category where category_id = old._id;\n"
                    + "end\n";
}
