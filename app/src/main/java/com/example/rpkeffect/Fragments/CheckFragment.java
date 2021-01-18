package com.example.rpkeffect.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.rpkeffect.R;
import com.example.rpkeffect.Constructors.Request;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CheckFragment extends Fragment {
    FragmentTransaction fm;
    public static final String TAG = "myLog";
    ImageButton close;
    Button next;
    ConfirmPasswordFragment confirmPasswordFragment;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    EditText email;
    View view;
    View snackView;
    int auth = 0;
    final int AUTH_FAIL = 0;
    final int AUTH_SUCCESS = 1;
    final int AUTH_POST_SUCCESS = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_check, container, false);
        fm = getActivity().getSupportFragmentManager().beginTransaction();
        next = root.findViewById(R.id.next);
        close = root.findViewById(R.id.close);
        email = root.findViewById(R.id.check_email);
        snackView = root.findViewById(R.id.check_fragment);
        myRef = database.getReference("confirmed");
        view = root.findViewById(R.id.check_fragment);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            Request request = userSnapshot.getValue(Request.class);
                            if (request.getEmail().equals(email.getText().toString().replaceAll(" ", ""))) {
                                auth = AUTH_SUCCESS;
                            }
                        }
                        if (auth == AUTH_SUCCESS) {
                            confirmPasswordFragment = new ConfirmPasswordFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("email", email.getText().toString().replaceAll(" ", ""));
                            confirmPasswordFragment.setArguments(bundle);
                            Log.d(TAG, "onDataChange: calling commit");
                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction
                                    .remove(CheckFragment.this)
                                    .commit();
                            fm
                                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_right)
                                    .replace(R.id.layout_registration, confirmPasswordFragment, ConfirmPasswordFragment.TAG)
                                    .commit();
                            auth = AUTH_POST_SUCCESS;
                        }
                        else if (auth == AUTH_FAIL){
                            Snackbar.make(
                                    view, "Данной подтверждённой заявки не найдено",
                                    Snackbar.LENGTH_SHORT)
                                    .show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_bottom)
                        .remove(CheckFragment.this)
                        .commit();
            }
        });

        return root;
    }
}
