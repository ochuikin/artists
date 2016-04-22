package com.obabichev.artists.components;

import com.obabichev.artists.adapters.ArtistsAdapter;
import com.obabichev.artists.fragments.ArtistDetailsFragment;
import com.obabichev.artists.network.HttpDownloaderAsyncTask;
import com.obabichev.artists.network.HttpDownloaderModule;
import com.obabichev.artists.network.PictureDownloader;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by obabichev on 22/04/16.
 */

@Component(modules = {HttpDownloaderModule.class})
@Singleton
public interface AppComponent {

    void inject(ArtistDetailsFragment artistDetailsFragment);

    void inject(HttpDownloaderAsyncTask httpDownloaderAsyncTask);

    void inject(PictureDownloader<ArtistsAdapter.ViewHolder> pictureDownloader);

}
