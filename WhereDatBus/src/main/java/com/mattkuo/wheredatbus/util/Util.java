package com.mattkuo.wheredatbus.util;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

public class Util {
    public static void displayPromptForEnablingGPS(final Activity activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        final String message = "Enable either GPS or any other location"
                + " service to find current location.  Click OK to go to"
                + " location services settings to let you do so.";

        builder.setMessage(message)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                activity.startActivity(new Intent(action));
                                d.dismiss();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                d.cancel();
                            }
                        });
        builder.create().show();
    }

    public static double distance(double lat, double lng, double lat2, double lng2) {
        double radius = 6378.137;
        double dLat = (lat2 - lat) * Math.PI / 180;
        double dLon = (lng2 - lng) * Math.PI / 180;
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(lat * Math.PI / 180)
                * Math.cos(lat2 * Math.PI / 180) * Math.sin(dLon/2) * Math.sin(dLon/2);
        double circumference = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = radius * circumference;
        return distance * 1000;
    }

    public static double roundToNDecimals(double number, double n) {
        double exponented = Math.pow(10, n);
        return Math.round(number * exponented) / exponented;
    }

    public static String joinString(String[] strArray, String delimiter) {
        if (strArray.length < 1) return "";
        else if (strArray.length == 1) return strArray[0];

        StringBuilder sb = new StringBuilder();
        sb.append(strArray[0]);

        for (int i = 1; i < strArray.length; i++) {
            sb.append(delimiter);
            sb.append(strArray[i]);
        }

        return sb.toString();
    }
}
