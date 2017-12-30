package com.brusdev.friendluancher;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {

    private Intent systemSettingsIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.settings_background)));
        getSupportActionBar().setLogo(R.drawable.ic_settings_white_48dp);
        getSupportActionBar().setTitle(getResources().getString(R.string.settings).toUpperCase());

        systemSettingsIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);

        findViewById(R.id.systemButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(systemSettingsIntent);
            }
        });
    }
}
