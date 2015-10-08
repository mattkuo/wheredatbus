package com.mattkuo.wheredatbus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mattkuo.wheredatbus.R;
import com.mattkuo.wheredatbus.protobuff.ProtoStop;

import java.util.ArrayList;

public class SearchListAdapter extends ArrayAdapter<ProtoStop> {
    Context mContext;

    public SearchListAdapter (Context context, ArrayList<ProtoStop> routes) {
        super(context, 0, routes);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context
                    .LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.item_search_list_row, null);
        }

        ProtoStop ps = getItem(position);

        TextView text = (TextView) convertView.findViewById(R.id.adapter_search_list_row_text);
        text.setText(ps.stop_code + " - " + ps.stop_name);

        return convertView;
    }
}
