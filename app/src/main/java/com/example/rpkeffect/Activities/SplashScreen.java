package com.example.rpkeffect.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.rpkeffect.Activities.AuthorizationActivity;
import com.example.rpkeffect.Utils.SaveState;

public class SplashScreen extends AppCompatActivity {
    SaveState saveState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        saveState = new SaveState(this);
        if (saveState.getState() == saveState.DARK_MODE_YES)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else if (saveState.getState() == saveState.DARK_MODE_NO)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        else if (saveState.getState() == saveState.DARK_MODE_USE_SYSTEM)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        else if (saveState.getState() == saveState.DARK_MODE_TIME)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_TIME);
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, AuthorizationActivity.class));
        finish();
    }
}
