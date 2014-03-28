package com.mattkuo.wheredatbus.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mattkuo.wheredatbus.model.Routes;
import com.mattkuo.wheredatbus.protobuff.ProtoCoordinate;
import com.mattkuo.wheredatbus.protobuff.ProtoPath;
import com.mattkuo.wheredatbus.protobuff.ProtoShape;

public class TransitDataMapFragment extends MapFragment {
    public static final String SHORT_ROUTE_NAME = "com.mattkuo.wheredatbus.short_route_name";
    private GoogleMap mGoogleMap;
    private Context mContext;
    private String mRouteName;
    private LatLngBounds.Builder mBoundsBuilder;

    public static TransitDataMapFragment newInstance(String shortRouteName) {
        Bundle bundle = new Bundle();
        bundle.putString(SHORT_ROUTE_NAME, shortRouteName);
        TransitDataMapFragment mapFragment = new TransitDataMapFragment();
        mapFragment.setArguments(bundle);
        return mapFragment;
    }


    private void plotRoutes(ProtoShape shape) {
        if (mGoogleMap == null) {
            return;
        }
        for (ProtoPath protoPath : shape.path) {
            PolylineOptions options = new PolylineOptions();
            for (ProtoCoordinate coordinate : protoPath.coordinates) {
                LatLng position = new LatLng(coordinate.latitude, coordinate.longitude);
                options.add(position);
                mBoundsBuilder.include(position);
            }
            mGoogleMap.addPolyline(options.color(0x7f3498db));
        }
    }

    private void setupMap() {
        mBoundsBuilder = new LatLngBounds.Builder();
        Routes routes = Routes.getInstance(mContext);
        this.plotRoutes(routes.getShape(mRouteName));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRouteName = getArguments().getString(SHORT_ROUTE_NAME);
        mContext = getActivity().getApplicationContext();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mGoogleMap = getMap();
        setupMap();

        mGoogleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                LatLngBounds bounds = mBoundsBuilder.build();
                CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, 50);
                mGoogleMap.moveCamera(update);
            }
        });

        return view;
    }
}
