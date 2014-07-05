package com.mattkuo.wheredatbus.model;

import java.util.ArrayList;

public class StopSchedule {
    private String mRouteNo;
    private String mRouteName;
    private String mDirection;
    private ArrayList<Schedule> mSchedules;

    public StopSchedule(String routeNo, String routeName, String direction, ArrayList<Schedule> schedules) {
        this.mRouteNo = routeNo;
        this.mRouteName = routeName;
        this.mDirection = direction;
        this.mSchedules = schedules;
    }

    public String getRouteNo() {
        return mRouteNo;
    }

    public String getRouteName() {
        return mRouteName;
    }

    public String getDirection() {
        return mDirection;
    }

    public ArrayList<Schedule> getSchedules() {
        return mSchedules;
    }


}
