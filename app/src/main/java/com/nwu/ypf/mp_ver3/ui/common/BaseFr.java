package com.nwu.ypf.mp_ver3.ui.common;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class BaseFr extends Fragment {

    protected AppCompatActivity requireCompatAty() {
        return (AppCompatActivity) requireActivity();
    }

    protected void setupActionBar(@NonNull Toolbar toolbar, boolean showBackButton) {
        requireCompatAty().setSupportActionBar(toolbar);
        requireCompatAty().getSupportActionBar().setDisplayHomeAsUpEnabled(showBackButton);
        if (showBackButton) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNavigationClick();
                }
            });
        }
    }

    protected void onNavigationClick() {
        Navigation.findNavController(getView()).navigateUp();
    }

}
