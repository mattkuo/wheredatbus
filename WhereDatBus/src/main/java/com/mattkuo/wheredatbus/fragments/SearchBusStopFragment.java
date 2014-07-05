package com.mattkuo.wheredatbus.fragments;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.mattkuo.wheredatbus.R;
import com.mattkuo.wheredatbus.activities.BusStopMapActivity;
import com.mattkuo.wheredatbus.adapters.SearchListAdapter;
import com.mattkuo.wheredatbus.model.Stops;
import com.mattkuo.wheredatbus.protobuff.ProtoStop;

import java.util.ArrayList;


public class SearchBusStopFragment extends ListFragment {
    private static final String TAG = "com.mattkuo.SearchBusStopFragment";
    private TextView mSearchText;

    public SearchBusStopFragment() {}

    public static SearchBusStopFragment newInstance() {
        return new SearchBusStopFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        mSearchText = (TextView) view.findViewById(R.id.search_fragment_search_text);
        ImageButton searchButton = (ImageButton) view.findViewById(R.id
                .search_fragment_search_button);
        searchButton.setOnClickListener(new SearchListener());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        search();
    }

    @Override
    public void onListItemClick(ListView l, View view, int position, long id) {
        int selectedStopCode = ((ProtoStop) getListAdapter().getItem(position)).stop_code;
        Log.d(TAG, "Selected Stop - " + selectedStopCode);

        Intent intent = new Intent(getActivity(), BusStopMapActivity.class);
        intent.putExtra(BusStopMapActivity.EXTRA_BUSSTOP, selectedStopCode);
        startActivity(intent);
    }

    private void search() {
        String query = mSearchText.getText().toString();
        if (query != null && query.length() != 0) {
            Stops stops = Stops.getInstance(getActivity().getApplicationContext());

            ArrayList<ProtoStop> foundStops = stops.getStopsMatching(query);

            ArrayAdapter<ProtoStop> stopsAdapter = new SearchListAdapter(getActivity().getApplication(), foundStops);

            setListAdapter(stopsAdapter);
        }
    }

    private class SearchListener implements ImageButton.OnClickListener {
        @Override
        public void onClick(View view) {
            search();
        }
    }
}
