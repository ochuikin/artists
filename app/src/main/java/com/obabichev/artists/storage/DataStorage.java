package com.obabichev.artists.storage;

/**
 * Created by olegchuikin on 15/04/16.
 */
public interface DataStorage<K, V> {

    void put(K key, V value);

    V get(K key);

    boolean contains(K key);

}
