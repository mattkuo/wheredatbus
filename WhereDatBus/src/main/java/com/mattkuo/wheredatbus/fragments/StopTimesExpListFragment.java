package com.mattkuo.wheredatbus.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mattkuo.wheredatbus.R;
import com.mattkuo.wheredatbus.adapters.ScheduleExpListAdapter;
import com.mattkuo.wheredatbus.data.ApiErrorDeserializer;
import com.mattkuo.wheredatbus.data.TranslinkService;
import com.mattkuo.wheredatbus.model.ApiError;
import com.mattkuo.wheredatbus.model.StopSchedule;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class StopTimesExpListFragment extends Fragment {
    private static final String TAG = "StopTimesExpListFragment";
    private static final String BUS_STOP_CODE = "BUS_STOP_CODE";

    private int mBusStopCode;
    private ExpandableListView mScheduleExpListView;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StopTimesExpListFragment() {
    }

    public static StopTimesExpListFragment newInstance(int busStopCode) {
        StopTimesExpListFragment fragment = new StopTimesExpListFragment();
        Bundle args = new Bundle();
        args.putInt(BUS_STOP_CODE, busStopCode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mBusStopCode = getArguments().getInt(BUS_STOP_CODE);
        }

        TranslinkService.getStopService().timesForRoute(mBusStopCode,
                getResources().getString(R.string.translink), new Callback<List<StopSchedule>>() {
                    @Override
                    public void success(List<StopSchedule> stopSchedules, Response response) {
                        ArrayList<StopSchedule> listOfStopSchedules = new ArrayList<>();
                        if (mBusStopCode != 0) {
                            listOfStopSchedules.addAll(stopSchedules);
                        }

                        ScheduleExpListAdapter scheduleExpListAdapter = new
                                ScheduleExpListAdapter(listOfStopSchedules, getActivity());
                        mScheduleExpListView.setAdapter(scheduleExpListAdapter);

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.i(TAG, error.getResponse().getReason());

                        try {
                            Reader reader = new InputStreamReader(error.getResponse().getBody()
                                    .in());
                            Gson gson = new GsonBuilder().registerTypeAdapter(ApiError.class,
                                    new ApiErrorDeserializer()).create();
                            ApiError apiError = gson.fromJson(reader, ApiError.class);

                            Toast toast = Toast.makeText(getActivity(), apiError.getMessage(),
                                    Toast.LENGTH_LONG);
                            toast.show();

                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }

                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stop_times_explistview, container, false);
        mScheduleExpListView = (ExpandableListView) view.findViewById(R.id.stoptimes_explistview);
        return view;
    }


}
