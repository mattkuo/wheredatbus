package com.mattkuo.wheredatbus.model;

import com.google.android.gms.maps.model.LatLng;

public class Bus {
    private String mVehicleNo;
    private String mRouteNumber;
    private String mDirection;
    private String mDestination;
    private LatLng mLatLng;
    private String mRecordedTime;

    public Bus(String vehicleNo, String routeNumber, String direction, String destination, LatLng latLng,
               String recordedTime) {
        mVehicleNo = vehicleNo;
        mRouteNumber = routeNumber;
        mDirection = direction;
        mDestination = destination;
        mLatLng = latLng;
        mRecordedTime = recordedTime;
    }

    public String getVehicleNo() {
        return this.mVehicleNo;
    }
    public void setVehicleNo(String vehicleNo) {
        mVehicleNo = vehicleNo;
    }

    public String getRouteNumber() {
        return this.mRouteNumber;
    }
    public void setRouteNumber(String routeNumber) {
        mRouteNumber = routeNumber;
    }

    public String getDirection() {
        return this.mDirection;
    }
    public void setDirection(String direction) {
        mDirection = direction;
    }

    public String getDestination() {
        return mDestination;
    }
    public void setDestination(String destination) {
        mDestination = destination;
    }

    public LatLng getLatLng() {
        return mLatLng;
    }
    public void setLatLng(LatLng latLng) {
        mLatLng = latLng;
    }

    public String getRecordedTime() {
        return mRecordedTime;
    }
    public void setRecordedTime(String recordedTime) {
        mRecordedTime = recordedTime;
    }

    @Override
    public String toString() {
        return mVehicleNo;
    }
}
