package com.brusdev.friendluancher;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.GridLayoutManager;

public class AppsActivity extends AppCompatActivity {

    private int activityMode;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps);

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.apps_background)));
        getSupportActionBar().setLogo(R.drawable.ic_apps_white_24dp);
        getSupportActionBar().setTitle(getResources().getString(R.string.applications).toUpperCase());

        activityMode = ACTIVITY_MODE_RUN;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            activityMode = bundle.getInt(ACTIVITY_MODE_KEY, ACTIVITY_MODE_RUN);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // Passing the column number 1 to show online one column in each row.
        recyclerViewLayoutManager = new GridLayoutManager(AppsActivity.this, 1);

        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        adapter = new AppsAdapter(AppsActivity.this, new ApkInfoExtractor(AppsActivity.this).GetAllInstalledApkInfo());
        ((AppsAdapter)adapter).setMode(activityMode);

        recyclerView.setAdapter(adapter);
    }

    public static final String ACTIVITY_MODE_KEY = "ACTIVITY_MODE";
    public static final int ACTIVITY_MODE_RUN = 1;
    public static final int ACTIVITY_MODE_SET = 2;
}