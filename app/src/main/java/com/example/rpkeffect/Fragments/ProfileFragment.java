package com.example.rpkeffect.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.rpkeffect.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private final static String TAG = "myLog";

    GoogleSignInAccount signInAccount;
    GoogleSignInClient mGoogleSignInClient;

    FirebaseAuth mAuth;
    FirebaseUser user;

    Uri personPhoto;
    CircleImageView civ;
    TextView email;
    TextView name;
    Button exit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        civ = (CircleImageView) root.findViewById(R.id.profile_image);
        email = (TextView) root.findViewById(R.id.email);
        name = (TextView) root.findViewById(R.id.name);
        exit = (Button) root.findViewById(R.id.exit_button);

        createRequest();
        fillIn();

        return root;
    }

    private void fillIn(){
        signInAccount = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (signInAccount != null){
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.exit_button:
                            signOut();
                            break;
                    }
                }
            });

            name.setText(signInAccount.getDisplayName());
            email.setText(signInAccount.getEmail());
            personPhoto = signInAccount.getPhotoUrl();

            Glide.with(this).load(String.valueOf(personPhoto)).into(civ);
        }
        else{
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signOutFB();
                }
            });
            name.setText(user.getDisplayName());
            email.setText(user.getEmail());
            civ.setImageResource(R.drawable.ic_user);
        }
    }

    private void createRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        getActivity().finish();
                    }
                });
        mAuth.signOut();
    }

    ///Sign out from Firebase
    private void signOutFB(){
        mAuth.signOut();
        getActivity().finish();
    }
}
