package serversyncdemo.olegbabichev.com.artists;

import android.graphics.Bitmap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by olegchuikin on 14/04/16.
 */
public class BitmapsStorage {

    public final static Map<String, Bitmap> data = Collections.synchronizedMap(new HashMap<String, Bitmap>());

}
