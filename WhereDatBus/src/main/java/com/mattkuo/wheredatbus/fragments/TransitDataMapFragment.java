package com.mattkuo.wheredatbus.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mattkuo.wheredatbus.protobuff.ProtoCoordinate;
import com.mattkuo.wheredatbus.protobuff.ProtoPath;
import com.mattkuo.wheredatbus.protobuff.ProtoShape;

public class TransitDataMapFragment extends MapFragment {
    public static final String SHORT_ROUTE_NAME = "com.mattkuo.wheredatbus.short_route_name";
    private GoogleMap mGoogleMap;

    public static TransitDataMapFragment newInstance(String shortRouteName) {
        Bundle bundle = new Bundle();
        bundle.putString(SHORT_ROUTE_NAME, shortRouteName);
        TransitDataMapFragment mapFragment = new TransitDataMapFragment();
        mapFragment.setArguments(bundle);
        return mapFragment;
    }

    public void plotRoutes(ProtoShape shape) {
        for (ProtoPath protoPath : shape.path) {
            PolylineOptions options = new PolylineOptions();

            for (ProtoCoordinate coordinate : protoPath.coordinates) {
                options.add(new LatLng(coordinate.latitude, coordinate.longitude));
            }
            mGoogleMap.addPolyline(options);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        mGoogleMap = getMap();
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
