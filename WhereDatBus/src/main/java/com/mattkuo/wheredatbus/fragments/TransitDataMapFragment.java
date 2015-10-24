package com.mattkuo.wheredatbus.fragments;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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
import com.mattkuo.wheredatbus.util.Util;

import java.util.ArrayList;
import java.util.HashMap;

public class TransitDataMapFragment extends MapFragment implements LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final String EXTRA_SHORT_ROUTE_NAME = "com.mattkuo.wheredatbus" +
            ".EXTRA_SHORT_ROUTE_NAME";
    public static final String EXTRA_BUSSTOP_CODE = "com.mattkuo.wheredatbus.EXTRA_BUSSTOP_CODE";

    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    protected final static String LOCATION_KEY = "location-key";

    private boolean mFirstTimeLoad = true;
    private GoogleMap mGoogleMap;
    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private boolean mRequestingLocationUpdates = true;
    private Location mCurrentLocation;
    private LocationRequest mLocationRequest;

    // For routes map
    private String mRouteName;
    private LatLngBounds.Builder mBoundsBuilder;
    private LatLngBounds mLatLngBounds;
    private HashMap<Bus, Marker> mBusMarkerHashMap;

    // For Bus stop map. Set to -1 if no bus stop code is passed in
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

    // For initializing Nearby map
    public static TransitDataMapFragment newInstance() {
        Bundle bundle = new Bundle();
        TransitDataMapFragment mapFragment = new TransitDataMapFragment();
        mapFragment.setArguments(bundle);
        return mapFragment;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRouteName = getArguments().getString(EXTRA_SHORT_ROUTE_NAME);
        mBusStopCode = getArguments().getInt(EXTRA_BUSSTOP_CODE, -1);
        mContext = getActivity();
        buildGoogleApiClient();
        updateValuesFromBundle(savedInstanceState);
        createLocationRequest();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mGoogleMap = getMap();

        if (mGoogleMap == null) {
            return view;
        }

        MapsInitializer.initialize(mContext);

        mGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (mFirstTimeLoad) {
                    mFirstTimeLoad = false;
                    mMapsLoadedListener.onMapsLoaded();
                    mGoogleMap.setMyLocationEnabled(true);

                    // Show nearby map
                    CameraUpdate update;

                    // Need better way of checking which constructor was used
                    if (mRouteName == null && mBusStopCode == -1) {
                        return;
                    } else if (mRouteName == null) {
                        update = CameraUpdateFactory.newLatLngZoom(mLatLngBounds.getCenter(), 15.0f);
                    } else {
                        // Zoom to routes bounds
                        // Need to setup bounds during onMapsLoaded call
                        update = CameraUpdateFactory.newLatLngBounds(mLatLngBounds, 50);
                    }

                    mGoogleMap.moveCamera(update);
                }

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mMapsLoadedListener.onLocationUpdate(location);
        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(userLocation, 15.0f);
        mGoogleMap.moveCamera(update);
    }

    @Override
    public void onConnected(Bundle bundle) {
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mCurrentLocation == null) {
            Util.displayPromptForEnablingGPS(getActivity());
            return;
        }
        LatLng userLocation = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(userLocation, 15.0f);
        mGoogleMap.moveCamera(update);
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);
        outState.putParcelable(LOCATION_KEY, mCurrentLocation);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
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

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(bus.getLatLng());
            markerOptions.title("Bus #: " + bus.getVehicleNo());
            markerOptions.icon(BitmapDescriptorFactory.fromResource(directionMarker));
            Marker marker = mGoogleMap.addMarker(markerOptions);

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

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(stopLatLng);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_stop_icon));
        mGoogleMap.addMarker(markerOptions);
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(15000);
        mLocationRequest.setFastestInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState == null) return;

        if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
            mRequestingLocationUpdates = savedInstanceState.getBoolean(REQUESTING_LOCATION_UPDATES_KEY);
        }

        if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
            mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
        }

    }

    public interface MapsLoadedListener {
        void onMapsLoaded();
        void onLocationUpdate(Location location);
    }
}
