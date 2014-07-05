package com.mattkuo.wheredatbus.data;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mattkuo.wheredatbus.interfaces.Translink;
import com.mattkuo.wheredatbus.model.Bus;
import com.mattkuo.wheredatbus.model.StopSchedule;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

final public class TranslinkService {
    private final static String URL = "http://api.translink.ca";
    private static Translink busService = null;
    private static Translink stopService = null;

    private TranslinkService() {}

    public static Translink getBusService() {
        if (busService == null) {
            Gson gson = new GsonBuilder().registerTypeAdapter(Bus.class,
                    new BusDeserializer()).create();

            busService = new RestAdapter.Builder().setEndpoint(URL).setConverter(new GsonConverter
                    (gson)).build().create(Translink.class);
        }
        return busService;
    }

    public static Translink getStopService() {
        if (stopService == null) {
            Gson gson = new GsonBuilder().registerTypeAdapter(StopSchedule.class,
                    new StopScheduleDeserializer()).create();
            RestAdapter.Builder builder = new RestAdapter.Builder();
            builder.setLogLevel(RestAdapter.LogLevel.FULL).setLog(new RestAdapter.Log() {
                public void log(String msg) {
                    Log.i("TranslinkService", msg);
                }
            });
            stopService = builder.setEndpoint(URL).setConverter(new GsonConverter
                    (gson)).build().create(Translink.class);
        }
        return stopService;
    }
}
