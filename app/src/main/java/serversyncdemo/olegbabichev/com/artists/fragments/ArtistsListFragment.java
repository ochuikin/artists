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
import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;

import serversyncdemo.olegbabichev.com.artists.BitmapsStorage;
import serversyncdemo.olegbabichev.com.artists.MainActivity;
import serversyncdemo.olegbabichev.com.artists.R;
import serversyncdemo.olegbabichev.com.artists.adapters.ArtistsAdapter;
import serversyncdemo.olegbabichev.com.artists.model.Artist;
import serversyncdemo.olegbabichev.com.artists.network.HttpDownloaderAsyncTask;
import serversyncdemo.olegbabichev.com.artists.network.PictureDownloader;

/**
 * Created by obabichev 13/04/16.
 */
public class ArtistsListFragment extends BaseFragment implements ChangingDataObserver<Artist> {

    private ListView artistsListView;

    private List<Artist> artists;

    private PictureDownloader<ImageView> pictureDownloaderThread;

    private final AdapterView.OnItemClickListener onListViewItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            applyFragment(ArtistDetailsFragment.create(ArtistsListFragment.this, (Artist) artistsListView.getAdapter().getItem(position)));
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
        pictureDownloaderThread.setListener(new PictureDownloader.Listener<ImageView>() {
            @Override
            public void onPictureDownloaded(ImageView imageView, Bitmap picture) {
                if (imageView != null && picture != null) {
                    imageView.setImageBitmap(picture);
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
            artistsListView.setAdapter(new ArtistsAdapter(this, artists));
            artistsListView.setOnItemClickListener(onListViewItemClickListener);
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
        this.artists = newData;
        setupAdapter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        pictureDownloaderThread.quit();
        Log.i("Picture downloader", "PictureDownloader stopped");

        pictureDownloaderThread.clearQueue();
    }

    public PictureDownloader<ImageView> getPictureDownloaderThread() {
        return pictureDownloaderThread;
    }
}
