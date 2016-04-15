package serversyncdemo.olegbabichev.com.artists.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import serversyncdemo.olegbabichev.com.artists.R;
import serversyncdemo.olegbabichev.com.artists.model.Artist;
import serversyncdemo.olegbabichev.com.artists.network.PictureDownloader;
import serversyncdemo.olegbabichev.com.artists.storage.DataStorage;
import serversyncdemo.olegbabichev.com.artists.utils.StringUtils;

/**
 * Created by obabichev on 14/04/16.
 */
public class ArtistsAdapter extends BaseAdapter {

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
        } else {
            holder.coverSmall.setImageResource(R.drawable.cover_downloading_faild);
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
        } else {
            holder.coverSmall.setVisibility(View.GONE);
            holder.progressBar.setVisibility(View.VISIBLE);
            pictureDownloaderThread.queuePicture(holder, imageUrl);
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
}