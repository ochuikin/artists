package com.obabichev.artists.robotium;

import android.widget.ListView;

import com.obabichev.artists.R;
import com.obabichev.artists.model.Artist;
import com.obabichev.artists.robotium.core.BaseTest;


/**
 * Created by obabichev on 22/04/16.
 */
public class ArtistDetailFragmentTest extends BaseTest {

    public void testOpenDetailScreen() {
        reachDetailListFragment(0);
    }

    public void testBackButton() {
        reachDetailListFragment(0);

        solo.goBack();
        waitText(R.string.artists_list_fragment_title);
    }

    public void testDetailFragmentContent() {
        for (Integer position : new int[]{0, 1, 2}){
            Artist artist = reachDetailListFragment(position);
            checkContentOnDetailScreen(artist);
            solo.goBack();
        }

    }

    private void checkContentOnDetailScreen(Artist artist){
        waitText(String.valueOf(artist.getAlbums()));
        waitText(String.valueOf(artist.getTracks()));
        waitText(artist.getName());
        waitText(artist.getDescription());
        if (artist.getGenres() != null && artist.getGenres().size() > 0){
            for (String genre : artist.getGenres()) {
                waitText(genre);
            }
        }
    }

    private Artist reachDetailListFragment(int position) {
        assertTrue(solo.waitForView(ListView.class, 0, WAIT_TIMEOUT));
        Artist item = (Artist) ((ListView) solo
                .getView(ListView.class, 0))
                .getAdapter()
                .getItem(position);
//        solo.clickInList(position, 0);
        solo.clickOnText(item.getName());
        waitText(R.string.abc_biography);
        return item;
    }
}

