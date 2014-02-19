package com.mattkuo.wheredatbus.fragments;

import java.util.List;

import com.mattkuo.wheredatbus.activities.MapListActivity;
import com.mattkuo.wheredatbus.data.RoutesDataSource;
import com.mattkuo.wheredatbus.model.Route;
import com.mattkuo.wheredatbus.R;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class RouteDirectoryFragment extends ListFragment {
    private static final String TAG = "RouteDirectoryFragment";

    private RoutesDataSource mDataSource;

    public RouteDirectoryFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_route_directory, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context activityContext = getActivity().getApplicationContext();

        if (activityContext != null) {
            mDataSource = new RoutesDataSource(activityContext);
            mDataSource.open();

            List<Route> values = mDataSource.getAllRoutes();
            ArrayAdapter<Route> routesAdapter = new ArrayAdapter<Route>(getActivity()
                    .getApplicationContext(), R.layout.adapter_route_list_item, values);
            setListAdapter(routesAdapter);
        }
    }

    @Override
    public void onListItemClick(ListView l, View view, int position, long id) {
        String selectedRouteShortName = ((Route) getListAdapter().getItem(position))
                .getRouteShortName();
        Log.d(TAG, "SelectedID - " + selectedRouteShortName);

        Intent i = new Intent(getActivity(), MapListActivity.class);
        i.putExtra(MapListFragment.EXTRA_ROUTE_NAME, selectedRouteShortName);
        startActivity(i);
    }
}
