package com.obabichev.artists.di.components;

import com.obabichev.artists.adapters.ArtistsAdapter;
import com.obabichev.artists.di.modules.AppModule;
import com.obabichev.artists.di.modules.HttpDownloaderModule;
import com.obabichev.artists.fragments.ArtistDetailsFragment;
import com.obabichev.artists.fragments.ArtistsListFragment;
import com.obabichev.artists.network.HttpJsonArtistsDownloaderAsyncTask;
import com.obabichev.artists.network.PictureDownloader;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by obabichev on 22/04/16.
 */

@Component(modules = {HttpDownloaderModule.class, AppModule.class})
@Singleton
public interface AppComponent {

    void inject(ArtistDetailsFragment artistDetailsFragment);

    void inject(HttpJsonArtistsDownloaderAsyncTask httpDownloaderAsyncTask);

    void inject(PictureDownloader<ArtistsAdapter.ViewHolder> pictureDownloader);

    void inject(ArtistsAdapter artistsAdapter);

    void inject(ArtistsListFragment artistsListFragment);
}
