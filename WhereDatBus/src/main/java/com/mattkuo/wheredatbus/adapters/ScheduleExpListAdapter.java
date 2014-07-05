package com.mattkuo.wheredatbus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.mattkuo.wheredatbus.R;
import com.mattkuo.wheredatbus.model.StopSchedule;

import java.util.ArrayList;

public class ScheduleExpListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private ArrayList<StopSchedule> mStopSchedules;

    public ScheduleExpListAdapter(ArrayList<StopSchedule> listOfStopSchedule,  Context context) {
        this.mContext = context;
        this.mStopSchedules = listOfStopSchedule;
    }

    @Override
    public int getGroupCount() {
        return mStopSchedules.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mStopSchedules.get(groupPosition).getSchedules().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mStopSchedules.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mStopSchedules.get(groupPosition).getSchedules().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String routeNumber = ((StopSchedule) getGroup(groupPosition)).getRouteNo();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.explistview_header, null);
        }

        TextView route = (TextView) convertView.findViewById(R.id.bus_route_header);
        route.setText(routeNumber);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup viewGroup) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.explistview_children, viewGroup, false);
        }

        TextView scheduleChildren = (TextView) view.findViewById(R.id.schedule_children);

        String leaveTime = mStopSchedules.get(groupPosition).getSchedules().get(childPosition).getExpectedLeaveTime();

        scheduleChildren.setText(leaveTime);
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
