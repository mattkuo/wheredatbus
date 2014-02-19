package com.mattkuo.wheredatbus.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;

public class Route {
    private String mRouteShortName;
    private String mRouteLongName;

    private LatLngBounds mBounds;
    private ArrayList<LatLng> mListOfPathPoints;
    private long mId;

    public ArrayList<LatLng> getListOfPathPoints() {
        return mListOfPathPoints;
    }

    public void setListOfPathPoints(ArrayList<LatLng> listOfPathPoints) {
        mListOfPathPoints = listOfPathPoints;
    }

    public LatLngBounds getBounds() {
        return mBounds;
    }

    public void setBounds(LatLngBounds bounds) {
        mBounds = bounds;
    }

    public void setRouteLongName(String routeLongName) {
        mRouteLongName = routeLongName.trim();
    }

    public String getRouteLongName() {
        return mRouteLongName;
    }

    public String getRouteShortName() {
        return mRouteShortName;
    }

    public void setRouteShortName(String routeShortName) {
        mRouteShortName = routeShortName.trim();
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    @Override
    public String toString() {
        return mRouteShortName + " - " + mRouteLongName;
    }
}
