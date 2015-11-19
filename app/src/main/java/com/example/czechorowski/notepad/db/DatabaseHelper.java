package com.example.czechorowski.notepad.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by MiroslawCzechorowski on 19.11.2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        super(context, Database.DB_NAME,null,Database.DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery=String.format("CREATE TABLE %s (_id INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT)"
                ,Database.TABLE, Database.Columns.TITLE,Database.Columns.DATA,Database.Columns.DATE);
        db.execSQL(sqlQuery);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Database.TABLE);
        onCreate(db);
    }

}
