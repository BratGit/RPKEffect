package com.example.rpkeffect.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rpkeffect.Constructors.User;
import com.example.rpkeffect.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends BaseAdapter {
    private List<User> users;
    private LayoutInflater layoutInflater;

    public UserAdapter(Context context, List<User> userConstructs){
        this.users = userConstructs;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public UserAdapter(LayoutInflater layoutInflater, List<User> userConstructs){
        this.users = userConstructs;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.user_item, parent, false);
        User user = (User)getItem(position);

        CircleImageView civ = convertView.findViewById(R.id.profile_list_image);
        TextView mail = convertView.findViewById(R.id.profile_list_mail);
        TextView name = convertView.findViewById(R.id.profile_list_name);
        TextView date = convertView.findViewById(R.id.profile_list_date);
        TextView ip = convertView.findViewById(R.id.profile_list_ip);

        if(user.getImage() != null)
            Glide.with(convertView).load(String.valueOf(user.getImage())).into(civ);
        else
            civ.setImageResource(R.drawable.ic_user);
        mail.setText(user.getMail());
        name.setText(user.getName());
        date.setText(user.getDate());
        ip.setText(user.getIp());

        return convertView;
    }
}
