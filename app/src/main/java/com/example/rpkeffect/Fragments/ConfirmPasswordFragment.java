package com.example.rpkeffect.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.rpkeffect.R;
import com.example.rpkeffect.Constructors.Request;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class ConfirmPasswordFragment extends Fragment {
    public static final String TAG = "ConfirmPasswordFragment";

    ImageButton close;
    Bundle bundle;
    Button create;
    EditText password, confirm;
    String email;
    View snackView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_confirm_password, container, false);

        bundle = getArguments();
        close = root.findViewById(R.id.close);
        create = root.findViewById(R.id.button_confirm);
        password = root.findViewById(R.id.check_password);
        confirm = root.findViewById(R.id.check_confirm);
        snackView = root.findViewById(R.id.fragment_confirm_password);

        if (bundle.getString("email") != null)
            email = bundle.getString("email");

        mAuth = FirebaseAuth.getInstance();
        myRef = database.getReference("confirmed");

        final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_bottom);

        if (email != null && validateForm()) {
            create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAuth.createUserWithEmailAndPassword(email, password.getText().toString().replaceAll(" ", ""))
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                    myRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                                Request request = userSnapshot.getValue(Request.class);
                                                if (request.getEmail().equals(email)) userSnapshot.getRef().removeValue();
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                    Snackbar.make(snackView, R.string.added, Snackbar.LENGTH_SHORT).show();
                                    getActivity().finish();
                                }
                            });
                }
            });
        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ft.remove(ConfirmPasswordFragment.this);
                ft.commit();
            }
        });

        return root;
    }

    private boolean validateForm() {
        boolean validate = true;

        if (containsCyrillic(password.getText().toString())) {
            password.setError("Пароль не должен содержать кириллицу");
            validate = false;

            if (!password.getText().toString().equals(confirm.getText().toString())) {
                password.setError("Пароли не совпадают");
                validate = false;
            }
        } else {
            password.setError(null);
            validate = true;
        }

        return validate;
    }

    private boolean containsCyrillic(String s) {
        return Pattern.matches(".*\\p{InCyrillic}.*", s);
    }
}
