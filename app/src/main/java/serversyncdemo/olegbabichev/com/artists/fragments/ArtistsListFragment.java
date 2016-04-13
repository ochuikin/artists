package serversyncdemo.olegbabichev.com.artists.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import serversyncdemo.olegbabichev.com.artists.MainActivity;
import serversyncdemo.olegbabichev.com.artists.R;
import serversyncdemo.olegbabichev.com.artists.adapters.ArtistsAdapter;
import serversyncdemo.olegbabichev.com.artists.model.Artist;
import serversyncdemo.olegbabichev.com.artists.network.HttpDownloaderAsyncTask;

/**
 * Created by obabichev 13/04/16.
 */
public class ArtistsListFragment extends BaseFragment implements ChangingDataObserver<Artist> {

    private ListView artistsListView;
    private List<Artist> artists;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new HttpDownloaderAsyncTask(MainActivity.JSON_ARTISTS_URL, this).execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.artists_list_layout, container, false);

        artistsListView = (ListView) result.findViewById(R.id.artists_list_view);

        return result;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        setupAdapter();
    }

    private void setupAdapter() {
        if (getActivity() == null || artistsListView == null) return;
        if (artists != null) {
            artistsListView.setAdapter(new ArtistsAdapter(getActivity(), artists));
        } else {
            artistsListView.setAdapter(null);
        }
    }


    @Override
    public void handleChangingData(List<Artist> newData) {
        this.artists = newData;
        setupAdapter();
    }
}
