package com.mattkuo.wheredatbus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mattkuo.wheredatbus.R;
import com.mattkuo.wheredatbus.protobuff.ProtoRoute;

import java.util.ArrayList;


public class RouteListAdapter extends ArrayAdapter<ProtoRoute> {
    Context mContext;

    public RouteListAdapter(Context context, ArrayList<ProtoRoute> routes) {
        super(context, 0, routes);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context
                    .LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_route_list_row, null);
        }

        ProtoRoute pr = getItem(position);

        TextView text = (TextView) convertView.findViewById(R.id.adapter_route_list_row_text);
        text.setText(pr.route_short + " - " + pr.route_long);

        return convertView;
    }
}
