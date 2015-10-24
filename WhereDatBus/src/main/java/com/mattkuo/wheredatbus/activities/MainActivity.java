package com.mattkuo.wheredatbus.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.mattkuo.wheredatbus.R;
import com.mattkuo.wheredatbus.data.TranslinkService;
import com.mattkuo.wheredatbus.fragments.RouteDirectoryFragment;
import com.mattkuo.wheredatbus.fragments.SearchBusStopFragment;
import com.mattkuo.wheredatbus.fragments.TransitDataMapFragment;
import com.mattkuo.wheredatbus.model.Stop;
import com.mattkuo.wheredatbus.util.Util;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends Activity implements TransitDataMapFragment.MapsLoadedListener {
    private SearchBusStopFragment mSearchFragment;
    private RouteDirectoryFragment mRouteDirectoryFragment;
    private TransitDataMapFragment mTransitDataMapFragment;

    private Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTransitDataMapFragment = TransitDataMapFragment.newInstance();
        this.swapFragment(mTransitDataMapFragment, "NEARBY_MAP");

        this.handleIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        this.handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_routes:
                if (mRouteDirectoryFragment == null) {
                    mRouteDirectoryFragment = RouteDirectoryFragment.newInstance();
                }
                this.swapFragment(mRouteDirectoryFragment, "ROUTE");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapsLoaded() {
    }

    @Override
    public void onLocationUpdate(Location location) {
        boolean isFirstLocationAcquired = false;
        if (mCurrentLocation == null) {
            mCurrentLocation = location;
            isFirstLocationAcquired = true;
        }

        double movedDistance = Util.distance(mCurrentLocation.getLatitude(),
                mCurrentLocation.getLongitude(),
                location.getLatitude(),
                location.getLongitude());

        if (movedDistance < 250 && !isFirstLocationAcquired) return;

        plotListOfStops(location.getLatitude(), location.getLongitude());
        mCurrentLocation = location;

    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (mSearchFragment == null) {
                mSearchFragment = SearchBusStopFragment.newInstance(query);
            } else {
                mSearchFragment.search(query);
            }

            swapFragment(mSearchFragment, "SEARCH");
        }
    }

    private void swapFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        if (!isTagInBackStack(tag)) transaction.addToBackStack(tag);

        transaction.commit();
    }

    private boolean isTagInBackStack(String tag) {
        if (getFragmentManager().getBackStackEntryCount() == 0) return false;
        getFragmentManager().executePendingTransactions();
        for (int i = 0; i < getFragmentManager().getBackStackEntryCount(); i++) {
            if (getFragmentManager().getBackStackEntryAt(i).getName().equals(tag)) return true;
        }

        return false;
    }

    private void plotListOfStops(double lat, double lng) {


        //Get Stops within a 800m radius
        TranslinkService.getStopService().listOfStopsForLatLng(800,
                Util.roundToNDecimals(lat, 6),
                Util.roundToNDecimals(lng, 6),
                getResources().getString(R.string.translink),
                new Callback<List<Stop>>() {
            @Override
            public void success(List<Stop> stops, Response response) {
                ArrayList<Stop> stopsList = new ArrayList<Stop>();
                stopsList.addAll(stops);
                mTransitDataMapFragment.plotStops(stopsList);
            }

            @Override
            public void failure(RetrofitError error) {
                // TODO: Add error checking
            }
        });
    }

}
