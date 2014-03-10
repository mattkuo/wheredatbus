package com.mattkuo.wheredatbus.fragments;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mattkuo.wheredatbus.R;
import com.mattkuo.wheredatbus.activities.MapListActivity;
import com.mattkuo.wheredatbus.adapters.RouteListAdapter;
import com.mattkuo.wheredatbus.model.Routes;
import com.mattkuo.wheredatbus.protobuff.ProtoRoute;

import java.util.ArrayList;
import java.util.List;


public class RouteDirectoryFragment extends ListFragment {
    private static final String TAG = "RouteDirectoryFragment";

    public RouteDirectoryFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_route_directory, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Routes routes = Routes.getInstance(getActivity().getApplicationContext());
        List<ProtoRoute> routeList = routes.getRoutes();

        ArrayAdapter<ProtoRoute> routesAdapter = new RouteListAdapter(getActivity()
                .getApplication(), new ArrayList<>(routeList));

        setListAdapter(routesAdapter);

    }

    @Override
    public void onListItemClick(ListView l, View view, int position, long id) {
        String selectedRouteShortName = ((ProtoRoute) getListAdapter().getItem(position)).route_short;
        Log.d(TAG, "SelectedID - " + selectedRouteShortName);

        Intent i = new Intent(getActivity(), MapListActivity.class);
        i.putExtra(MapListFragment.EXTRA_ROUTE_NAME, selectedRouteShortName);
        startActivity(i);
    }
}
