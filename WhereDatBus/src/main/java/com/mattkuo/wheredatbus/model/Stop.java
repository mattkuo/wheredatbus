package com.mattkuo.wheredatbus.model;


import java.io.Serializable;

public class Stop implements Serializable {
    private int mStopCode;
    private String mStopName;
    private double mLatitude;
    private double mLongitude;
    private String[] mRoutes;
    private boolean mIsWheelchairAccessable;

    public Stop(int stopCode, String stopName, double latitude, double longitude, boolean isWheelchairAccessable, String[] routes) {
        this.mLatitude = latitude;
        this.mStopCode = stopCode;
        this.mStopName = stopName;
        this.mLongitude = longitude;
        this.mIsWheelchairAccessable = isWheelchairAccessable;
        this.mRoutes = routes;
    }

    public int getStopCode() {
        return mStopCode;
    }

    public void setStopCode(int stopCode) {
        mStopCode = stopCode;
    }

    public String getStopName() {
        return mStopName;
    }

    public void setStopName(String stopName) {
        mStopName = stopName;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public String[] getRoutes() {
        return mRoutes;
    }

    public void setRoutes(String[] routes) {
        this.mRoutes = routes;
    }

    public boolean isIsWheelchairAccessable() {
        return mIsWheelchairAccessable;
    }

    public void setIsWheelchairAccessable(boolean isWheelchairAccessable) {
        this.mIsWheelchairAccessable = isWheelchairAccessable;
    }

    @Override
    public String toString() {
        return String.valueOf(mStopCode);
    }
}
