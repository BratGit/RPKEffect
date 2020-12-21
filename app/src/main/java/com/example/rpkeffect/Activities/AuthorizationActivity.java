package com.example.rpkeffect.Activities;


import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.rpkeffect.R;
import com.example.rpkeffect.Utils.SaveState;
import com.example.rpkeffect.Utils.Utils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import de.hdodenhof.circleimageview.CircleImageView;

public class AuthorizationActivity extends AppCompatActivity {
    public ProgressDialog mProgressDialog;
    View snackView;
    GoogleSignInAccount signInAccount;
    CircleImageView image;
    TextView gotoRegistration;
    EditText login, password;
    Button enter;
    SaveState saveState;
    private static final String TAG = "myLog";

    GoogleSignInClient mGoogleSignInClient;
    SignInButton signin;
    int RC_SIGN_IN = 0;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
//            fillIn();
//            signOut();
        }
//        signOut();
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "onBackPressed", Toast.LENGTH_SHORT).show();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkForSignedUp();

        setContentView(R.layout.activity_authorization);

        saveState = new SaveState(this);
//        Log.d("myLog", "darkMode " + saveState.getState());
        if (saveState.getState() == saveState.DARK_MODE_YES)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else if (saveState.getState() == saveState.DARK_MODE_NO)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        else if (saveState.getState() == saveState.DARK_MODE_USE_SYSTEM)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

//        showProgressDialog();
//        if(!checkForSignedUp())
//            hideProgressDialog();

        snackView = findViewById(R.id.layout_auth);

        gotoRegistration = findViewById(R.id.goto_registration);
        gotoRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = null;

//                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
//                    View view = findViewById(R.id.auth_logo);
//                    if (view != null) {
//                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AuthorizationActivity.this, view, getString(R.string.transition));
//                        bundle = options.toBundle();
//                    }
//                }
//
//                Intent intent = new Intent(AuthorizationActivity.this, RegistrationActivity.class);
//                if (bundle == null) {
//                    startActivity(intent);
//                } else {
//                    startActivity(intent, bundle);
//                }
                startActivity(new Intent(AuthorizationActivity.this, RegistrationActivity.class));
            }
        });

        signin = findViewById(R.id.sign_in_button);

        enter = findViewById(R.id.enterButton);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(snackView.getWindowToken(), 0);
                enterByLogin();
            }
        });

        login = findViewById(R.id.login);
        password = findViewById(R.id.password);

        signin.setSize(SignInButton.SIZE_STANDARD);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }
        });

        Log.d(TAG, "MAC-address(wlan0): " + Utils.getMACAddress("wlan0"));
        Log.d(TAG, "MAC-address(eth0): " + Utils.getMACAddress("eth0"));
        Log.d(TAG, "IPv4: " + Utils.getIPAddress(true));
        Log.d(TAG, "IPv6: " + Utils.getIPAddress(false));

    }

    private void checkForSignedUp() {
        createRequest();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(AuthorizationActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void enterByLogin() {
        Log.d(TAG, "signIn:" + login.getText().toString());
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(login.getText().toString().replaceAll(" ", ""), password.getText().toString().replaceAll(" ", ""))
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Snackbar.make(snackView, R.string.auth_failed, Snackbar.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "success auth with " + mAuth.getCurrentUser().getEmail());
                            Intent intent = new Intent(AuthorizationActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        hideProgressDialog();
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String loginText = login.getText().toString();
        if (TextUtils.isEmpty(loginText)) {
            login.setError("Required.");
            valid = false;
        } else {
            login.setError(null);
        }

        String passwordText = password.getText().toString();
        if (TextUtils.isEmpty(passwordText)) {
            password.setError("Required.");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }

    void updateUI(FirebaseUser user) {
        if (user != null) {
            Log.d(TAG, user.getEmail());
        }
    }

    private void createRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("myLog", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } catch (ApiException e) {
                Log.w("myLog", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("myLog", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            Log.w("myLog", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }
}
