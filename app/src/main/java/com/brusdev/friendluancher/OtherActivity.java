package com.brusdev.friendluancher;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class OtherActivity extends AppCompatActivity {

    private Intent appsIntent;
    private Intent galleryIntent;
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

        Intent packageIntent;
        ComponentName componentName;
        PackageManager packageManager = getPackageManager();

        appsIntent = new Intent(OtherActivity.this, AppsActivity.class);
        //appsIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        packageIntent = new Intent(Intent.ACTION_MAIN, null);
        packageIntent.addCategory(Intent.CATEGORY_APP_GALLERY);
        packageIntent.addCategory(Intent.CATEGORY_DEFAULT);
        componentName = packageIntent.resolveActivity(packageManager);
        if (componentName != null) {
            galleryIntent = packageManager.getLaunchIntentForPackage(componentName.getPackageName());
        }

        settingsIntent = new Intent(OtherActivity.this, SettingsActivity.class);

        findViewById(R.id.appsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(appsIntent);
            }
        });

        findViewById(R.id.photoButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(galleryIntent);
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
