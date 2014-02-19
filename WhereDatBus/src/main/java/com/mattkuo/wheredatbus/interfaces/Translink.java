package com.mattkuo.wheredatbus.interfaces;

import com.mattkuo.wheredatbus.model.Bus;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Query;

public interface Translink {

    @Headers("accept: application/JSON")
    @GET("/rttiapi/v1/buses")
    void listOfBusForRoute(
            @Query("routeNo") String routeNo,
            @Query("apikey") String apiKey,
            Callback<List<Bus>> callback
    );

}
