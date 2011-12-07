package com.example.umainehabapp;
 
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import org.apache.http.util.ByteArrayBuffer;
import android.util.Log;
 
public class PredictedDataFetcher extends umainehabappActivity {
        //private final String PATH = "/data/data/com.helloandroid.imagedownloader/";  //put the downloaded file here
       
        public void DownloadFromUrl(String KMLURL, String fileName) {  //this is the downloader method
                try {
                        URL url = new URL(KMLURL); //you can write here any link
                        File file = new File(fileName);
 
                        long startTime = System.currentTimeMillis();
                        Log.d("KMLDownloader", "download begining");
                        Log.d("KMLDownloader", "download url:" + url);
                        Log.d("KMLDownloader", "downloaded file name:" + fileName);
                        /* Open a connection to that URL. */
                        URLConnection ucon = url.openConnection();
 
                        /*
                         * Define InputStreams to read from the URLConnection.
                         */
                        InputStream is = ucon.getInputStream();
                        BufferedInputStream bis = new BufferedInputStream(is);
 
                        /*
                         * Read bytes to the Buffer until there is nothing more to read(-1).
                         */
                        ByteArrayBuffer baf = new ByteArrayBuffer(50);
                        int current = 0;
                        while ((current = bis.read()) != -1) {
                                baf.append((byte) current);
                        }
 
                        /* Convert the Bytes read to a String. */
                        FileOutputStream fos = new FileOutputStream(file);
                        fos.write(baf.toByteArray());
                        fos.close();
                        Log.d("KMLDownloader", "download ready in"
                                        + ((System.currentTimeMillis() - startTime) / 1000)
                                        + " sec");
 
                } catch (IOException e) {
                        Log.d("KMLDownloader", "Error: " + e);
                }
 
        }
}
