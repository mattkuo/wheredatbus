package com.mattkuo.wheredatbus.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mattkuo.wheredatbus.R;
import com.mattkuo.wheredatbus.model.Bus;
import com.mattkuo.wheredatbus.model.Routes;
import com.mattkuo.wheredatbus.model.Stops;
import com.mattkuo.wheredatbus.protobuff.ProtoCoordinate;
import com.mattkuo.wheredatbus.protobuff.ProtoPath;
import com.mattkuo.wheredatbus.protobuff.ProtoShape;
import com.mattkuo.wheredatbus.protobuff.ProtoStop;

import java.util.ArrayList;
import java.util.HashMap;

public class TransitDataMapFragment extends MapFragment {
    public static final String EXTRA_SHORT_ROUTE_NAME = "com.mattkuo.wheredatbus" +
            ".EXTRA_SHORT_ROUTE_NAME";
    public static final String EXTRA_BUSSTOP_CODE = "com.mattkuo.wheredatbus.EXTRA_BUSSTOP_CODE";
    private boolean mFirstTimeLoad = true;
    private GoogleMap mGoogleMap;
    private Context mContext;

    // For routes map
    private String mRouteName;
    private LatLngBounds.Builder mBoundsBuilder;
    private LatLngBounds mLatLngBounds;
    private HashMap<Bus, Marker> mBusMarkerHashMap;

    // For Bus stop map
    private int mBusStopCode;

    private MapsLoadedListener mMapsLoadedListener;

    // For initializing bus route map
    public static TransitDataMapFragment newInstance(String shortRouteName) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_SHORT_ROUTE_NAME, shortRouteName);
        TransitDataMapFragment mapFragment = new TransitDataMapFragment();
        mapFragment.setArguments(bundle);
        return mapFragment;
    }

    // For initializing Bus stop map
    public static TransitDataMapFragment newInstance(int busStopCode) {
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_BUSSTOP_CODE, busStopCode);
        TransitDataMapFragment mapFragment = new TransitDataMapFragment();
        mapFragment.setArguments(bundle);
        return mapFragment;
    }

    public void handleClickedBus(Bus bus) {
        if (mGoogleMap == null) {
            return;
        }
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(bus.getLatLng()));
        mBusMarkerHashMap.get(bus).showInfoWindow();
    }

    public void plotBuses(ArrayList<Bus> listOfBuses) {
        if (mGoogleMap == null) {
            return;
        }

        if (mBusMarkerHashMap == null) {
            mBusMarkerHashMap = new HashMap<>();
        }
        for (Bus bus : listOfBuses) {

            int directionMarker;

            switch (bus.getDirection().toUpperCase()) {
                case "NORTH":
                    directionMarker = R.drawable.ic_bus_up;
                    break;
                case "EAST":
                    directionMarker = R.drawable.ic_bus_right;
                    break;
                case "SOUTH":
                    directionMarker = R.drawable.ic_bus_down;
                    break;
                case "WEST":
                    directionMarker = R.drawable.ic_bus_left;
                    break;
                default:
                    directionMarker = R.drawable.ic_bus_up;
                    break;
            }

            Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(bus.getLatLng())
                    .title("Bus #: " + bus.getVehicleNo()).flat(true).icon
                            (BitmapDescriptorFactory.fromResource(directionMarker)));

            mBusMarkerHashMap.put(bus, marker);
        }
    }

    public void plotRoute() {
        Routes routes = Routes.getInstance(mContext);
        ProtoShape shape = routes.getShape(mRouteName);

        if (mGoogleMap == null) {
            return;
        }
        mBoundsBuilder = new LatLngBounds.Builder();
        for (ProtoPath protoPath : shape.path) {
            PolylineOptions options = new PolylineOptions();
            for (ProtoCoordinate coordinate : protoPath.coordinates) {
                LatLng position = new LatLng(coordinate.latitude, coordinate.longitude);
                options.add(position);
                mBoundsBuilder.include(position);
            }
            mGoogleMap.addPolyline(options.color(0x7f3498db));
        }
        mLatLngBounds = mBoundsBuilder.build();
    }

    public void plotStop() {
        ProtoStop stop = Stops.getInstance(getActivity()).getStop(mBusStopCode);

        LatLng stopLatLng = new LatLng(stop.coordinate.latitude, stop.coordinate.longitude);

        if (mGoogleMap == null) {
            return;
        }

        mBoundsBuilder = new LatLngBounds.Builder();
        mBoundsBuilder.include(stopLatLng);
        mLatLngBounds = mBoundsBuilder.build();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRouteName = getArguments().getString(EXTRA_SHORT_ROUTE_NAME);
        mBusStopCode = getArguments().getInt(EXTRA_BUSSTOP_CODE);
        mContext = getActivity();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mMapsLoadedListener = (MapsLoadedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement " +
                    "MapsLoadedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mGoogleMap = getMap();

        if (mGoogleMap == null) {
            return view;
        }

        MapsInitializer.initialize(getActivity());

        mGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (mFirstTimeLoad) {
                    mFirstTimeLoad = false;
                    mMapsLoadedListener.onMapsLoaded();
                    // Need to setup bounds during onMapsLoaded call
                    CameraUpdate update;
                    if (mLatLngBounds != null) {
                        update = CameraUpdateFactory.newLatLngBounds(mLatLngBounds, 50);
                    } else {
                        update = null;
                    }


                    mGoogleMap.moveCamera(update);

                    if (mRouteName == null) {
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLngBounds.getCenter(), 15.0f));
                    }
                }

            }
        });

        return view;
    }

    public interface MapsLoadedListener {
        public void onMapsLoaded();
    }
}
