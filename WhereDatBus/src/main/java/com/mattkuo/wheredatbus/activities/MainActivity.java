package com.mattkuo.wheredatbus.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.mattkuo.wheredatbus.R;
import com.mattkuo.wheredatbus.fragments.RouteDirectoryFragment;
import com.mattkuo.wheredatbus.fragments.SearchBusStopFragment;

public class MainActivity extends Activity {
    private SearchBusStopFragment mSearchFragment;
    private RouteDirectoryFragment mRouteDirectoryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.handleIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        this.handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (mSearchFragment == null) {
                mSearchFragment = SearchBusStopFragment.newInstance(query);
            } else {
                mSearchFragment.search(query);
            }

            swapFragment(mSearchFragment);
        }
    }

    private void swapFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_routes:
                if (mRouteDirectoryFragment == null) {
                    mRouteDirectoryFragment = RouteDirectoryFragment.newInstance();
                }
                this.swapFragment(mRouteDirectoryFragment);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
