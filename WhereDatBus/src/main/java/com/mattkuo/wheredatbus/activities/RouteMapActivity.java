package com.mattkuo.wheredatbus.activities;


import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

import com.google.android.gms.maps.MapFragment;
import com.mattkuo.wheredatbus.R;
import com.mattkuo.wheredatbus.fragments.BusListFragment;
import com.mattkuo.wheredatbus.fragments.TransitDataMapFragment;

public class RouteMapActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map_list);
        FragmentManager fm = getFragmentManager();

        String shortRouteName = getIntent().getExtras().getString("ROUTE");

        MapFragment mapFragment = TransitDataMapFragment.newInstance(shortRouteName);
        fm.beginTransaction().replace(R.id.map_container, mapFragment).commit();

        BusListFragment busListFragment = BusListFragment.newInstance(shortRouteName);
        fm.beginTransaction().replace(R.id.map_data_list, busListFragment).commit();

    }


}
