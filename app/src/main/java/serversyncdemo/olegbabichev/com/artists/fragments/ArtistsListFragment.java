package serversyncdemo.olegbabichev.com.artists.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import serversyncdemo.olegbabichev.com.artists.R;

/**
 * Created by olegchuikin on 13/04/16.
 */
public class ArtistsListFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.artists_list_layout, container, false);
    }

}
