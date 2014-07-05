package com.mattkuo.wheredatbus.model;

public class Schedule {
    private String mDestination;
    private String mExpectedLeaveTime;
    private int mExpectedCountdown;
    private String mScheduleStatus;
    private boolean mCancelledTrip;
    private boolean mCancelledStop;
    private boolean mAddedTrip;
    private boolean mAddedStop;
    private String mLastUpdate;

    public Schedule(String destination, String expectedLeaveTime, int expectedCountdown,
                     String scheduleStatus, boolean cancelledTrip, boolean cancelledStop,
                     boolean addedTrip, boolean addedStop, String lastUpdate) {
        mDestination = destination;
        mExpectedLeaveTime = expectedLeaveTime;
        mExpectedCountdown = expectedCountdown;
        mScheduleStatus = scheduleStatus;
        mCancelledTrip = cancelledTrip;
        mCancelledStop = cancelledStop;
        mAddedTrip = addedTrip;
        mAddedStop = addedStop;
        mLastUpdate = lastUpdate;
    }

    public String getDestination() {
        return mDestination;
    }

    public String getExpectedLeaveTime() {
        return mExpectedLeaveTime;
    }

    public int getExpectedCountdown() {
        return mExpectedCountdown;
    }

    public String getScheduleStatus() {
        return mScheduleStatus;
    }

    public boolean isCancelledTrip() {
        return mCancelledTrip;
    }

    public boolean isCancelledStop() {
        return mCancelledStop;
    }

    public boolean isAddedTrip() {
        return mAddedTrip;
    }

    public boolean isAddedStop() {
        return mAddedStop;
    }

    public String getLastUpdate() {
        return mLastUpdate;
    }
}
