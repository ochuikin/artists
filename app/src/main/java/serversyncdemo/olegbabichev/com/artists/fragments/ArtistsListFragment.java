package serversyncdemo.olegbabichev.com.artists.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import serversyncdemo.olegbabichev.com.artists.MainActivity;
import serversyncdemo.olegbabichev.com.artists.R;
import serversyncdemo.olegbabichev.com.artists.adapters.ArtistsAdapter;
import serversyncdemo.olegbabichev.com.artists.model.Artist;
import serversyncdemo.olegbabichev.com.artists.network.HttpDownloaderAsyncTask;
import serversyncdemo.olegbabichev.com.artists.network.PictureDownloader;

import static serversyncdemo.olegbabichev.com.artists.fragments.ArtistsListFragment.FragmentState.*;
import static serversyncdemo.olegbabichev.com.artists.fragments.ArtistsListFragment.FragmentState.LOADED;

/**
 * Created by obabichev 13/04/16.
 */
public class ArtistsListFragment extends BaseFragment implements ChangingDataObserver<Artist> {

    private ListView artistsListView;

    private List<Artist> artists;
    private Button reloadDataButton;

    private PictureDownloader<ArtistsAdapter.ViewHolder> pictureDownloaderThread;

    private final AdapterView.OnItemClickListener onListViewItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            applyFragment(ArtistDetailsFragment.create(getActivity(), (Artist) artistsListView.getAdapter().getItem(position)));
        }
    };

    private final View.OnClickListener reloadDataOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switchState(LOADING);
            new HttpDownloaderAsyncTask(MainActivity.JSON_ARTISTS_URL, ArtistsListFragment.this).execute();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new HttpDownloaderAsyncTask(MainActivity.JSON_ARTISTS_URL, this).execute();

        pictureDownloaderThread = new PictureDownloader<>(new Handler());
        pictureDownloaderThread.start();
        pictureDownloaderThread.getLooper();
        pictureDownloaderThread.setListener(new PictureDownloader.Listener<ArtistsAdapter.ViewHolder>() {
            @Override
            public void onPictureDownloaded(ArtistsAdapter.ViewHolder holder, Bitmap picture) {
                holder.coverSmall.setVisibility(View.VISIBLE);
                holder.progressBar.setVisibility(View.GONE);
                if (holder != null && picture != null) {
                    holder.coverSmall.setImageBitmap(picture);
                } else {
                    holder.coverSmall.setImageResource(R.drawable.cover_downloading_faild);
                }
            }
        });
        Log.i("Picture downloader", "PictureDownloader started");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.artists_list_fragment, container, false);

        artistsListView = (ListView) result.findViewById(R.id.artists_list_view);
        reloadDataButton = (Button) result.findViewById(R.id.reloadDataButton);
        reloadDataButton.setOnClickListener(reloadDataOnClickListener);

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
            switchState(LOADED);
            artistsListView.setAdapter(new ArtistsAdapter(this, artists));
            artistsListView.setOnItemClickListener(onListViewItemClickListener);
            Log.i("", "JSON downloaded");
        } else {
            artistsListView.setAdapter(null);
        }
    }

    @Override
    protected String getTitle() {
        return getActivity().getString(R.string.artists_list_fragment_title);
    }

    @Override
    public void handleChangingData(List<Artist> newData) {
        if (newData != null) {
            this.artists = newData;
            setupAdapter();
        } else {
            switchState(ERROR);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        pictureDownloaderThread.quit();
        Log.i("Picture downloader", "PictureDownloader stopped");

        pictureDownloaderThread.clearQueue();
    }

    public PictureDownloader<ArtistsAdapter.ViewHolder> getPictureDownloaderThread() {
        return pictureDownloaderThread;
    }

    public enum FragmentState {
        LOADED,
        LOADING,
        ERROR
    }

    private void switchState(FragmentState state) {
        findViewById(R.id.loaded).setVisibility((state.equals(LOADED) ? View.VISIBLE : View.GONE));
        findViewById(R.id.loading).setVisibility((state.equals(LOADING) ? View.VISIBLE : View.GONE));
        findViewById(R.id.error).setVisibility((state.equals(ERROR) ? View.VISIBLE : View.GONE));
    }
}
