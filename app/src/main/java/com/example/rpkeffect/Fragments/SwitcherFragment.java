package com.example.rpkeffect.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class SwitcherFragment extends Fragment {
    SwitchCompat switchCompat;
    CheckBox checkBox;
    SaveState saveState;
    View snackView;
    int useSystem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_switcher, container, false);

        saveState = new SaveState(getContext());

        switchCompat = root.findViewById(R.id.switcher);
        checkBox = root.findViewById(R.id.checkbox);
        snackView = root.findViewById(R.id.fragment_switcher);

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
                } else {
                    switchCompat.setEnabled(true);
                }
                switcher();
            }
        });

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    saveState.setState(saveState.DARK_MODE_YES);
                } else {
                    saveState.setState(saveState.DARK_MODE_NO);
                }
                switcher();
            }
        });

        return root;
    }

    private void switcher(){
        switch(saveState.getState()){
            case 0://NIGHT_MODE_NO
                ((AppCompatActivity)getActivity()).getDelegate()
                            .setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case 1://NIGHT_MODE_YES
                ((AppCompatActivity)getActivity()).getDelegate()
                        .setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case 2://NIGHT_MODE_FOLLOW_SYSTEM
                if (checkBox.isChecked())
                    Snackbar.make(snackView, "Изменения вступят в силу после перезапускка приложения",
                        Snackbar.LENGTH_SHORT).show();
                break;
        }
        useSystem++;
    }
}
