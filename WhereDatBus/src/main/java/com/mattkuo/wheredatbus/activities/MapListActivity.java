package com.mattkuo.wheredatbus.activities;


import android.app.Fragment;

import com.mattkuo.wheredatbus.fragments.MapListFragment;

public class MapListActivity extends SingleFragmentActivity {
    public Fragment createFragment() {
        return MapListFragment.newInstance(getIntent().getStringExtra(MapListFragment
                .EXTRA_ROUTE_NAME));
    }
}
