package serversyncdemo.olegbabichev.com.artists.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import serversyncdemo.olegbabichev.com.artists.R;
import serversyncdemo.olegbabichev.com.artists.model.Artist;
import serversyncdemo.olegbabichev.com.artists.network.PictureDownloader;
import serversyncdemo.olegbabichev.com.artists.utils.StringUtils;

/**
 * Created by obabichev on 14/04/16.
 */
public class ArtistDetailsFragment extends BaseFragment {

    private ArtistsListFragment parent;
    private Context context;
    private Artist artist;

    private ImageView coverBig;
    private TextView genres;
    private TextView songNumber;
    private TextView description;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.artist_details_fragment, container, false);

        coverBig = (ImageView) result.findViewById(R.id.artist_cover_big);
        genres = (TextView) result.findViewById(R.id.artist_genres);
        songNumber = (TextView) result.findViewById(R.id.track_number);
        description = (TextView) result.findViewById(R.id.artist_description);

        return result;
    }

    @Override
    public void onResume() {
        super.onResume();

        genres.setText(StringUtils.join(", ", artist.getGenres()));
        songNumber.setText(String.format("%d %s  \u00B7  %d %s",
                artist.getAlbums(), context.getString(R.string.abc_albums),
                artist.getTracks(), context.getString(R.string.abc_tracks)));
        description.setText(artist.getDescription());
        parent.getPictureDownloaderThread().queuePicture(coverBig, artist.getCover().getBig());
    }

    @Override
    protected String getTitle() {
        return artist.getName();
    }

    public static ArtistDetailsFragment create(ArtistsListFragment parent, Artist artist) {
        ArtistDetailsFragment fragment = new ArtistDetailsFragment();
        fragment.artist = artist;
        fragment.context = parent.getActivity();
        fragment.parent = parent;
        return fragment;
    }


}
