package com.mattkuo.wheredatbus.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
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
        String routeName = ((StopSchedule) getGroup(groupPosition)).getRouteName();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.explistview_header, null);
        }

        TextView route = (TextView) convertView.findViewById(R.id.bus_route_header);
        route.setText(routeNumber + " - " + routeName);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup viewGroup) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.explistview_children, viewGroup, false);
        }

        TextView scheduleDestination = (TextView) view.findViewById(R.id.schedule_destination);
        TextView scheduleTime = (TextView) view.findViewById(R.id.schedule_time);
        ImageView clockIcon = (ImageView) view.findViewById(R.id.schedule_status);

        String dest = mStopSchedules.get(groupPosition).getSchedules().get(childPosition).getDestination();
        String leaveTime = mStopSchedules.get(groupPosition).getSchedules().get(childPosition).getExpectedLeaveTime();
        String status = mStopSchedules.get(groupPosition).getSchedules().get(childPosition).getScheduleStatus();

        scheduleDestination.setText(dest);
        scheduleTime.setText(leaveTime);

        switch (status) {
            case "+": // Early
                clockIcon.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
                break;
            case "-": // Late
                clockIcon.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                break;
            case "*": // Scheduled
                clockIcon.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
            default:
                clockIcon.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                break;
        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
