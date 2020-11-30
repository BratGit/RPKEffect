package com.example.rpkeffect.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.rpkeffect.Adapters.ProductAdapter;
import com.example.rpkeffect.Adapters.RequestAdapter;
import com.example.rpkeffect.Constructors.Product;
import com.example.rpkeffect.Constructors.Request;
import com.example.rpkeffect.Interfaces.AsyncOutputListener;
import com.example.rpkeffect.Interfaces.JsonInfoListener;
import com.example.rpkeffect.Interfaces.JsonTaskListener;
import com.example.rpkeffect.R;
import com.example.rpkeffect.Utils.AsyncOutput;
import com.example.rpkeffect.Utils.JsonInfo;
import com.example.rpkeffect.Utils.JsonTask;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FirstProviderFragment extends Fragment implements JsonTaskListener, JsonInfoListener {
    private static final String TAG = "myLog";

    public ProgressDialog mProgressDialog;
    public static ListView listView;
    public List<Product> mProducts;
    public ProductAdapter adapter;
    FloatingActionButton getJson;
    FloatingActionButton getInfo;
    ProgressBar progressBar;
    Handler h;
    int value;
    int mMax;
    Product mProduct;
    Dialog dialog;

    int jsonMin = 0, jsonMax = 100;

    final int STATUS_SET_VISIBILITY = 0;
    final int STATUS_SET_PROGRESS = 1;
    final int STATUS_GET_PRODUCT = 2;
    final int STATUS_START_GETTING_INFO = 3;
    final int STATUS_FINISH_GETTING_INFO = 4;

    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_first, container, false);
        listView = root.findViewById(R.id.fst_listview);
        getJson = root.findViewById(R.id.get_json_btn);
        getInfo = root.findViewById(R.id.get_info_btn);
        progressBar = root.findViewById(R.id.progress_bar);
        mProducts = new ArrayList<>();
        adapter = new ProductAdapter(getLayoutInflater(), mProducts);
        listView.setAdapter(adapter);

        getJson.attachToListView(listView);

        dialog = new Dialog(getActivity());
        dialog
                .getWindow()
                .requestFeature(Window.FEATURE_NO_TITLE);

        dialog
                .getWindow()
                .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        dialog
                .getWindow()
                .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setCancelable(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDialog(position, getLayoutInflater());
            }
        });

        h = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case STATUS_SET_VISIBILITY:
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setMax(mMax);
                        break;
                    case STATUS_SET_PROGRESS:
                        progressBar.setProgress(value);
                        break;
                    case STATUS_GET_PRODUCT:
                        mProducts.add(mProduct);
                        adapter.notifyDataSetChanged();
                        break;
                    case STATUS_START_GETTING_INFO:
                        showProgressDialog();
                        break;
                    case STATUS_FINISH_GETTING_INFO:
                        hideProgressDialog();
                        break;
                }
            }
        };

        getJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValues();
            }
        });

        getInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonInfo info = new JsonInfo();
                info.setListener(FirstProviderFragment.this);
                info.execute("https://effect-gifts.ru/api/?action=getHappyLogs");
            }
        });

        getValues();

        return root;
    }

    private void showDialog(final int position, final LayoutInflater inflater) {
        dialog.setContentView(R.layout.dialog_product);

        TextView code, article, name, status;

        code = (TextView) dialog.findViewById(R.id.product_code);
        article = (TextView) dialog.findViewById(R.id.product_article);
        name = (TextView) dialog.findViewById(R.id.product_name);
        status = (TextView) dialog.findViewById(R.id.product_status);

        if (mProducts.get(position).getName().equals("Нет товара")) name.setTextColor(Color.RED);
        else name.setTextColor(Color.GREEN);

        if (mProducts.get(position).getStatus().equals("10")) status.setTextColor(Color.RED);
        else status.setTextColor(Color.WHITE);

        name.setText(mProducts.get(position).getName());
        code.setText(mProducts.get(position).getCode());
        article.setText(mProducts.get(position).getArticle());
        status.setText(mProducts.get(position).getStatus());

        dialog.show();
    }

    private void getValues(){
        JsonTask jsonTask = new JsonTask();
        jsonTask.setListener(FirstProviderFragment.this);
        jsonTask.setMin(jsonMin);
        jsonTask.setMax(jsonMax);
        jsonTask.execute("https://effect-gifts.ru/api/?action=getHappyLogs");
        jsonMin += 1000;
        jsonMax += 1000;
    }

    @Override
    public void onSuccessListener(List<Product> products) {

    }

    @Override
    public void onAddProduct(Product product) {
        mProduct = product;
        h.sendEmptyMessage(STATUS_GET_PRODUCT);
    }

    @Override
    public void onUpdateProgressBarListener(final int progress) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                value = progress;
                h.sendEmptyMessage(STATUS_SET_PROGRESS);
            }
        };
        thread.start();
    }

    @Override
    public void onSetMaxProgressBar(int max) {
        mMax = max;
        h.sendEmptyMessage(STATUS_SET_VISIBILITY);
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStartListener() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                h.sendEmptyMessage(STATUS_START_GETTING_INFO);
            }
        };
        thread.start();
    }

    @Override
    public void onFinishListener() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                h.sendEmptyMessage(STATUS_FINISH_GETTING_INFO);
            }
        };
        thread.start();
    }
}



