package com.example.rpkeffect.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.rpkeffect.R;
import com.example.rpkeffect.Utils.SaveState;

public class SwitcherFragment extends Fragment {
    SwitchCompat switchCompat;
    CheckBox checkBox;
    SaveState saveState;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_switcher, container, false);

        saveState = new SaveState(getContext());

        switchCompat = root.findViewById(R.id.switcher);
        checkBox = root.findViewById(R.id.checkbox);

        if (saveState.getState() == saveState.DARK_MODE_YES)
            switchCompat.setChecked(true);
        else if (saveState.getState() == saveState.DARK_MODE_NO)
            switchCompat.setChecked(false);
        else if (saveState.getState() == saveState.DARK_MODE_USE_SYSTEM){
            switchCompat.setEnabled(false);
            checkBox.setChecked(true);
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    saveState.setState(saveState.DARK_MODE_USE_SYSTEM);
                    switchCompat.setEnabled(false);
                    ((AppCompatActivity)getActivity()).getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                } else {
                    switchCompat.setEnabled(true);
                    if (switchCompat.isChecked()) {
                        ((AppCompatActivity) getActivity()).getDelegate()
                                .setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        saveState.setState(saveState.DARK_MODE_YES);
                    }
                    else
                        ((AppCompatActivity)getActivity()).getDelegate()
                                .setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    saveState.setState(saveState.DARK_MODE_NO);
                }
            }
        });

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    saveState.setState(saveState.DARK_MODE_YES);
                    ((AppCompatActivity)getActivity()).getDelegate()
                            .setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    saveState.setState(saveState.DARK_MODE_NO);
                    ((AppCompatActivity)getActivity()).getDelegate()
                            .setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });

        return root;
    }
}
