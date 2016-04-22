package com.obabichev.artists.robotium;

import com.obabichev.artists.R;
import com.obabichev.artists.robotium.core.BaseTest;

/**
 * Created by olegchuikin on 22/04/16.
 */
public class ArtistsListFragmentTest extends BaseTest{

    public void testOpenFragmentTest() {
        assertTrue(solo.waitForText(getString(R.string.artists_list_fragment_title)));

//        solo.sleep(100000);
    }

}
