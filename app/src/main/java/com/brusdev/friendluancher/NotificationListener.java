package com.brusdev.friendluancher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.Arrays;

public class NotificationListener extends NotificationListenerService {
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver notificationGetAllReceiver;
    private BroadcastReceiver notificationClearAllReceiver;

    @Override
    public void onCreate() {
        super.onCreate();

        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());

        notificationGetAllReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                StatusBarNotification[] notifications = NotificationListener.this.getActiveNotifications();

                Intent notificationListIntent = new Intent(ACTION_NOTIFICATION_LIST);
                notificationListIntent.putParcelableArrayListExtra(NOTIFICATION_LIST_EXTRA,
                        (ArrayList<StatusBarNotification>)Arrays.asList(notifications));
                localBroadcastManager.sendBroadcast(notificationListIntent);
            }
        };
        registerReceiver(notificationGetAllReceiver, new IntentFilter(ACTION_NOTIFICATION_GET_ALL));

        notificationClearAllReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                cancelAllNotifications();
            }
        };
        registerReceiver(notificationClearAllReceiver, new IntentFilter(ACTION_NOTIFICATION_CLEAR_ALL));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(notificationGetAllReceiver);
        unregisterReceiver(notificationClearAllReceiver);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        localBroadcastManager.sendBroadcast(new Intent(ACTION_NOTIFICATION_POSTED));
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        localBroadcastManager.sendBroadcast(new Intent(ACTION_NOTIFICATION_REMOVED));
    }

    public static final String NOTIFICATION_LIST_EXTRA = "NOTIFICATION_LIST";
    public static final String ACTION_NOTIFICATION_LIST = "brusdev.intent.action.NOTIFICATION_LIST";
    public static final String ACTION_NOTIFICATION_GET_ALL = "brusdev.intent.action.NOTIFICATION_GET_ALL";
    public static final String ACTION_NOTIFICATION_CLEAR_ALL = "brusdev.intent.action.NOTIFICATION_CLEAR_ALL";
    public static final String ACTION_NOTIFICATION_POSTED = "brusdev.intent.action.NOTIFICATION_POSTED";
    public static final String ACTION_NOTIFICATION_REMOVED = "brusdev.intent.action.NOTIFICATION_REMOVED";

}