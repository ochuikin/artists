package com.obabichev.artists.di.modules;

import android.app.Application;
import android.content.Context;

import com.obabichev.artists.network.HttpDownloader;
import com.obabichev.artists.network.HttpDownloaderImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by obabichev on 22/04/16.
 */

@Module
public class AppModule {

    Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    Context provideContext() {
        return application.getApplicationContext();
    }

}
