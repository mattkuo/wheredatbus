package com.mattkuo.wheredatbus.model;


public class Stop {
    private int mStopCode;
    private String mStopName;
    private float mLatitude;
    private float mLongitude;

    public Stop(int stopCode, String stopName, float latitude, float longitude) {
        this.mLatitude = latitude;
        this.mStopCode = stopCode;
        this.mStopName = stopName;
        this.mLongitude = longitude;
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

    public float getLatitude() {
        return mLatitude;
    }

    public void setLatitude(float latitude) {
        mLatitude = latitude;
    }

    public float getLongitude() {
        return mLongitude;
    }

    public void setLongitude(float longitude) {
        mLongitude = longitude;
    }

    @Override
    public String toString() {
        return String.valueOf(mStopCode);
    }
}
