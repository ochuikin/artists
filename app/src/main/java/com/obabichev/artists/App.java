package com.obabichev.artists;

import android.app.Application;

import com.obabichev.artists.components.AppComponent;
import com.obabichev.artists.components.DaggerAppComponent;
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
                .build();
    }

    public static AppComponent getComponent() {
        return component;
    }
}
