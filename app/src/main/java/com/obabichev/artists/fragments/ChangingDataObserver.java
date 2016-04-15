package com.obabichev.artists.fragments;

import java.util.List;

/**
 * Created by obabichev on 14/04/16.
 */
public interface ChangingDataObserver<T> {

    void handleChangingData(List<T> newData);

}
