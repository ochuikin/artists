package serversyncdemo.olegbabichev.com.artists.network;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.GpsStatus;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import serversyncdemo.olegbabichev.com.artists.BitmapsStorage;

/**
 * Created by obabichev on 14/04/16.
 */
public class PictureDownloader<Token> extends HandlerThread {

    private final static String TAG = "PictureDownloader";
    private static final int MESSAGE_DOWNLOAD = 0;

    private Handler handler;
    private Map<Token, String> requestMap = Collections.synchronizedMap(new HashMap<Token, String>());

    private Handler responseHandler;
    private Listener<Token> listener;

    public interface Listener<Token>{
        void onPictureDownloaded(Token token, Bitmap picture);
    }

    public void setListener(Listener<Token> listener) {
        this.listener = listener;
    }

    public PictureDownloader(Handler responseHandler) {
        super(TAG);
        this.responseHandler = responseHandler;
    }

    public void queuePicture(Token token, String url) {
        Log.i("Picture downloader", "PictureDownloader call queuePicture");

        requestMap.put(token, url);
        handler.obtainMessage(MESSAGE_DOWNLOAD, token)
                .sendToTarget();
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onLooperPrepared() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    @SuppressWarnings("unchecked")
                    Token token = (Token) msg.obj;
                    Log.i("Picture downloader", "PictureDownloader request for url: " + requestMap.get(token));
                    handleRequest(token);
                }
            }
        };
    }

    private void handleRequest(final Token token){

        try {
            final String url = requestMap.get(token);

            if (url == null)
                return;

            byte[] bitmapBytes = new HttpDownloader().getUrlBytes(url);
            final Bitmap bitMap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
            Log.i("Picture downloader", "PictureDownloader picture downloaded");

            //todo del later, imitation slow internet ^_^
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            BitmapsStorage.data.put(url, bitMap);
            responseHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (requestMap.get(token) == null || !requestMap.get(token).equals(url)){
                        return;
                    }

                    requestMap.remove(token);
                    listener.onPictureDownloaded(token, bitMap);
                }
            });

        } catch (IOException e) {
            Log.e("Picture downloader", "PictureDownloader Error downloading image", e);
        }
    }

    public void clearQueue() {
        handler.removeMessages(MESSAGE_DOWNLOAD);
        requestMap.clear();
    }
}
