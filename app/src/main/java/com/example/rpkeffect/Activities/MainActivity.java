package com.example.rpkeffect.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rpkeffect.R;
import com.example.rpkeffect.Constructors.User;
import com.example.rpkeffect.Utils.Utils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "myLog";

    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount signInAccount;

    private AppBarConfiguration mAppBarConfiguration;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    String name, email, image, ip, date, id;
    FirebaseAuth mAuth;
    FirebaseUser user;

    ArrayList<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        createRequest();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        mAuth = FirebaseAuth.getInstance();
        if (signInAccount == null && mAuth.getCurrentUser() == null){
            Toast.makeText(this, "Выполните авторизацию!!!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, AuthorizationActivity.class));
        }
        else if (signInAccount == null) {
            mAuth = FirebaseAuth.getInstance();
            Log.d(TAG, mAuth.getCurrentUser().toString());
            user = mAuth.getCurrentUser();


            if (user.getEmail() != null)
                name = user.getEmail();
            else name = null;

            if (user.getPhotoUrl() != null)
                image = user.getPhotoUrl().toString();
            else image = null;

            if (user.getEmail() != null)
                email = user.getEmail();
            else email = null;

            ip = Utils.getIPAddress(false);

            if (user.getUid() != null)
                id = user.getUid();
        } else {
            name = signInAccount.getDisplayName();
            image = signInAccount.getPhotoUrl().toString();
            email = signInAccount.getEmail();
            ip = Utils.getIPAddress(false);
            id = signInAccount.getId();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_first, R.id.nav_sec, R.id.nav_third, R.id.nav_profile, R.id.nav_user_list, R.id.nav_registrate_list)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy hh:mm");

        date = formatForDateNow.format(dateNow);

        myRef = database.getReference("user");

        userList = new ArrayList<>();
        myRef.push();
        User user = new User(name, image, email, date, ip);
//        myRef.setValue(user);
        myRef.child(id).setValue(user);


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User userConstruct = userSnapshot.getValue(User.class);
                    userList.add(userConstruct);
//                    Log.d("myLog", userConstruct.getName() + " " + userList.size() + " id - " + userConstruct.getId());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}