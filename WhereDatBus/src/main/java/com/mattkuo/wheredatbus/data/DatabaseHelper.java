package com.mattkuo.wheredatbus.data;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "bus";
    private static final int DATABASE_VERSION = 1;

    public static final String ROUTE_TABLE = "routes";
    public static final String ROUTE_ID = "route_id";
    public static final String ROUTE_SHORT_NAME = "route_short";
    public static final String ROUTE_LONG_NAME = "route_long_name";

    public static final String TRIPS_TABLE = "trips";
    // public static final

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

}
