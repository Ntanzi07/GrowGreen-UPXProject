package com.example.growgreen;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.growgreen.helpers.BottomMenuHelper;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        // Configura o menu inferior
        BottomMenuHelper.setupBottomMenu(this);
        BottomMenuHelper.updateMenuColors(this);

        // Seu código da tela About Us...
    }
}
