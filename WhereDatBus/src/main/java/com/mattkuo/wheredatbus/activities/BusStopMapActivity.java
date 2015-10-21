package com.mattkuo.wheredatbus.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mattkuo.wheredatbus.R;
import com.mattkuo.wheredatbus.adapters.ScheduleExpListAdapter;
import com.mattkuo.wheredatbus.data.ApiErrorDeserializer;
import com.mattkuo.wheredatbus.data.TranslinkService;
import com.mattkuo.wheredatbus.fragments.StopTimesExpListFragment;
import com.mattkuo.wheredatbus.fragments.TransitDataMapFragment;
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

public class BusStopMapActivity extends Activity implements TransitDataMapFragment.MapsLoadedListener {
    public static final String EXTRA_BUSSTOP = "com.mattkuo.WhereDatBus.EXTRA_BUSSTOP";
    private static final String TAG = "BusStopMapActivity";

    private TransitDataMapFragment mMapFragment;
    private StopTimesExpListFragment mStopTimesExpListFragment;
    private int mBusStopCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_list);
        FragmentManager fm = getFragmentManager();

        mBusStopCode = getIntent().getExtras().getInt(EXTRA_BUSSTOP);

        if (getActionBar() != null) getActionBar().setTitle("Stop: " + mBusStopCode);

        mMapFragment = TransitDataMapFragment.newInstance(mBusStopCode);

        mStopTimesExpListFragment = StopTimesExpListFragment.newInstance(mBusStopCode);
        fm.beginTransaction().replace(R.id.map_data_list, mStopTimesExpListFragment)
                .replace(R.id.map_container, mMapFragment).commit();

        // Get stop data
        TranslinkService.getStopScheduleService().timesForRoute(mBusStopCode,
                getResources().getString(R.string.translink), new Callback<List<StopSchedule>>() {
                    @Override
                    public void success(List<StopSchedule> stopSchedules, Response response) {
                        ArrayList<StopSchedule> listOfStopSchedules = new ArrayList<>();
                        if (mBusStopCode != 0) {
                            listOfStopSchedules.addAll(stopSchedules);
                        }

                        ScheduleExpListAdapter scheduleExpListAdapter = new
                                ScheduleExpListAdapter(listOfStopSchedules, BusStopMapActivity.this);
                        mStopTimesExpListFragment.setAdapter(scheduleExpListAdapter);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (error.getResponse() == null) {
                            Toast.makeText(BusStopMapActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }

                        Log.e(TAG, error.getResponse().getReason());
                        try {
                            Reader reader = new InputStreamReader(error.getResponse().getBody()
                                    .in());
                            Gson gson = new GsonBuilder().registerTypeAdapter(ApiError.class,
                                    new ApiErrorDeserializer()).create();
                            ApiError apiError = gson.fromJson(reader, ApiError.class);

                            Toast toast = Toast.makeText(BusStopMapActivity.this, apiError.getMessage(),
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
    public void onMapsLoaded() {
        mMapFragment.plotStop();
    }
}
