package com.mattkuo.wheredatbus.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mattkuo.wheredatbus.R;
import com.mattkuo.wheredatbus.interfaces.Translink;
import com.mattkuo.wheredatbus.model.Bus;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

final public class TranslinkService {
    private final static String URL = "http://api.translink.ca";
    private static Translink service = null;

    private TranslinkService() {}

    public static Translink getService() {
        if (service == null) {
            Gson gson = new GsonBuilder().registerTypeAdapter(Bus.class,
                    new BusDeserializer()).create();
            service = new RestAdapter.Builder().setEndpoint(URL).setConverter(new GsonConverter
                    (gson)).build().create(Translink.class);
        }
        return service;
    }
}
