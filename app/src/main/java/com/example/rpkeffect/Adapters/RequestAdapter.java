package com.example.rpkeffect.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rpkeffect.Constructors.Request;
import com.example.rpkeffect.R;

import java.util.List;

public class RequestAdapter extends BaseAdapter {
    private List<Request> requests;
    private LayoutInflater layoutInflater;

    public RequestAdapter(Context context, List<Request> requests){
        this.requests = requests;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public RequestAdapter(LayoutInflater layoutInflater, List<Request> requests){
        this.requests = requests;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public int getCount() {
        return requests.size();
    }

    @Override
    public Object getItem(int position) {
        return requests.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.request_item, parent, false);

        TextView email, date, ip;
        email = convertView.findViewById(R.id.request_mail);
        date = convertView.findViewById(R.id.request_date);
        ip = convertView.findViewById(R.id.request_ip);

        email.setText(requests.get(position).getEmail());
        ip.setText(requests.get(position).getIp());
        date.setText(requests.get(position).getDate());

        return convertView;
    }
}
