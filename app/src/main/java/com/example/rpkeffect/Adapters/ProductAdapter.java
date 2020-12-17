package com.example.rpkeffect.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.rpkeffect.Constructors.Product;
import com.example.rpkeffect.R;

import java.util.List;

public class ProductAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    List<Product> list;
    Context context;

    public ProductAdapter(Context context, LayoutInflater layoutInflater, List<Product> list) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.product_item, parent, false);
        TextView name, status, article, code;
        name = convertView.findViewById(R.id.name);
        status = convertView.findViewById(R.id.status);
        article = convertView.findViewById(R.id.article);
        code = convertView.findViewById(R.id.code);

        if (list.get(position).getName().equals("Нет товара")) name.setTextColor(ContextCompat.getColor(context, R.color.cherry));
        else name.setTextColor(ContextCompat.getColor(context, R.color.turquoise));

        if (list.get(position).getStatus().equals("10")) status.setTextColor(ContextCompat.getColor(context, R.color.cherry));
        else status.setTextColor(ContextCompat.getColor(context, R.color.turquoise));

        name.setText(list.get(position).getName());
        status.setText(list.get(position).getStatus());
        article.setText(list.get(position).getArticle());
        code.setText(list.get(position).getCode());

        return convertView;
    }
}
