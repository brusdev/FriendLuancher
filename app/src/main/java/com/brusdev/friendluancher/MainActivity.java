package com.brusdev.friendluancher;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    private Handler handler;

    private Intent dialerIntent;
    private Intent messagesIntent;
    private Intent cameraIntent;
    private Intent galleryIntent;
    private Intent otherIntent;

    private DateFormat dateFormat;
    private DateFormat timeFormat;

    private Button dateTimeButton;
    private Button statusButton;

    Drawable wifiOnDrawable;
    Drawable wifiOffDrawable;
    Drawable[] statusDrawables;

    BroadcastReceiver timeTickReceiver;
    BroadcastReceiver connectivityReceiver;
    BroadcastReceiver batteryChangedReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        dateFormat = android.text.format.DateFormat.getDateFormat(this);
        timeFormat = android.text.format.DateFormat.getTimeFormat(this);

        handler = new Handler();

        Intent packageIntent;
        ComponentName componentName;
        PackageManager packageManager = getPackageManager();

        packageIntent = new Intent(Intent.ACTION_DIAL, null);
        packageIntent.addCategory(Intent.CATEGORY_DEFAULT);
        componentName = packageIntent.resolveActivity(packageManager);
        if (componentName != null) {
            dialerIntent = packageManager.getLaunchIntentForPackage(componentName.getPackageName());
        }

        messagesIntent = new Intent(MainActivity.this, MessagesActivity.class);
        messagesIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        packageIntent = new Intent(Intent.ACTION_MAIN, null);
        packageIntent.addCategory(Intent.CATEGORY_APP_GALLERY);
        packageIntent.addCategory(Intent.CATEGORY_DEFAULT);
        componentName = packageIntent.resolveActivity(packageManager);
        if (componentName != null) {
            galleryIntent = packageManager.getLaunchIntentForPackage(componentName.getPackageName());
        }

        packageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        packageIntent.addCategory(Intent.CATEGORY_DEFAULT);
        componentName = packageIntent.resolveActivity(packageManager);
        if (componentName != null) {
            cameraIntent = packageManager.getLaunchIntentForPackage(componentName.getPackageName());
        }

        otherIntent = new Intent(MainActivity.this, OtherActivity.class);
        otherIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        dateTimeButton = findViewById(R.id.dateTimeButton);
        statusButton = findViewById(R.id.statusButton);

        wifiOnDrawable = getResources().getDrawable(R.drawable.ic_network_wifi_white_48dp);
        wifiOffDrawable = getResources().getDrawable(R.drawable.ic_signal_wifi_off_white_48dp);
        statusDrawables = statusButton.getCompoundDrawables();

        findViewById(R.id.phoneButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, AppAnalytics.DIALER_CONTENT_ID);
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                startActivity(MainActivity.this.dialerIntent);
            }
        });

        findViewById(R.id.messagesButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(MainActivity.this.messagesIntent);
            }
        });

        findViewById(R.id.cameraButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(MainActivity.this.cameraIntent);
            }
        });

        findViewById(R.id.photoButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(MainActivity.this.galleryIntent);
            }
        });

        findViewById(R.id.otherButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(MainActivity.this.otherIntent);
            }
        });

        timeTickReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateDateTime();
            }
        };

        connectivityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateConnectionStatus();
            }
        };

        batteryChangedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                        status == BatteryManager.BATTERY_STATUS_FULL;
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

                statusDrawables[2] = getBatteryDrawable(isCharging, level, scale);

                updateStatus();
            }
        };

        this.registerReceiver(timeTickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
        this.registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        this.registerReceiver(batteryChangedReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        updateDateTime();
        updateBatteryStatus();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        unregisterReceiver(timeTickReceiver);
        unregisterReceiver(connectivityReceiver);
        unregisterReceiver(batteryChangedReceiver);
    }
    private void updateDateTime() {
        Date now = new Date();
        String dateTimeText = dateFormat.format(now) + "\n\n" + timeFormat.format(now);
        dateTimeButton.setText(dateTimeText);
    }

    private void updateConnectionStatus() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean wifiConnected = false;
        boolean mobileConnected = false;
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        }

        statusDrawables[0] = wifiConnected ? wifiOnDrawable : wifiOffDrawable;

        updateStatus();
    }

    private void updateBatteryStatus() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, ifilter);

        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        statusDrawables[2] = getBatteryDrawable(isCharging, level, scale);

        updateStatus();
    }

    private void updateStatus() {
        statusButton.setCompoundDrawablesWithIntrinsicBounds(
                statusDrawables[0], statusDrawables[1], statusDrawables[2], statusDrawables[3]);
    }

    private Drawable getBatteryDrawable(boolean isCharging, int level, int scale) {
        Bitmap battery_source_bitmap = BitmapFactory.decodeResource(getResources(), isCharging ? R.drawable.ic_battery_charging_full_white_48dp : R.drawable.ic_battery_full_white_48dp);

        Paint battery_paint = new Paint();
        Bitmap battery_bitmap = Bitmap.createBitmap(battery_source_bitmap.getWidth(), battery_source_bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas battery_canvas = new Canvas(battery_bitmap);

        int levelHeight = battery_source_bitmap.getHeight() * level / scale;
        Rect topRect = new Rect(0, 0, battery_source_bitmap.getWidth(), battery_source_bitmap.getHeight() - levelHeight);
        Rect bottomRect = new Rect(0, battery_source_bitmap.getHeight() - levelHeight, battery_source_bitmap.getWidth(), battery_source_bitmap.getHeight());

        battery_paint.setAlpha(75);
        battery_canvas.drawBitmap(battery_source_bitmap, topRect, topRect, battery_paint);

        battery_canvas.drawBitmap(battery_source_bitmap, bottomRect, bottomRect, null);

        return new BitmapDrawable(getResources(), battery_bitmap);
    }
}
