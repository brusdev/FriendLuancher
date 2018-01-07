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
        getSupportActionBar().setLogo(R.drawable.ic_settings_white_24dp);
        getSupportActionBar().setTitle(getResources().getString(R.string.settings).toUpperCase());

        systemSettingsIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);

        findViewById(R.id.emergencyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, EmergencyActivity.class);
                intent.putExtra(EmergencyActivity.ACTIVITY_MODE_KEY, EmergencyActivity.ACTIVITY_MODE_SET);
                startActivity(intent);
            }
        });

        findViewById(R.id.favoritesButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, FavoritesActivity.class);
                intent.putExtra(FavoritesActivity.ACTIVITY_MODE_KEY, FavoritesActivity.ACTIVITY_MODE_SET);
                startActivity(intent);
            }
        });

        findViewById(R.id.systemButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(systemSettingsIntent);
            }
        });
    }
}
