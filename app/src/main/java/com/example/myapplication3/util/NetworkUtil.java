package com.example.myapplication3.util;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import android.util.Log; // Import Log

public class NetworkUtil {

    private static final OkHttpClient client = new OkHttpClient();
    private static final String TAG = "NetworkUtil"; // Tag ekle

    public static String fetchYemeklerHtml(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            ResponseBody body = response.body();
            if (body != null) {
                String html = body.string();
                Log.d(TAG, "HTML İçeriği: " + html); // HTML'yi logla
                return html;
            } else {
                return "";
            }
        }
    }
}
