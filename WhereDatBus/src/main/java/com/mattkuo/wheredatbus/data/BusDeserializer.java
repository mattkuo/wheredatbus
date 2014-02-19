package com.mattkuo.wheredatbus.data;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mattkuo.wheredatbus.model.Bus;

import java.lang.reflect.Type;

public class BusDeserializer implements JsonDeserializer<Bus> {
    @Override
    public Bus deserialize(final JsonElement json, final Type typeOfT,
                           final JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();

        final String vehicleNo = jsonObject.get("VehicleNo").getAsString();
        final String routeNo = jsonObject.get("RouteNo").getAsString();
        final String direction = jsonObject.get("Direction").getAsString();
        final String destination = jsonObject.get("Destination").getAsString();
        final String recordedTime = jsonObject.get("RecordedTime").getAsString();
        final double latitude = jsonObject.get("Latitude").getAsDouble();
        final double longitude = jsonObject.get("Longitude").getAsDouble();

        final LatLng latLng = new LatLng(latitude, longitude);

        return new Bus(vehicleNo, routeNo, direction, destination, latLng, recordedTime);
    }
}
