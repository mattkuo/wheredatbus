package com.mattkuo.wheredatbus.activities;


import android.app.Fragment;

import com.mattkuo.wheredatbus.fragments.MapListFragment;

public class MapListActivity extends SingleFragmentActivity {
    public Fragment createFragment() {
    	Fragment mapListFragment = new MapListFragment();
    	mapListFragment.setArguments(getIntent().getExtras());
        return mapListFragment;
    }
}
