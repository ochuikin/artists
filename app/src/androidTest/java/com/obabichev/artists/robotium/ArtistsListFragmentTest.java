package com.obabichev.artists.robotium;

import android.widget.ListView;

import com.obabichev.artists.R;
import com.obabichev.artists.adapters.ArtistsAdapter;
import com.obabichev.artists.model.Artist;
import com.obabichev.artists.robotium.core.BaseTest;
import com.robotium.solo.Solo;

/**
 * Created by obabichev on 22/04/16.
 */
public class ArtistsListFragmentTest extends BaseTest {

    private final int JSON_ARTISTS_COUNT = 317;
    private final String FIRST_ARTIST_NAME = "Tove Lo";
    private final String LAST_ARTIST_NAME = "Jason Derulo";

    public void testOpenFragmentTest() {
        waitText(R.string.artists_list_fragment_title);
    }

    public void testDownloadingJson() {
        waitText(FIRST_ARTIST_NAME);

        ListView listView = solo.getView(ListView.class, 0);
        assertEquals(listView.getAdapter().getCount(), JSON_ARTISTS_COUNT);

        solo.scrollListToBottom(0);
        waitText(LAST_ARTIST_NAME);
    }

    public void testContentOfListItem() {
        waitText(R.string.artists_list_fragment_title);
        waitText(FIRST_ARTIST_NAME);

        final ArtistsAdapter adapter = (ArtistsAdapter) solo.getView(ListView.class, 0).getAdapter();
        final Artist artist = (Artist) adapter.getItem(0);

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                adapter.getItems().clear();
                adapter.getItems().add(artist);
                adapter.notifyDataSetChanged();
            }
        });
        getInstrumentation().waitForIdleSync();

        checkContentOnDetailScreen(artist);
    }

    public void testRotateScreen(){
        solo.setActivityOrientation(Solo.LANDSCAPE);

        solo.scrollListToTop(0);
        waitText(FIRST_ARTIST_NAME);
        solo.scrollListToBottom(0);
        waitText(LAST_ARTIST_NAME);

    }

    private void checkContentOnDetailScreen(Artist artist) {
        waitText(String.valueOf(artist.getAlbums()));
        waitText(String.valueOf(artist.getTracks()));
        waitText(artist.getName());
        if (artist.getGenres() != null && artist.getGenres().size() > 0) {
            for (String genre : artist.getGenres()) {
                waitText(genre);
            }
        }
    }

}
