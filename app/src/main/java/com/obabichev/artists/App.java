package com.obabichev.artists;

import android.app.Application;

import com.obabichev.artists.di.components.AppComponent;
import com.obabichev.artists.di.components.DaggerAppComponent;
import com.obabichev.artists.di.modules.AppModule;
import com.obabichev.artists.network.HttpDownloaderModule;

/**
 * Created by obabichev on 22/04/16.
 */
public class App extends Application {

    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerAppComponent
                .builder()
                .httpDownloaderModule(new HttpDownloaderModule())
                .appModule(new AppModule(this))
                .build();
    }

    public static AppComponent getComponent() {
        return component;
    }
}
