package com.example.rpkeffect.Utils;

import android.os.AsyncTask;
import android.text.PrecomputedText;
import android.util.Log;

import com.example.rpkeffect.Constructors.Product;
import com.example.rpkeffect.Interfaces.JsonInfoListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.transform.Result;

public class JsonInfo extends AsyncTask<String, String, String> {
    private static final String TAG = "myLog";

    JsonInfoListener listener;

    public void setListener(JsonInfoListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        listener.onStartListener();
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        URL url = null;
        try {
            url = new URL(params[0]);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
            connection.setRequestProperty("Accept", "*/*");
            connection.connect();

            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            JSONArray array = new JSONArray(buffer.toString());

            int errors = countMatches(buffer.toString(), "\"9\"");
            int success = countMatches(buffer.toString(), "\"10\"");
            listener.onFinishListener(success, errors, (int)(errors+success));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int countMatches(String str, String sub) {
        int count = 0;
        if(!str.isEmpty() && !sub.isEmpty()) {
            for (int i = 0; (i = str.indexOf(sub, i)) != -1; i += sub.length()) {
                count++;
            }
        }
        return count;
    }
}
