package com.example.rpkeffect.Utils;

import android.os.AsyncTask;
import android.util.Log;

import com.example.rpkeffect.Constructors.Product;
import com.example.rpkeffect.Interfaces.JsonTaskListener;

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
import java.util.List;

public class JsonTask extends AsyncTask<String, String, String> {

    JsonTaskListener listener;
    List<Product> products;

    int min = 0, max = 1000;

    public void setMin(int min){
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setListener(JsonTaskListener listener) {
        this.listener = listener;
    }

    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected String doInBackground(String... params) {


        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0]);
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

            products = new ArrayList<>();
            JSONArray array = new JSONArray(buffer.toString());
            listener.onSetMaxProgressBar(max);
            for (int outerArrayPos = min; outerArrayPos < max; outerArrayPos++) {
                JSONArray innerArray = array.getJSONArray(outerArrayPos);
                String name = "", code = "", article = "", status = "";

                for (int innerArrayPos = 0; innerArrayPos < innerArray.length(); innerArrayPos++) {
                    String valueAtIndex = innerArray.getString(innerArrayPos); //innerArray
                    switch (innerArrayPos) {
                        case 0:
                            status = valueAtIndex;
                            break;
                        case 1:
                            name = valueAtIndex;
                            break;
                        case 2:
                            code = valueAtIndex;
                            break;
                        case 3:
                            article = valueAtIndex;
                            break;
                    }
                    if (status.equals("")) status = "---";
                    if (status.equals("10")) name = "Нет товара";
                    if (name.equals("")) name = "---";
                    if (code.equals("")) code = "---";
                    if (article.equals("")) article = "---";
                    Product product = new Product(name, status, article, code);
                    products.add(product);
                    listener.onAddProduct(product);
                    listener.onUpdateProgressBarListener(outerArrayPos);
                }
            }
            listener.onSuccessListener(products);
            Log.d("myLog", "doInBackground: max = " + max);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}
