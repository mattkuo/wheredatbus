package com.mattkuo.wheredatbus.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mattkuo.wheredatbus.model.Schedule;
import com.mattkuo.wheredatbus.model.StopSchedule;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class StopScheduleDeserializer implements JsonDeserializer<StopSchedule> {
    @Override
    public StopSchedule deserialize(final JsonElement json, final Type typeOfT,
                           final JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();

        final String routeNo = jsonObject.get("RouteNo").getAsString();
        final String routeName = jsonObject.get("RouteName").getAsString();
        final String direction = jsonObject.get("Direction").getAsString();

        final JsonArray schedules = jsonObject.getAsJsonArray("Schedules");

        ArrayList<Schedule> listOfSchedules = new ArrayList<>();
        for (JsonElement schedule : schedules) {
            JsonObject obj = schedule.getAsJsonObject();
            String destination = obj.get("Destination").getAsString();
            String expectedLeaveTime = obj.get("ExpectedLeaveTime").getAsString();
            int expectedCountDown = obj.get("ExpectedCountdown").getAsInt();
            String scheduleStatus = obj.get("ScheduleStatus").getAsString();
            boolean cancelledTrip = obj.get("CancelledTrip").getAsBoolean();
            boolean cancelledStop = obj.get("CancelledStop").getAsBoolean();
            boolean addedTrip = obj.get("AddedTrip").getAsBoolean();
            boolean addedStop = obj.get("AddedStop").getAsBoolean();
            String lastUpdate = obj.get("LastUpdate").getAsString();

            listOfSchedules.add(new Schedule(destination, expectedLeaveTime, expectedCountDown, scheduleStatus, cancelledTrip, cancelledStop, addedTrip, addedStop, lastUpdate));

        }

        return new StopSchedule(routeNo, routeName, direction, listOfSchedules);
    }
}