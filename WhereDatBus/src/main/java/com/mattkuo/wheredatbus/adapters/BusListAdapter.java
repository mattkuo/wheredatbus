package com.mattkuo.wheredatbus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mattkuo.wheredatbus.R;
import com.mattkuo.wheredatbus.model.Bus;

import java.util.ArrayList;

/**
 * Adapter to hold a list of buses
 */
public class BusListAdapter extends ArrayAdapter<Bus> {

    private Context mContext;

    public BusListAdapter(Context context, ArrayList<Bus> buses) {
        super(context, 0, buses);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context
                    .LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_bus_list_row, null);
        }

        // Configure the view for this Bus
        Bus bus = getItem(position);

        TextView routeNoTextView = (TextView) convertView.findViewById(R.id
                .route_list_item_route_no);
        routeNoTextView.setText(bus.getRouteNumber());

        TextView directionTextView = (TextView) convertView.findViewById(R.id
                .route_list_item_direction);
        directionTextView.setText(bus.getDirection());

        TextView destinationTextView = (TextView) convertView.findViewById(R.id
                .route_list_item_destination);
        destinationTextView.setText(bus.getDestination());

        TextView timeTextView = (TextView) convertView.findViewById(R.id
                .route_list_item_recorded_time);
        timeTextView.setText("Last Update: " + bus.getRecordedTime());

        return convertView;
    }
}
