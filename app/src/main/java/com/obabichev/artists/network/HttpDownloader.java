package com.obabichev.artists.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by obabichev on 13/04/16.
 */
public interface HttpDownloader {

    byte[] getUrlBytes(String urlString) throws IOException;

    String getUrlString(String urlString) throws IOException;
}
