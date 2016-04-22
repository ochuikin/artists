package com.obabichev.artists.robotium.core;

import android.test.ActivityInstrumentationTestCase2;

import com.obabichev.artists.MainActivity;
import com.robotium.solo.Solo;

/**
 * Created by olegchuikin on 22/04/16.
 */
public class BaseTest extends ActivityInstrumentationTestCase2<MainActivity> {
    public BaseTest() {
        super(MainActivity.class);
    }

    protected Solo solo;

    @Override
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    protected String getString(int id) {
        return getActivity().getString(id);
    }
}
