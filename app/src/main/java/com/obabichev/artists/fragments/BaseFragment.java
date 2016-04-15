package com.obabichev.artists.fragments;

import android.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.obabichev.artists.MainActivity;

/**
 * Created by obabichev on 13/04/16.
 */
public abstract class BaseFragment extends Fragment {

    public void applyFragment(BaseFragment baseFragment) {
        MainActivity ma = (MainActivity) getActivity();
        if (ma == null) {
            return;
        }
        ma.applyFragment(baseFragment);
    }

    protected void finish() {
        MainActivity ma = (MainActivity) getActivity();
        if (ma == null) {
            return;
        }
        ma.finishFragment();
    }

    protected View findViewById(int id) {
        MainActivity ma = (MainActivity) getActivity();
        if (ma == null) {
            return null;
        }
        return ma.findViewById(id);
    }

    protected void showMenuBackButton(boolean show){
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(show);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getTitle());
        showMenuBackButton(false);
    }

    protected String getTitle() {
        return ((MainActivity) getActivity()).getTitle().toString();
    }

}
