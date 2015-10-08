package com.mattkuo.wheredatbus.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.mattkuo.wheredatbus.R;


public class StopTimesExpListFragment extends Fragment {
    private static final String TAG = "StopTimesExpListFragment";
    private static final String BUS_STOP_CODE = "BUS_STOP_CODE";

    private int mBusStopCode;
    private ExpandableListView mScheduleExpListView;

    public StopTimesExpListFragment() {
    }

    public static StopTimesExpListFragment newInstance(int busStopCode) {
        StopTimesExpListFragment fragment = new StopTimesExpListFragment();
        Bundle args = new Bundle();
        args.putInt(BUS_STOP_CODE, busStopCode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mBusStopCode = getArguments().getInt(BUS_STOP_CODE);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stop_times_exp_list, container, false);
        mScheduleExpListView = (ExpandableListView) view.findViewById(R.id.stoptimes_exp_list);
        mScheduleExpListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long id) {
                ImageView tabImageView = (ImageView) view.findViewById(R.id.tab_down_imageview);

                if (expandableListView.isGroupExpanded(groupPosition)) {
                    tabImageView.setImageResource(R.drawable.tab_down);
                } else {
                    tabImageView.setImageResource(R.drawable.tab_up);
                }

                return false;
            }
        });
        return view;
    }

    public void setAdapter(BaseExpandableListAdapter expandableListAdapter) {
        mScheduleExpListView.setAdapter(expandableListAdapter);
    }


}
