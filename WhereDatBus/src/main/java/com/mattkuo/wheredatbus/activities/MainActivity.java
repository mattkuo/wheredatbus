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

public class MainActivity extends Activity implements TransitDataMapFragment.MapsLoadedListener {
    private SearchBusStopFragment mSearchFragment;
    private RouteDirectoryFragment mRouteDirectoryFragment;
    private TransitDataMapFragment mTransitDataMapFragment;

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
//        TranslinkService.getStopService().listOfStopsForLatLng(500, getResources().getString(R.string
//                .translink), new Callback<List<Bus>>() {
//            @Override
//            public void success(List<Bus> buses, Response response) {
//
//                ArrayList<Bus> busList = new ArrayList<>();
//
//                if (mRouteName != null) {
//                    busList.addAll(buses);
//                }
//
//                ArrayAdapter busAdapter = new BusListAdapter(mContext, busList);
//                setListAdapter(busAdapter);
//                mBusListListener.onBusListLoaded(busList);
//            }
//
//            @Override
//            public void failure(RetrofitError retrofitError) {
//                // TODO Error check
//            }
//        });

    }

    @Override
    public void onLocationUpdate(Location location) {

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

}
