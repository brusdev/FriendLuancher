package com.brusdev.friendluancher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class FavoritesActivity extends AppCompatActivity {

    private int activityMode;

    private Button favourite1Button;
    private Button favourite2Button;
    private Button favourite3Button;
    private Button favourite4Button;
    private ApkInfoExtractor mApkInfoExtractor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.favourites_background)));
        getSupportActionBar().setLogo(R.drawable.ic_filter_list_white_24dp);
        getSupportActionBar().setTitle(getResources().getString(R.string.favourites).toUpperCase());

        activityMode = ACTIVITY_MODE_RUN;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            activityMode = bundle.getInt(ACTIVITY_MODE_KEY, ACTIVITY_MODE_RUN);
        }

        mApkInfoExtractor = new ApkInfoExtractor(this);

        favourite1Button = findViewById(R.id.favourite1Button);
        favourite2Button = findViewById(R.id.favourite2Button);
        favourite3Button = findViewById(R.id.favourite3Button);
        favourite4Button = findViewById(R.id.favourite4Button);

        favourite1Button.setTag(R.string.index, FAVOURITE_INDEX_1);
        favourite2Button.setTag(R.string.index, FAVOURITE_INDEX_2);
        favourite3Button.setTag(R.string.index, FAVOURITE_INDEX_3);
        favourite4Button.setTag(R.string.index, FAVOURITE_INDEX_4);

        String packageNameFavourite1;
        String packageNameFavourite2;
        String packageNameFavourite3;
        String packageNameFavourite4;
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        packageNameFavourite1 = sharedPref.getString(PACKAGE_NAME_FAVOURITE_1, null);
        packageNameFavourite2 = sharedPref.getString(PACKAGE_NAME_FAVOURITE_2, null);
        packageNameFavourite3 = sharedPref.getString(PACKAGE_NAME_FAVOURITE_3, null);
        packageNameFavourite4 = sharedPref.getString(PACKAGE_NAME_FAVOURITE_4, null);


        if (packageNameFavourite1 != null) {
            setFavouriteButton(favourite1Button, packageNameFavourite1);
        }
        if (packageNameFavourite2 != null) {
            setFavouriteButton(favourite2Button, packageNameFavourite2);
        }
        if (packageNameFavourite3 != null) {
            setFavouriteButton(favourite3Button, packageNameFavourite3);
        }
        if (packageNameFavourite4 != null) {
            setFavouriteButton(favourite4Button, packageNameFavourite4);
        }

        View.OnClickListener favouriteButtonOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activityMode == ACTIVITY_MODE_RUN) {
                    Object packageName = view.getTag(R.string.packageName);

                    if (packageName != null) {
                        Intent intent = FavoritesActivity.this.getPackageManager().
                                getLaunchIntentForPackage((String)packageName);
                        FavoritesActivity.this.startActivity(intent);
                    }
                } else {
                    Object index = view.getTag(R.string.index);
                    Intent intent = new Intent(FavoritesActivity.this, AppsActivity.class);
                    intent.putExtra(AppsActivity.ACTIVITY_MODE_KEY, AppsActivity.ACTIVITY_MODE_SET);
                    FavoritesActivity.this.startActivityForResult(intent, (int)index);
                }
            }
        };


        favourite1Button.setOnClickListener(favouriteButtonOnClickListener);
        favourite2Button.setOnClickListener(favouriteButtonOnClickListener);
        favourite3Button.setOnClickListener(favouriteButtonOnClickListener);
        favourite4Button.setOnClickListener(favouriteButtonOnClickListener);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check whether the result is ok
        if (resultCode == RESULT_OK) {
            String packageName = data.getStringExtra("packageName");

            SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            switch (requestCode) {
                case FAVOURITE_INDEX_1:
                    setFavouriteButton(favourite1Button, packageName);
                    editor.putString(PACKAGE_NAME_FAVOURITE_1, packageName);
                    break;
                case FAVOURITE_INDEX_2:
                    setFavouriteButton(favourite2Button, packageName);
                    editor.putString(PACKAGE_NAME_FAVOURITE_2, packageName);
                    break;
                case FAVOURITE_INDEX_3:
                    setFavouriteButton(favourite3Button, packageName);
                    editor.putString(PACKAGE_NAME_FAVOURITE_3, packageName);
                    break;
                case FAVOURITE_INDEX_4:
                    setFavouriteButton(favourite4Button, packageName);
                    editor.putString(PACKAGE_NAME_FAVOURITE_4, packageName);
                    break;
            }

            editor.commit();
        } else {
            Log.e("FavoritesActivity", "Failed to pick app");
        }
    }

    private void setFavouriteButton(Button button, String packageName) {
        button.setCompoundDrawablesWithIntrinsicBounds(mApkInfoExtractor.getAppIconByPackageName(packageName), null, null, null);
        button.setText(mApkInfoExtractor.GetAppName(packageName));
    }

    private static final int FAVOURITE_INDEX_1 = 1;
    private static final int FAVOURITE_INDEX_2 = 2;
    private static final int FAVOURITE_INDEX_3 = 3;
    private static final int FAVOURITE_INDEX_4 = 4;

    public static final String PACKAGE_NAME_FAVOURITE_1 = "PACKAGE_NAME_FAVOURITE_1";
    public static final String PACKAGE_NAME_FAVOURITE_2 = "PACKAGE_NAME_FAVOURITE_2";
    public static final String PACKAGE_NAME_FAVOURITE_3 = "PACKAGE_NAME_FAVOURITE_3";
    public static final String PACKAGE_NAME_FAVOURITE_4 = "PACKAGE_NAME_FAVOURITE_4";

    public static final String ACTIVITY_MODE_KEY = "ACTIVITY_MODE";
    public static final int ACTIVITY_MODE_RUN = 1;
    public static final int ACTIVITY_MODE_SET = 2;
}
