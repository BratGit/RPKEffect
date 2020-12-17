package com.example.rpkeffect.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
    public static final String TAG = "myLog";
    ImageButton close;
    Button next;
    ConfirmPasswordFragment confirmPasswordFragment;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    EditText email;
    View snackView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_check, container, false);

        next = root.findViewById(R.id.next);
        close = root.findViewById(R.id.close);
        email = root.findViewById(R.id.check_email);
        snackView = root.findViewById(R.id.check_fragment);
        myRef = database.getReference("confirmed");
        confirmPasswordFragment = new ConfirmPasswordFragment();

        final FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();

        //TODO сделать разделение между строками "Заявка на рассмотрении" и "Вы не подавали заявку"
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            Request request = userSnapshot.getValue(Request.class);
                            if (request.getEmail().equals(email.getText().toString().replaceAll(" ", ""))){
                                Bundle bundle = new Bundle();
                                bundle.putString("email", email.getText().toString().replaceAll(" ", ""));
                                confirmPasswordFragment.setArguments(bundle);
                                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                                fm
                                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_right)
                                        .replace(R.id.layout_registration, confirmPasswordFragment, ConfirmPasswordFragment.TAG)
                                        .commit();
                            }

                        }
                        Snackbar.make(snackView, R.string.auth_failed, Snackbar.LENGTH_SHORT).show();
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
                Log.d(TAG, "fragment removed");
            }
        });

        return root;
    }
}
