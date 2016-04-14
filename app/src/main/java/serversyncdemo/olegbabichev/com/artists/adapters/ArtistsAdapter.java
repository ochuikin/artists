package serversyncdemo.olegbabichev.com.artists.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import serversyncdemo.olegbabichev.com.artists.R;
import serversyncdemo.olegbabichev.com.artists.model.Artist;

/**
 * Created by obabichev on 14/04/16.
 */
public class ArtistsAdapter extends BaseAdapter {

    private List<Artist> items;
    private Context context;

    public ArtistsAdapter(Context context, List<Artist> items) {
        this.context = context;
        this.items = items;
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
            holder.name = (TextView) view.findViewById(R.id.artist_name);
            holder.genres = (TextView) view.findViewById(R.id.artist_genres);
            holder.songNumber = ((TextView) view.findViewById(R.id.artist_song_number));
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Artist artist = items.get(position);
        holder.name.setText(artist.getName());
        holder.genres.setText(genresFormat(artist.getGenres()));
        holder.songNumber.setText(tracksNumberFormat(artist.getAlbums(), artist.getTracks()));

        return view;
    }

    private String genresFormat(List<String> genres) {
        StringBuilder sb = new StringBuilder();
        for (String genre : genres) {
            sb.append(genre + ", ");
        }
        return sb.substring(0, sb.length() - 2);
    }

    private String tracksNumberFormat(int albumsNumber, int tracksNumber) {
        return String.format("%d %s, %d %s",
                albumsNumber, context.getString(R.string.abc_albums), tracksNumber, context.getString(R.string.abc_tracks));
    }

    private static class ViewHolder{
        public TextView name;
        public TextView genres;
        public TextView songNumber;
    }
}
