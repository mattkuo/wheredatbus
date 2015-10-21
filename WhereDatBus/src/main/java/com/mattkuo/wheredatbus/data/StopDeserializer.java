package com.mattkuo.wheredatbus.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mattkuo.wheredatbus.model.Stop;

import java.lang.reflect.Type;

public class StopDeserializer implements JsonDeserializer<Stop> {
    @Override
    public Stop deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();

        final int stopCode = jsonObject.get("StopNo").getAsInt();
        final String name = jsonObject.get("Name").getAsString();
        final double latitude = jsonObject.get("Latitude").getAsDouble();
        final double longitude = jsonObject.get("Longitude").getAsDouble();

        final int wheelChair = jsonObject.get("WheelchairAccess").getAsInt();
        boolean isWheelChairAccessable = wheelChair == 1;

        String[] stringRoutes = jsonObject.get("Routes").getAsString().split(", ");
        Integer[] intRoutes = new Integer[stringRoutes.length];

        for (int i = 0; i < stringRoutes.length; i++) {
            intRoutes[i] = Integer.parseInt(stringRoutes[i]);
        }

        return new Stop(stopCode, name, latitude, longitude, isWheelChairAccessable, intRoutes);

    }
}