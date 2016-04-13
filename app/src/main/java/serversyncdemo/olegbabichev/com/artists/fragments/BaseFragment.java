package serversyncdemo.olegbabichev.com.artists.fragments;

import android.app.Fragment;
import android.view.View;

import serversyncdemo.olegbabichev.com.artists.MainActivity;

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

    @Override
    public void onResume() {
        getActivity().setTitle(getTitle());
        super.onResume();
    }

    protected String getTitle() {
        return ((MainActivity) getActivity()).getTitle().toString();
    }

}
