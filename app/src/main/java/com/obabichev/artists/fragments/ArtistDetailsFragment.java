package com.obabichev.artists.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;

import com.obabichev.artists.App;
import com.obabichev.artists.R;
import com.obabichev.artists.model.Artist;
import com.obabichev.artists.network.HttpDownloader;
import com.obabichev.artists.utils.StringUtils;

import javax.inject.Inject;

/**
 * Created by obabichev on 14/04/16.
 */
public class ArtistDetailsFragment extends BaseFragment {

    private Artist artist;
    private Bitmap cover = null;

    private ImageView coverBig;
    private ProgressBar progressBar;
    private TextView genres;
    private TextView songNumber;
    private TextView description;

    @Inject
    HttpDownloader httpDownloader;

    @Inject
    Context context;

    /**
     * In the detail screen I will download pictures using AsyncTask and will not save it
     */
    private final AsyncTask<Void, Void, Bitmap> downloadCover = new AsyncTask<Void, Void, Bitmap>() {
        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                byte[] bitmapBytes = httpDownloader.getUrlBytes(artist.getCover().getBig());
                final Bitmap bitMap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
                Log.i(TAG, "Image downloaded by url: " + artist.getCover().getBig());
                return bitMap;
            } catch (IOException e) {
                Log.e(TAG, "Downloading image failed", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            cover = bitmap;
            if (bitmap != null) {
                coverBig.setImageBitmap(bitmap);
            } else {
                coverBig.setImageResource(R.drawable.cover_downloading_faild);
            }
            switchLoaded(true);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        App.getComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View result = inflater.inflate(R.layout.artist_details_fragment, container, false);

        coverBig = (ImageView) result.findViewById(R.id.artist_cover_big);
        progressBar = (ProgressBar) result.findViewById(R.id.progress_bar);
        genres = (TextView) result.findViewById(R.id.artist_genres);
        songNumber = (TextView) result.findViewById(R.id.track_number);
        description = (TextView) result.findViewById(R.id.artist_description);

        if (!downloadCover.getStatus().equals(AsyncTask.Status.FINISHED)) {
            switchLoaded(false);
            downloadCover.execute();
        } else {
            switchLoaded(true);
            if (cover != null) {
                coverBig.setImageBitmap(cover);
            } else {
                coverBig.setImageResource(R.drawable.cover_downloading_faild);
            }
        }

        return result;
    }

    @Override
    public void onResume() {
        super.onResume();
        showMenuBackButton(true);

        genres.setText(StringUtils.join(", ", artist.getGenres()));
        songNumber.setText(String.format("%d %s  \u00B7  %d %s",
                artist.getAlbums(), context.getString(R.string.abc_albums),
                artist.getTracks(), context.getString(R.string.abc_tracks)));
        description.setText(artist.getDescription());

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        progressBar.setLayoutParams(new LinearLayout.LayoutParams(width, width));

    }

    @Override
    protected String getTitle() {
        return artist.getName();
    }

    public static ArtistDetailsFragment create(Artist artist) {
        ArtistDetailsFragment fragment = new ArtistDetailsFragment();
        fragment.artist = artist;
        return fragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void switchLoaded(boolean isLoaded) {
        coverBig.setVisibility(isLoaded ? View.VISIBLE : View.GONE);
        progressBar.setVisibility(isLoaded ? View.GONE : View.VISIBLE);
    }
}
