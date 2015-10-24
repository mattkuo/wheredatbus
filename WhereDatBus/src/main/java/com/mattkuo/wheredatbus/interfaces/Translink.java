package com.mattkuo.wheredatbus.interfaces;

import com.mattkuo.wheredatbus.model.Bus;
import com.mattkuo.wheredatbus.model.Stop;
import com.mattkuo.wheredatbus.model.StopSchedule;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import retrofit.http.Query;

public interface Translink {

    @Headers("accept: application/JSON")
    @GET("/rttiapi/v1/buses")
    void listOfBusForRoute(
        @Query("routeNo") String routeNo,
        @Query("apikey") String apiKey,
        Callback<List<Bus>> callback
    );

    @Headers("accept: application/JSON")
    @GET("/rttiapi/v1/stops/{stopCode}/estimates")
    void timesForRoute(
        @Path("stopCode") int stopCode,
        @Query("apikey") String apiKey,
        Callback<List<StopSchedule>> callback
    );

    @Headers("accept: application/JSON")
    @GET("/rttiapi/v1/stops/")
    void listOfStopsForLatLng(
        @Query("radius") int radius,
        @Query("lat") double lat,
        @Query("long") double lng,
        @Query("apikey") String apiKey,
        Callback<List<Stop>> callback
    );

}
