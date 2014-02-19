package com.mattkuo.wheredatbus.data;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.mattkuo.wheredatbus.model.Route;

public class RoutesDataSource {
    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDatabaseHelper;
    private String[] mRouteColumns = {DatabaseHelper.ROUTE_ID, DatabaseHelper.ROUTE_SHORT_NAME,
            DatabaseHelper.ROUTE_LONG_NAME};

    public RoutesDataSource(Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        mDatabase = mDatabaseHelper.getReadableDatabase();
    }

    public void close() {
        mDatabaseHelper.close();
    }


    public List<Route> getAllRoutes() {
        List<Route> routes = new ArrayList<Route>();

        Cursor cursor = mDatabase.query(mDatabaseHelper.ROUTE_TABLE, mRouteColumns, null, null,
                null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Route route = cursorToRoute(cursor);
            routes.add(route);
            cursor.moveToNext();
        }

        return routes;
    }

    private Route cursorToRoute(Cursor cursor) {
        Route route = new Route();
        route.setId(cursor.getInt(0));
        route.setRouteShortName(cursor.getString(1));
        route.setRouteLongName(cursor.getString(2));
        return route;
    }

}
