package com.mattkuo.wheredatbus.fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mattkuo.wheredatbus.R;
import com.mattkuo.wheredatbus.adapters.BusListAdapter;
import com.mattkuo.wheredatbus.data.TranslinkService;
import com.mattkuo.wheredatbus.model.Bus;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Fragment that holds a listview for a list of buses for a route
 */
public class BusListFragment extends ListFragment {
    final static String EXTRA_SHORT_ROUTE_NAME = "com.mattkuo.wheredatbus.EXTRA_SHORT_ROUTE_NAME";

    private String mRouteName;
    private BusListListener mBusListListener;
    private Context mContext;

    // Activity needs to implement to listen to changes
    public interface BusListListener {
        public void onBusListLoaded(ArrayList<Bus> listOfBuses);
        public void onBusListItemClick(Bus bus);
    }

    public static BusListFragment newInstance(String shortRouteName) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_SHORT_ROUTE_NAME, shortRouteName);
        BusListFragment busListFragment = new BusListFragment();
        busListFragment.setArguments(bundle);
        return busListFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mContext = getActivity().getApplicationContext();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRouteName = getArguments().getString(EXTRA_SHORT_ROUTE_NAME);

        TranslinkService.getBusService().listOfBusForRoute(mRouteName, getResources().getString(R.string
                .translink), new Callback<List<Bus>>() {
            @Override
            public void success(List<Bus> buses, Response response) {

                ArrayList<Bus> busList = new ArrayList<>();

                if (mRouteName != null) {
                    busList.addAll(buses);
                }

                ArrayAdapter busAdapter = new BusListAdapter(mContext, busList);
                setListAdapter(busAdapter);
                mBusListListener.onBusListLoaded(busList);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                // TODO Error check
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Bus bus = (Bus) l.getItemAtPosition(position);
        mBusListListener.onBusListItemClick(bus);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bus_list, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mBusListListener = (BusListListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mBusListListener = null;
    }
}
