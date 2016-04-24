package com.obabichev.artists.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import com.obabichev.artists.App;
import com.obabichev.artists.MainActivity;
import com.obabichev.artists.R;
import com.obabichev.artists.adapters.ArtistsAdapter;
import com.obabichev.artists.model.Artist;
import com.obabichev.artists.network.HttpJsonArtistsDownloaderAsyncTask;
import com.obabichev.artists.network.PictureDownloader;
import com.obabichev.artists.storage.LruCacheBitmapStorage;

import javax.inject.Inject;

import static com.obabichev.artists.fragments.ArtistsListFragment.FragmentState.*;
import static com.obabichev.artists.fragments.ArtistsListFragment.FragmentState.LOADED;

/**
 * Created by obabichev 13/04/16.
 */
public class ArtistsListFragment extends BaseFragment implements ChangingDataObserver<Artist> {

    private ListView artistsListView;

    private List<Artist> artists = null;

    @Inject
    Context context;

    private PictureDownloader<ArtistsAdapter.ViewHolder> pictureDownloaderThread;

    private final AdapterView.OnItemClickListener onListViewItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            applyFragment(ArtistDetailsFragment.create((Artist) artistsListView.getAdapter().getItem(position)));
        }
    };

    private final View.OnClickListener reloadDataOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switchState(LOADING);
            new HttpJsonArtistsDownloaderAsyncTask(MainActivity.JSON_ARTISTS_URL, ArtistsListFragment.this).execute();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.getComponent().inject(this);

        setRetainInstance(true);

        if (artists == null) {
            Log.i(TAG, "Request to download json");
            new HttpJsonArtistsDownloaderAsyncTask(MainActivity.JSON_ARTISTS_URL, this).execute();
        }

        //init and start picture downloader, creating LruCache with 1/8 of available ram
        pictureDownloaderThread = new PictureDownloader<>(new Handler());
        App.getComponent().inject(pictureDownloaderThread);
        pictureDownloaderThread.start();
        pictureDownloaderThread.setDataStorage(new LruCacheBitmapStorage());
        pictureDownloaderThread.getLooper();

        Log.i(TAG, "pictureDownloaderThread started");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.artists_list_fragment, container, false);

        artistsListView = (ListView) result.findViewById(R.id.artists_list_view);
        result.findViewById(R.id.reloadDataButton).setOnClickListener(reloadDataOnClickListener);

        return result;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupAdapter();
    }

    private void setupAdapter() {
        if (artistsListView == null) return;
        if (artists != null) {
            switchState(LOADED);
            artistsListView.setAdapter(new ArtistsAdapter(artists, pictureDownloaderThread));
            artistsListView.setOnItemClickListener(onListViewItemClickListener);
            Log.i(TAG, "Setup adapter for ArtistsListFragment");
        } else {
            artistsListView.setAdapter(null);
            Log.i(TAG, "Setup adapter: Artists data is null");
        }
    }

    @Override
    protected String getTitle() {
        return context.getString(R.string.artists_list_fragment_title);
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

        //stop picture downloader
        pictureDownloaderThread.quit();
        Log.i(TAG, "pictureDownloaderThread stopped");

        pictureDownloaderThread.clearQueue();
    }

    public enum FragmentState {
        LOADED,
        LOADING,
        ERROR
    }

    private void switchState(FragmentState state) {
        Log.i(TAG, "ArtistsListFragment switched state to: " + state);
        findViewById(R.id.loaded).setVisibility((state.equals(LOADED) ? View.VISIBLE : View.GONE));
        findViewById(R.id.loading).setVisibility((state.equals(LOADING) ? View.VISIBLE : View.GONE));
        findViewById(R.id.error).setVisibility((state.equals(ERROR) ? View.VISIBLE : View.GONE));
    }
}
