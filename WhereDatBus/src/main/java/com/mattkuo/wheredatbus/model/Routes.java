package com.mattkuo.wheredatbus.model;

import android.content.Context;
import android.util.Log;

import com.mattkuo.wheredatbus.data.WireProtoBuf;
import com.mattkuo.wheredatbus.protobuff.ProtoRoute;
import com.mattkuo.wheredatbus.protobuff.ProtoRoutes;
import com.mattkuo.wheredatbus.protobuff.ProtoShape;
import com.squareup.wire.Wire;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Routes {
    private static final String TAG = "Routes";

    private static Routes sRoutes = null;
    HashMap<String, ProtoRoute> mRouteMap = new HashMap<>();
    ArrayList<String> mRouteList = new ArrayList<>();
    Context mContext;
    private ProtoRoutes mProtoRoutes;

    public static Routes getInstance(Context context) {
        if (sRoutes == null) {
            sRoutes = new Routes(context);
        }
        if (sRoutes.mProtoRoutes == null) {
            sRoutes = new Routes(context);
        }

        return sRoutes;
    }

    private Routes(Context context) {
        mContext = context;
        Wire wire = WireProtoBuf.getInstance();

        try {
            mProtoRoutes = wire.parseFrom(context.getAssets().open("routes"), ProtoRoutes.class);

            for (ProtoRoute protoRoute : mProtoRoutes.routes) {
                mRouteList.add(protoRoute.route_short);
                mRouteMap.put(protoRoute.route_short, protoRoute);
            }
        } catch (IOException e) {
            Log.e(TAG, "Problem opening routes file: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public ProtoRoute getRoute(String routeShort) {
        return mRouteMap.get(routeShort);
    }

    public ArrayList<String> getRouteList() {
        return mRouteList;
    }

    public List<ProtoRoute> getRoutes() {
        return mProtoRoutes.routes;
    }

    public ProtoShape getShape(String routeNum) {
        Wire wire = WireProtoBuf.getInstance();
        try {
            return wire.parseFrom(mContext.getAssets().open(routeNum + ".shape"), ProtoShape.class);
        } catch (IOException e) {
            Log.e(TAG, "Problem opening shape file: " + e.getMessage());
        }
        return null;
    }
}
