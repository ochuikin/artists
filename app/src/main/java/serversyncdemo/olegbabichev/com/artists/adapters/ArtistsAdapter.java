package serversyncdemo.olegbabichev.com.artists.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import serversyncdemo.olegbabichev.com.artists.R;
import serversyncdemo.olegbabichev.com.artists.model.Artist;

/**
 * Created by olegchuikin on 14/04/16.
 */
public class ArtistsAdapter extends BaseAdapter {

    private List<Artist> items;
    private LayoutInflater inflater;

    public ArtistsAdapter(Context context, List<Artist> items) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        View view = (convertView != null)? convertView:
                inflater.inflate(R.layout.artist_list_item_layout, parent, false);

        TextView name = (TextView) view.findViewById(R.id.artist_name);
        name.setText(items.get(position).getName());
        return view;
    }
}
