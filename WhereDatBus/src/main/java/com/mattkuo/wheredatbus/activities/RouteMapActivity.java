package com.mattkuo.wheredatbus.activities;


import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

import com.mattkuo.wheredatbus.R;
import com.mattkuo.wheredatbus.fragments.BusListFragment;
import com.mattkuo.wheredatbus.fragments.TransitDataMapFragment;
import com.mattkuo.wheredatbus.model.Bus;

import java.util.ArrayList;

/**
 *
 */
public class RouteMapActivity extends Activity implements BusListFragment.BusListListener,
        TransitDataMapFragment.MapsLoadedListener {
    public final static String EXTRA_SHORT_ROUTE_NAME = "com.mattkuo.wheredatbus.EXTRA_SHORT_ROUTE_NAME";
    String mShortRouteName;
    TransitDataMapFragment mMapFragment;
    BusListFragment mBusListFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_list);
        FragmentManager fm = getFragmentManager();

        mShortRouteName = getIntent().getExtras().getString(EXTRA_SHORT_ROUTE_NAME);

        getActionBar().setSubtitle("Route: " + mShortRouteName);

        mMapFragment = TransitDataMapFragment.newInstance(mShortRouteName);
        fm.beginTransaction().replace(R.id.map_container, mMapFragment).commit();

        mBusListFragment = BusListFragment.newInstance(mShortRouteName);
        fm.beginTransaction().replace(R.id.map_data_list, mBusListFragment).commit();
    }

    @Override
    public void onBusListItemClick(Bus bus) {
        mMapFragment.handleClickedBus(bus);
    }

    @Override
    public void onBusListLoaded(ArrayList<Bus> listOfBuses) {
        mMapFragment.plotBuses(listOfBuses);
    }

    @Override
    public void onMapsLoaded() {
        mMapFragment.plotRoute();
    }


}
