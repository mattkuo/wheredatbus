package com.mattkuo.wheredatbus.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mattkuo.wheredatbus.R;
import com.mattkuo.wheredatbus.adapters.BusListAdapter;
import com.mattkuo.wheredatbus.data.TranslinkService;
import com.mattkuo.wheredatbus.model.Bus;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class BusListFragment extends ListFragment {
    final static String SHORT_ROUTE_NAME = "com.mattkuo.wheredatbus.short_route_name";

    String mRouteName;

    public static BusListFragment newInstance(String shortRouteName) {
        Bundle bundle = new Bundle();
        bundle.putString(SHORT_ROUTE_NAME, shortRouteName);
        BusListFragment busListFragment = new BusListFragment();
        busListFragment.setArguments(bundle);
        return busListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRouteName = getArguments().getString(SHORT_ROUTE_NAME);

        TranslinkService.getService().listOfBusForRoute(mRouteName, getResources().getString(R.string
                .translink), new Callback<List<Bus>>() {
            @Override
            public void success(List<Bus> buses, Response response) {

                ArrayList<Bus> busList = new ArrayList<>();

                if (mRouteName != null) {
                    busList.addAll(buses);
                } else {
                    ((TextView) getView().findViewById(android.R.id.empty)).setText(mRouteName);
                }

                ArrayAdapter busAdapter = new BusListAdapter(getActivity().getApplicationContext
                        (), busList);
                setListAdapter(busAdapter);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                // TODO Error check
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bus_list, container, false);
    }


}
