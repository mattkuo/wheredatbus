package com.mattkuo.wheredatbus.fragments;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mattkuo.wheredatbus.R;
import com.mattkuo.wheredatbus.activities.BusStopMapActivity;
import com.mattkuo.wheredatbus.adapters.SearchListAdapter;
import com.mattkuo.wheredatbus.model.Stops;
import com.mattkuo.wheredatbus.protobuff.ProtoStop;

import java.util.ArrayList;


public class SearchBusStopFragment extends ListFragment {
    private static final String TAG = "com.mattkuo.SearchBusStopFragment";
    private static final String SEARCH_QUERY = "SEARCH_QUERY";
    private Context mContext;

    public SearchBusStopFragment() {}

    public static SearchBusStopFragment newInstance(String query) {
        SearchBusStopFragment fragment = new SearchBusStopFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SEARCH_QUERY, query);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        String query = getArguments().getString(SEARCH_QUERY, "");
        this.search(query);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_bus_stop, container, false);

        return view;
    }

    @Override
    public void onListItemClick(ListView l, View view, int position, long id) {
        int selectedStopCode = ((ProtoStop) getListAdapter().getItem(position)).stop_code;
        Intent intent = new Intent(getActivity(), BusStopMapActivity.class);
        intent.putExtra(BusStopMapActivity.EXTRA_BUSSTOP, selectedStopCode);
        startActivity(intent);
    }

    public void search(String query) {
        if (query.length() == 0) { return; }

        Stops stops = Stops.getInstance(mContext);

        ArrayList<ProtoStop> foundStops = stops.getStopsMatching(query);

        ArrayAdapter<ProtoStop> stopsAdapter = new SearchListAdapter(mContext, foundStops);

        setListAdapter(stopsAdapter);
    }

}
