package com.example.rpkeffect.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rpkeffect.R;
import com.example.rpkeffect.Constructors.User;
import com.example.rpkeffect.Adapters.UserAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserListFragment extends Fragment {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("user");
    ListView usersListView;
    List<User> userList;
    User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.user_list_layout, container, false);
        usersListView = (ListView) root.findViewById(R.id.listView_users);

        userList = new ArrayList<>();
        user = new User();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
//                    idProfile = GoogleSignIn.getLastSignedInAccount(getActivity());
                    User userConstruct = userSnapshot.getValue(User.class);
                    userList.add(userConstruct);
//                    Log.d("myLog", userConstruct.getName() + userList.size());
                }
                UserAdapter adapter = new UserAdapter(inflater, userList);
                usersListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return root;
    }
}
