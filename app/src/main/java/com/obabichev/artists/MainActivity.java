package com.obabichev.artists;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.obabichev.artists.fragments.ArtistsListFragment;
import com.obabichev.artists.fragments.BaseFragment;

public class MainActivity extends AppCompatActivity {

    public static final String JSON_ARTISTS_URL = "http://download.cdn.yandex.net/mobilization-2016/artists.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_reply_white_24dp);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getFragmentManager().getBackStackEntryCount() == 0) {
            applyFragment(new ArtistsListFragment());
        }
    }

    public void applyFragment(BaseFragment baseFragment) {
        if (findViewById(R.id.main_activity_fragment) == null) {
            return;
        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.main_activity_fragment, baseFragment)
                .addToBackStack(null)
                .commit();
    }

    public void finishFragment() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount() <= 1) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getFragmentManager().getBackStackEntryCount() > 1) {
                    onBackPressed();
                }
                break;
        }
        return true;
    }
}
