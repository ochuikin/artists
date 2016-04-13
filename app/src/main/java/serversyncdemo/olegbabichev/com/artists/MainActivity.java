package serversyncdemo.olegbabichev.com.artists;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import serversyncdemo.olegbabichev.com.artists.fragments.ArtistsListFragment;
import serversyncdemo.olegbabichev.com.artists.fragments.BaseFragment;

public class MainActivity extends AppCompatActivity {

    public static final String JSON_ARTISTS_URL = "http://download.cdn.yandex.net/mobilization-2016/artists.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        applyFragment(new ArtistsListFragment());
    }

    public void applyFragment(BaseFragment baseFragment) {
        if (findViewById(R.id.main_activity_fragment) == null) {
            return;
        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.main_activity_fragment, baseFragment)
                .addToBackStack(null)
                .commit();
    }

    public void finishFragment() {
        getSupportFragmentManager().popBackStack();
    }
}
