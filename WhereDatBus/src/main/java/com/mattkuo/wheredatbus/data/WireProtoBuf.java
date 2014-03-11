package com.mattkuo.wheredatbus.data;

import com.squareup.wire.Wire;

public class WireProtoBuf {
    private static Wire sWire = null;

    public static Wire getInstance() {
        if (sWire == null) {
            sWire = new Wire();
        }
        return sWire;
    }

    private WireProtoBuf() {
    }
}
