package com.mattkuo.wheredatbus.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;

import com.mattkuo.wheredatbus.R;
import com.mattkuo.wheredatbus.fragments.StopTimesExpListFragment;
import com.mattkuo.wheredatbus.fragments.TransitDataMapFragment;

public class BusStopMapActivity extends Activity implements TransitDataMapFragment.MapsLoadedListener {
    public static final String EXTRA_BUSSTOP = "com.mattkuo.WhereDatBus.EXTRA_BUSSTOP";
    private static final String TAG = "BusStopMapActivity";

    private TransitDataMapFragment mMapFragment;
    private StopTimesExpListFragment mStopTimesExpListFragment;
    private int mBusStopCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_list);
        FragmentManager fm = getFragmentManager();

        mBusStopCode = getIntent().getExtras().getInt(EXTRA_BUSSTOP);

        getActionBar().setSubtitle("Stop: " + mBusStopCode);

        mMapFragment = TransitDataMapFragment.newInstance(mBusStopCode);
        mStopTimesExpListFragment = StopTimesExpListFragment.newInstance(mBusStopCode);
        fm.beginTransaction().replace(R.id.map_data_list, mStopTimesExpListFragment).commit();

    }

    @Override
    public void onMapsLoaded() {
        Log.d(TAG, "Maps Loaded");
    }
}
