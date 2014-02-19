package com.mattkuo.wheredatbus.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mattkuo.wheredatbus.model.Bus;

import java.util.ArrayList;
import java.util.List;

public class BusListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Bus> mBusList = new ArrayList<Bus>();

    public BusListAdapter(Context context) {
        super();
        mContext = context;
    }

    public void setBusList(List<Bus> buses) {
        mBusList = buses;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mBusList.size();
    }

    @Override
    public Object getItem(int i) {
        return mBusList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
