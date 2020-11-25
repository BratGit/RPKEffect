package com.example.rpkeffect.Utils;

import android.os.AsyncTask;

import com.example.rpkeffect.Constructors.Product;
import com.example.rpkeffect.Interfaces.AsyncOutputListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class AsyncOutput extends AsyncTask<String, String, String> {
    AsyncOutputListener listener;
    List<Product> products = new ArrayList<>();

    public void setListener(AsyncOutputListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        String resultValue = "";
        try {
            JSONArray array = new JSONArray(strings[0]);

            for (int outerArrayPos = 0; outerArrayPos < array.length(); outerArrayPos++) {
                JSONArray innerArray = array.getJSONArray(outerArrayPos);
                String name = "", code = "", article = "", status = "";

                for (int innerArrayPos = 0; innerArrayPos < innerArray.length(); innerArrayPos++) {
                    String valueAtIndex = innerArray.getString(innerArrayPos); //innerArray
                    resultValue += valueAtIndex;
//                    Log.d(TAG, "executing: " + innerArrayPos + " " + valueAtIndex);/
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
                    Product product = new Product(name, status, article, code);
//                    Log.d("myLog", "doInBackground: " + name + " " + status + " " +
//                            article + " " + code);
                    products.add(product);
                    listener.onUpdateListener(outerArrayPos, array.length());
//                    if (adapter == null) {
//                        adapter = new ProductAdapter(getLayoutInflater(), products);
//                        listView.setAdapter(adapter);
//                    } else {
//                        adapter.notifyDataSetChanged();
//                    }
                    // Do whatever you want with the values here
                }
            }
            listener.onPostExecute(products);
//                textView.setText(resultValue);
        } catch (JSONException e) {
            e.printStackTrace();
//            Log.d(TAG, "onPostExecute: " + e.getMessage());
//                listView.setText(e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
