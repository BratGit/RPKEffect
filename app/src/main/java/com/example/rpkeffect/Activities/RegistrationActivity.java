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
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.rpkeffect.Fragments.CheckFragment;
import com.example.rpkeffect.R;
import com.example.rpkeffect.Constructors.Request;
import com.example.rpkeffect.Utils.Utils;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    public ProgressDialog mProgressDialog;
    private FirebaseAuth mAuth;
    private static final String TAG = "myLog";
    EditText login;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    View snackView;
    CheckFragment checkFragment;
    Button registrate, check;
//    ImageButton back;
    String ip, date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_layout);

        checkFragment = new CheckFragment();

        snackView = findViewById(R.id.layout_registration);

//        back = findViewById(R.id.button_back);
        login = findViewById(R.id.loginRegistration);
        registrate = findViewById(R.id.button_registration);
        check = findViewById(R.id.button_check);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_top, R.anim.slide_in_right);
                ft.replace(R.id.layout_registration, checkFragment, CheckFragment.TAG);
                ft.commit();
            }
        });

//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle bundle = null;
//
//                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
//                    View view = findViewById(R.id.auth_logo);
//                    if (view != null) {
//                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegistrationActivity.this, view, getString(R.string.transition));
//                        bundle = options.toBundle();
//                    }
//                }
//
//                Intent intent = new Intent(RegistrationActivity.this, AuthorizationActivity.class);
//                if (bundle == null) {
//                    startActivity(intent);
//                } else {
//                    startActivity(intent, bundle);
//                }
//            }
//        });

        registrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(snackView.getWindowToken(), 0);
                createAccount(login.getText().toString().replaceAll(" ", ""));
            }
        });

        mAuth = FirebaseAuth.getInstance();
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    public boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean containsCyrillic(String s) {
        return Pattern.matches(".*\\p{InCyrillic}.*", s);
    }

    private boolean validateForm() {
        boolean valid = true;

        String loginText = login.getText().toString().replaceAll(" ", "");
        if (TextUtils.isEmpty(loginText)) {
            login.setError(getString(R.string.required));
            valid = false;
            if (containsCyrillic(loginText)) {
                login.setError(getString(R.string.contains_cyrillic));
                valid = false;
            } else if (isEmailValid(loginText)) {
                login.setError(getString(R.string.email_incorrect_format));
                valid = false;
            }
            else {
                login.setError(null);
            }
        } else {
            login.setError(null);
        }

        return valid;
    }

    private void createAccount(final String email) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START create_user_with_email]

        myRef = database.getReference("registration_list");

        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy hh:mm");

        date = formatForDateNow.format(dateNow);
        ip = Utils.getMACAddress("wlan0");
        Request request = new Request(email, ip, date);

        Log.d(TAG, "IPv6: " + ip);

        myRef.push();
        myRef.child(email.replaceAll("\\.", "-")).setValue(request);

        hideProgressDialog();
        Snackbar.make(snackView, R.string.created_request, Snackbar.LENGTH_SHORT).show();
        finish();
    }
}
