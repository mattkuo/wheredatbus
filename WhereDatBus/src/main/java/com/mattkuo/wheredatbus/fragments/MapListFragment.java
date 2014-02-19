package com.mattkuo.wheredatbus.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.mattkuo.wheredatbus.R;
import com.mattkuo.wheredatbus.adapters.BusListAdapter;
import com.mattkuo.wheredatbus.data.TranslinkService;
import com.mattkuo.wheredatbus.model.Bus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MapListFragment extends Fragment {
    static final String EXTRA_ROUTE_NAME = "com.mattkuo.wheredatbus.route_name";
    static final LatLng VANCOUVER = new LatLng(49.250, -123.100);

    private GoogleMap mMap;
    private String mRoute;
    private Bundle mBundle;
    private MapView mMapView;
    private ListView mListView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRoute = getArguments().getString(EXTRA_ROUTE_NAME);
        mBundle = savedInstanceState;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreateView(inflater, parent, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_map_list, parent, false);
        mListView = (ListView) v.findViewById(R.id.bus_list);
        try {
            MapsInitializer.initialize(getActivity());
        } catch (GooglePlayServicesNotAvailableException e) {
            // What to do if play services not available
        }


        mMapView = (MapView) v.findViewById(R.id.map_container);
        mMapView.onCreate(mBundle);
        setUpMapIfNecessary(v);

        TranslinkService.getService().listOfBusForRoute(mRoute, getResources().getString(R.string
                .translink), new Callback<List<Bus>>() {
            @Override
            public void success(List<Bus> buses, Response response) {

                ArrayList<Bus> busList = new ArrayList<Bus>();
                busList.addAll(buses);

                ArrayAdapter busAdapter = new BusListAdapter(getActivity().getApplicationContext
                        (), busList);
                mListView.setAdapter(busAdapter);
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();

    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        mMapView.onLowMemory();
        super.onLowMemory();
    }

    private void setUpMapIfNecessary(View view) {
        if (mMap == null) {
            mMap = ((MapView) view.findViewById(R.id.map_container)).getMap();

            if (mMap != null) {
                // TODO: Setup map here
                setupMap();
            }
        }
    }

    private void setupMap() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(VANCOUVER, 15));
        // mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
    }

    //	public static MapListFragment newInstance(String shortRouteId){
    //		Bundle args = new Bundle();
    //		args.putString(EXTRA_ROUTE_NAME, shortRouteId);
    //
    //		MapListFragment fragment = new MapListFragment();
    //		fragment.setArguments(args);
    //		return fragment;
    //	}


}
