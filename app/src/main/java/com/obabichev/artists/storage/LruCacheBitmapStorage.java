package com.obabichev.artists.storage;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by olegchuikin on 15/04/16.
 */
public class LruCacheBitmapStorage implements DataStorage<String, Bitmap> {

    private final int cacheSize;

    private LruCache<String, Bitmap> cache;

    public LruCacheBitmapStorage(int cacheSize){
        this.cacheSize = cacheSize;

        cache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    public LruCacheBitmapStorage() {
        this((int) (Runtime.getRuntime().maxMemory() / 8)); // MaxMemory/8 by default
    }

    @Override
    public void put(String key, Bitmap value) {
        if (key == null || value == null){
            return;
        }
        cache.put(key, value);
    }

    @Override
    public Bitmap get(String key) {
        if (key == null){
            return null;
        }
        return cache.get(key);
    }

    @Override
    public boolean contains(String key) {
        if (key == null){
            return false;
        }
        return cache.get(key) != null;
    }
}
