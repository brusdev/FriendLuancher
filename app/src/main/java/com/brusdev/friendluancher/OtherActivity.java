package com.brusdev.friendluancher;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class OtherActivity extends AppCompatActivity {

    private Intent appsIntent;
    private Intent settingsIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.other_background)));
        getSupportActionBar().setLogo(R.drawable.ic_more_vert_white_24dp);
        getSupportActionBar().setTitle(getResources().getString(R.string.other).toUpperCase());

        appsIntent = new Intent(OtherActivity.this, AppsActivity.class);
        appsIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        settingsIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);

        findViewById(R.id.appsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(appsIntent);
            }
        });

        findViewById(R.id.settingsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(settingsIntent);
            }
        });
    }
}
