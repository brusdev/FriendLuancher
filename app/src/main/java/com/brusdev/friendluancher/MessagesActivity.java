package com.brusdev.friendluancher;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class MessagesActivity extends AppCompatActivity {

    public static final String WHATSAPP_PACKAGE_NAME = "com.whatsapp";
    private Intent smsIntent;
    private Intent mailIntent;
    private Intent whatsappIntent;
    private Button smsButton;
    private Button mailButton;
    private Button whatsappButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.messages_background)));
        getSupportActionBar().setLogo(R.drawable.ic_message_white_24dp);
        getSupportActionBar().setTitle(getResources().getString(R.string.messages).toUpperCase());

        smsButton = findViewById(R.id.smsButton);
        mailButton = findViewById(R.id.mailButton);
        whatsappButton = findViewById(R.id.whatsappButton);


        Intent packageIntent;
        ComponentName componentName;
        List<ResolveInfo> resolveInfos;
        PackageManager packageManager = getPackageManager();
        ApkInfoExtractor apkInfoExtractor = new ApkInfoExtractor(this);

        packageIntent = new Intent(Intent.ACTION_VIEW, null);
        packageIntent.setData(Uri.parse("sms:"));
        packageIntent.resolveActivity(packageManager);
        componentName = packageIntent.resolveActivity(packageManager);
        if (componentName != null) {
            smsButton.setCompoundDrawablesWithIntrinsicBounds(null, apkInfoExtractor.getAppIconByPackageName(componentName.getPackageName()), null, null);
            smsIntent = packageManager.getLaunchIntentForPackage(componentName.getPackageName());
        } else {
            smsButton.setVisibility(View.GONE);
        }

        packageIntent = new Intent(Intent.ACTION_SEND);
        packageIntent.setType("message/rfc822");
        resolveInfos = packageManager.queryIntentActivities(packageIntent, 0);
        if (resolveInfos.size() > 0) {
            mailButton.setCompoundDrawablesWithIntrinsicBounds(null, apkInfoExtractor.getAppIconByPackageName(resolveInfos.get(0).activityInfo.packageName), null, null);
            mailIntent = packageManager.getLaunchIntentForPackage(resolveInfos.get(0).activityInfo.packageName);
        } else {
            mailButton.setVisibility(View.GONE);
        }

        whatsappButton.setCompoundDrawablesWithIntrinsicBounds(null, apkInfoExtractor.getAppIconByPackageName(WHATSAPP_PACKAGE_NAME), null, null);
        whatsappIntent = packageManager.getLaunchIntentForPackage(WHATSAPP_PACKAGE_NAME);


        smsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(MessagesActivity.this.smsIntent);
            }
        });

        mailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(MessagesActivity.this.mailIntent);
            }
        });

        whatsappButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(MessagesActivity.this.whatsappIntent);
            }
        });
    }
}
