package com.mattkuo.wheredatbus.data;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mattkuo.wheredatbus.interfaces.Translink;
import com.mattkuo.wheredatbus.model.Bus;
import com.mattkuo.wheredatbus.model.Stop;
import com.mattkuo.wheredatbus.model.StopSchedule;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

// TODO: Find a better way to create services
final public class TranslinkService {
    private final static String URL = "http://api.translink.ca";
    private static Translink mBusService = null;
    private static Translink mStopScheduleService = null;
    private static Translink mStopService = null;

    private TranslinkService() {}

    public static Translink getBusService() {
        if (mBusService == null) {
            Gson gson = new GsonBuilder().registerTypeAdapter(Bus.class,
                    new BusDeserializer()).create();

            mBusService = new RestAdapter.Builder().setEndpoint(URL).setConverter(new GsonConverter
                    (gson)).build().create(Translink.class);
        }
        return mBusService;
    }

    public static Translink getStopScheduleService() {
        if (mStopScheduleService == null) {
            Gson gson = new GsonBuilder().registerTypeAdapter(StopSchedule.class,
                    new StopScheduleDeserializer()).create();
            RestAdapter.Builder builder = new RestAdapter.Builder();
            builder.setLogLevel(RestAdapter.LogLevel.FULL).setLog(new RestAdapter.Log() {
                public void log(String msg) {
                    Log.i("TranslinkService", msg);
                }
            });
            mStopScheduleService = builder.setEndpoint(URL).setConverter(new GsonConverter
                    (gson)).build().create(Translink.class);
        }
        return mStopScheduleService;
    }

    public static Translink getStopService() {
        if (mStopService == null) {
            Gson gson = new GsonBuilder().registerTypeAdapter(Stop.class, new StopDeserializer()).create();
            RestAdapter.Builder builder = new RestAdapter.Builder();
            builder.setLogLevel(RestAdapter.LogLevel.FULL).setLog(new RestAdapter.Log() {
                public void log(String msg) {
                    Log.i("getStopService", msg);
                }
            });
            mStopService = builder.setEndpoint(URL).setConverter(new GsonConverter(gson)).build().create(Translink.class);
        }

        return mStopService;
    }
}
