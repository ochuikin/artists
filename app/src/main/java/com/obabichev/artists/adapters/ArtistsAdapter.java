package com.obabichev.artists.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import com.obabichev.artists.R;
import com.obabichev.artists.model.Artist;
import com.obabichev.artists.network.PictureDownloader;
import com.obabichev.artists.storage.DataStorage;
import com.obabichev.artists.utils.StringUtils;

/**
 * Created by obabichev on 14/04/16.
 */
public class ArtistsAdapter extends BaseAdapter {

    private final String TAG = getClass().getSimpleName().toUpperCase();

    private List<Artist> items;
    private Context context;

    private PictureDownloader<ViewHolder> pictureDownloaderThread;
    private DataStorage<String, Bitmap> dataStorage = null;

    public ArtistsAdapter(Context context, List<Artist> items, PictureDownloader<ViewHolder> pictureDownloaderThread) {
        this.context = context;
        this.items = items;
        this.pictureDownloaderThread = pictureDownloaderThread;
        this.dataStorage = pictureDownloaderThread.getDataStorage();
        pictureDownloaderThread.setListener(new PictureDownloader.Listener<ArtistsAdapter.ViewHolder>() {
            @Override
            public void onPictureDownloaded(ArtistsAdapter.ViewHolder holder, Bitmap picture) {
                setImageToHolder(holder, picture);
            }
        });
    }

    private void setImageToHolder(ViewHolder holder, Bitmap picture) {
        holder.coverSmall.setVisibility(View.VISIBLE);
        holder.progressBar.setVisibility(View.GONE);
        if (picture != null) {
            holder.coverSmall.setImageBitmap(picture);
            Log.i(TAG, "Image for holder.coverSmall was changed");
        } else {
            holder.coverSmall.setImageResource(R.drawable.cover_downloading_faild);
            Log.e(TAG, "Image for holder.coverSmall was changed by FailImage");
        }
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.artist_list_item_layout, null, true);
            holder = new ViewHolder();
            holder.coverSmall = (ImageView) view.findViewById(R.id.artist_cover_small);
            holder.progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
            holder.name = (TextView) view.findViewById(R.id.artist_name);
            holder.genres = (TextView) view.findViewById(R.id.artist_genres);
            holder.songNumber = ((TextView) view.findViewById(R.id.artist_song_number));
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        Artist artist = items.get(position);
        holder.name.setText(artist.getName());
        holder.genres.setText(StringUtils.join(", ", artist.getGenres()));
        holder.songNumber.setText(tracksNumberFormat(artist.getAlbums(), artist.getTracks()));

        String imageUrl = artist.getCover().getSmall();
        if (dataStorage != null && dataStorage.contains(imageUrl)) {
            setImageToHolder(holder, dataStorage.get(imageUrl));
            Log.i(TAG, "Image founded in dataStorage");
        } else {
            holder.coverSmall.setVisibility(View.GONE);
            holder.progressBar.setVisibility(View.VISIBLE);
            pictureDownloaderThread.queuePicture(holder, imageUrl);
            Log.i(TAG, "Image not founded in storage and made request to download it");
        }

        return view;
    }


    private String tracksNumberFormat(int albumsNumber, int tracksNumber) {
        return String.format("%d %s, %d %s",
                albumsNumber, context.getString(R.string.abc_albums), tracksNumber, context.getString(R.string.abc_tracks));
    }

    public static class ViewHolder {
        public ImageView coverSmall;
        public ProgressBar progressBar;
        public TextView name;
        public TextView genres;
        public TextView songNumber;
    }

    public void setItems(List<Artist> items) {
        this.items = items;
    }

    public List<Artist> getItems() {
        return items;
    }
}