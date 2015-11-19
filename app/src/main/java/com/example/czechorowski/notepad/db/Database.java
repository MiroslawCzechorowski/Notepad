package com.example.czechorowski.notepad.db;

import android.provider.BaseColumns;

/**
 * Created by MiroslawCzechorowski on 19.11.2015.
 */
public class Database {
    public static final String DB_NAME="com.example.czechorowski.notepad.db.my_files";
    public static final int DB_VERSION=1;
    public static final String TABLE="myFiles";

    public class Columns{
        public static final String TITLE="TITLE";
        public static final String DATA="DATA";
        public static final String DATE="DATE";
        public static final String _ID= BaseColumns._ID;
    }
}
