package com.obabichev.artists.network;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import com.obabichev.artists.fragments.ChangingDataObserver;
import com.obabichev.artists.model.Artist;

/**
 * Created by olegchuikin on 13/04/16.
 */
public class HttpDownloaderAsyncTask extends AsyncTask<Void, Void, List<Artist>> {

    private final String TAG = getClass().getSimpleName().toUpperCase();

    private final String url;

    private ChangingDataObserver<Artist> observer;

    public HttpDownloaderAsyncTask(String url, ChangingDataObserver<Artist> observer) {
        this.observer = observer;
        this.url = url;
    }

    @Override
    protected List<Artist> doInBackground(Void... params) {

        try {
            String stringJson = new HttpDownloader().getUrlString(url);
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