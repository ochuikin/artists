package com.obabichev.artists.di.modules;

import com.obabichev.artists.network.HttpDownloader;
import com.obabichev.artists.network.HttpDownloaderImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by obabichev on 22/04/16.
 */
@Module
public class HttpDownloaderModule {

    @Provides
    @Singleton
    HttpDownloader provideHttpDownloader() {
        return new HttpDownloaderImpl();
    }

}
