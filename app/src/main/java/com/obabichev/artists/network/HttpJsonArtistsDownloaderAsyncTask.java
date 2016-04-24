package com.obabichev.artists.network;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import com.obabichev.artists.App;
import com.obabichev.artists.fragments.ChangingDataObserver;
import com.obabichev.artists.model.Artist;

import javax.inject.Inject;

/**
 * It is for downloading json with artists.
 * <p/>
 * For handling downloaded artists ChangingDataObserver is used.
 * <p/>
 * Created by obabichev on 13/04/16.
 */
public class HttpJsonArtistsDownloaderAsyncTask extends AsyncTask<Void, Void, List<Artist>> {

    private final String TAG = getClass().getSimpleName().toUpperCase();

    private final String url;

    private ChangingDataObserver<Artist> observer;

    @Inject
    HttpDownloader httpDownloader;

    public HttpJsonArtistsDownloaderAsyncTask(String url, ChangingDataObserver<Artist> observer) {
        this.observer = observer;
        this.url = url;

        App.getComponent().inject(this);
    }

    @Override
    protected List<Artist> doInBackground(Void... params) {

        try {
            String stringJson = httpDownloader.getUrlString(url);
            Log.i(TAG, "Downloaded JSON: " + stringJson);

            List<Artist> artists = new Gson().fromJson(stringJson, new TypeToken<List<Artist>>() {
            }.getType());
            if (artists != null) {
                Log.i(TAG, "JSON was successfully parsed");
            } else {
                Log.e(TAG, "JSON parsing failed");
            }
            return artists;

        } catch (IOException e) {
            Log.e(TAG, "Error in downloading data", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Artist> result) {
        super.onPostExecute(result);

        observer.handleChangingData(result);
    }
}
