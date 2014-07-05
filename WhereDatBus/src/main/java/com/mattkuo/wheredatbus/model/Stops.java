package com.mattkuo.wheredatbus.model;


import android.content.Context;
import android.util.Log;

import com.mattkuo.wheredatbus.data.WireProtoBuf;
import com.mattkuo.wheredatbus.protobuff.ProtoStop;
import com.mattkuo.wheredatbus.protobuff.ProtoStops;
import com.squareup.wire.Wire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Stops {
    private static final String TAG = "com.mattkuo.model.Stops";
    private static Stops mStops;
    HashMap<Integer, ProtoStop> mProtoStopHashMap = new HashMap<Integer, ProtoStop>();
    private ProtoStops mProtoStops;

    private Stops(Context context) {
        Wire wire = WireProtoBuf.getInstance();
        try {
            setStops(wire.parseFrom(context.getAssets().open("stops"), ProtoStops.class));
            for (ProtoStop stop : getStops().stops) {
                mProtoStopHashMap.put(stop.stop_code, stop);
            }

        } catch (Exception localException) {
            Log.e(TAG, "Cant open stops file! " + localException.getMessage(), localException);
        }
    }

    public static Stops getInstance(Context context) {
        if (mStops == null || mStops.mProtoStops == null) {
            mStops = new Stops(context);
        }
        return mStops;
    }

    public ProtoStop getStop(Integer id) {
        return mProtoStopHashMap.get(Integer.valueOf(id));
    }

    public ProtoStops getStops() {
        return mProtoStops;
    }

    public ArrayList<ProtoStop> getStopsMatching(String id) {
        ArrayList<ProtoStop> array = new ArrayList<>();
        for (Map.Entry<Integer, ProtoStop> e : mProtoStopHashMap.entrySet()) {
            if (e.getKey().toString().contains(id)) {
                array.add(e.getValue());
            }
        }

        return array;
    }

    public void setStops(ProtoStops paramStops) {
        mProtoStops = paramStops;
    }
}
