package com.example.rpkeffect.Fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rpkeffect.R;
import com.example.rpkeffect.Constructors.Request;
import com.example.rpkeffect.Adapters.RequestAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RegistrationListFragment extends Fragment {
    private final static String TAG = "myLog";
    LinearLayout snackView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("registration_list");
    DatabaseReference registrationRef = database.getReference("confirmed");
    DatabaseReference confirmed;
    ListView registrationListView;
    Request request;
    private FirebaseAuth mAuth;
    Dialog confirmDialog;
    List<Request> requestList;

    View root;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.registration_list_fragment, container, false);

        registrationListView = (ListView) root.findViewById(R.id.registrationListView);
        request = new Request();
        requestList = new ArrayList<>();

        snackView = root.findViewById(R.id.registration_list);

        mAuth = FirebaseAuth.getInstance();

        confirmDialog = new Dialog(getActivity());
        confirmDialog
                .getWindow()
                .requestFeature(Window.FEATURE_NO_TITLE);

        confirmDialog
                .getWindow()
                .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        confirmDialog
                .getWindow()
                .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        confirmDialog.setCancelable(true);


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requestList.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Request request = userSnapshot.getValue(Request.class);
                    requestList.add(request);
                }
                RequestAdapter adapter = new RequestAdapter(inflater, requestList);
                registrationListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        registrationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDialog(position, inflater);
            }
        });

        return root;
    }

    private void showDialog(final int position, final LayoutInflater inflater) {
        confirmDialog.setContentView(R.layout.request_dialog);


        final ImageButton confirm, reject, cancel;
        TextView mail, ip, date;

        mail = confirmDialog.findViewById(R.id.confirm_email);
        ip = confirmDialog.findViewById(R.id.confirm_ip);
        date = confirmDialog.findViewById(R.id.confirm_date);

        mail.setText(requestList.get(position).getEmail());
        ip.setText(requestList.get(position).getIp());
        date.setText(requestList.get(position).getDate());

        confirm = confirmDialog.findViewById(R.id.button_confirm);
        reject = confirmDialog.findViewById(R.id.button_reject);
        cancel = confirmDialog.findViewById(R.id.button_cancel);

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            Request request = userSnapshot.getValue(Request.class);
                            if (request.getEmail().equals(requestList.get(position).getEmail())) {
                                userSnapshot.getRef().removeValue();
                            }
                        }
                        RequestAdapter adapter = new RequestAdapter(inflater, requestList);
                        registrationListView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                confirmDialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmed = database.getReference("registration_list");
                confirmed.push();
                confirmed.child(requestList.get(position).getEmail().replaceAll("\\.", "-"))
                        .setValue(requestList.get(position));

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            Request request = userSnapshot.getValue(Request.class);
                            if (request.getEmail().equals(requestList.get(position).getEmail())) {
                                userSnapshot.getRef().removeValue();
                                registrationRef.push();
                                registrationRef.child(request.getEmail().replaceAll("\\.", "-")).setValue(request);

                            }
                        }
                        RequestAdapter adapter = new RequestAdapter(inflater, requestList);
                        registrationListView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                confirmDialog.dismiss();
            }
        });

        confirmDialog.show();
    }
}
