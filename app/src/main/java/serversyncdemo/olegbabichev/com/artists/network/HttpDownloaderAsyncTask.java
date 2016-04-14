package serversyncdemo.olegbabichev.com.artists.network;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import serversyncdemo.olegbabichev.com.artists.fragments.ChangingDataObserver;
import serversyncdemo.olegbabichev.com.artists.model.Artist;

/**
 * Created by olegchuikin on 13/04/16.
 */
public class HttpDownloaderAsyncTask extends AsyncTask<Void, Void, List<Artist>> {

    private final String url;
    private List<Artist> artists;

    private ChangingDataObserver<Artist> observer;

    public HttpDownloaderAsyncTask(String url, ChangingDataObserver<Artist> observer) {
        this.observer = observer;
        this.url = url;
    }

    @Override
    protected List<Artist> doInBackground(Void... params) {

        try {
            String stringJson = new HttpDownloader().getUrlString(url);
            Log.i("network", "Downloaded JSON: " + stringJson);

            artists = new Gson().fromJson(stringJson, new TypeToken<List<Artist>>() {
            }.getType());
            Log.i("", "parsed");
            return artists;

        } catch (IOException e) {
            Log.e("", "Error in downloading data", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Artist> result) {
        super.onPostExecute(result);

        observer.handleChangingData(result);
    }
}
