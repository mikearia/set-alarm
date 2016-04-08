package com.codelabs.alarm;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    Button showNotificationBut, stopNotificationBut, alertButton;
    NotificationManager notificationManager;
    boolean isNotificActive = false;
    int notifID = 33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showNotificationBut = (Button) findViewById(R.id.button_show);
        stopNotificationBut = (Button) findViewById(R.id.button_stop);
        alertButton = (Button) findViewById(R.id.button_alert);
    }

    public void showNotification(View view) {
        NotificationCompat.Builder notificBuilder = new
                NotificationCompat.Builder(this)
                .setContentTitle("Message")
                .setContentText("New Message")
                .setTicker("Alert New Message")
                .setSmallIcon(R.drawable.default_avatar);

        Intent moreInfoIntent = new Intent(this, MoreInfoNotification.class);
        TaskStackBuilder tStackBuilder = TaskStackBuilder.create(this);
        tStackBuilder.addParentStack(MoreInfoNotification.class);
        tStackBuilder.addNextIntent(moreInfoIntent);
        PendingIntent pendingIntent = tStackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
        notificBuilder.setContentIntent(pendingIntent);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notifID, notificBuilder.build());
        isNotificActive = true;
    }

    public void stopNotification(View view) {
        if (isNotificActive){
            notificationManager.cancel(notifID);
        }
    }

    public void setAlarm(View view){
        Long alertTime = new GregorianCalendar().getTimeInMillis()+5*1000;

        Intent alertIntent = new Intent(this, AlertReceiver.class);

        AlarmManager alarmManager = (AlarmManager)
                getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime,
                PendingIntent.getBroadcast(this, 1, alertIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT));
    }
}
