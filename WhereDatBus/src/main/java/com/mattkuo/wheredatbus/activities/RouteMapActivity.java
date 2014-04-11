package com.mattkuo.wheredatbus.activities;


import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.MapFragment;
import com.mattkuo.wheredatbus.R;
import com.mattkuo.wheredatbus.adapters.BusListAdapter;
import com.mattkuo.wheredatbus.data.TranslinkService;
import com.mattkuo.wheredatbus.fragments.BusListFragment;
import com.mattkuo.wheredatbus.fragments.TransitDataMapFragment;
import com.mattkuo.wheredatbus.model.Bus;
import com.mattkuo.wheredatbus.model.Routes;
import com.mattkuo.wheredatbus.protobuff.ProtoShape;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RouteMapActivity extends Activity implements BusListFragment.BusListListener{
    String mShortRouteName;
    TransitDataMapFragment mMapFragment;
    BusListFragment mBusListFragment;


    @Override
    public void onBusListItemClick(Bus bus) {
        mMapFragment.handleClickedBus(bus);
    }

    @Override
    public void onBusListLoaded(ArrayList<Bus> listOfBuses) {
        mMapFragment.plotBuses(listOfBuses);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map_list);
        FragmentManager fm = getFragmentManager();

        mShortRouteName = getIntent().getExtras().getString("ROUTE");

        mMapFragment = TransitDataMapFragment.newInstance(mShortRouteName);
        fm.beginTransaction().replace(R.id.map_container, mMapFragment).commit();

        mBusListFragment = BusListFragment.newInstance(mShortRouteName);
        fm.beginTransaction().replace(R.id.map_data_list, mBusListFragment).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        if (resultCode != ConnectionResult.SUCCESS) {
            GooglePlayServicesUtil.getErrorDialog(resultCode, this, 1).show();
        }
    }
}
