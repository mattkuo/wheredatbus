package com.mattkuo.wheredatbus.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mattkuo.wheredatbus.model.ApiError;

import java.lang.reflect.Type;

public class ApiErrorDeserializer implements JsonDeserializer<ApiError>{
    @Override
    public ApiError deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext
            context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String code = jsonObject.get("Code").getAsString();
        String message = jsonObject.get("Message").getAsString();

        return new ApiError(code, message);
    }
}
