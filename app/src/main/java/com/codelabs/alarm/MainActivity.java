package com.codelabs.alarm;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    TimePicker timePicker;
    Switch turnAlarm;

    int hour;
    int minutes;

    NotificationManager notificationManager;
    boolean isNotificActive = false;
    int notifID = 33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Calendar calendar = Calendar.getInstance();
        hour = calendar.get(calendar.HOUR_OF_DAY);
        minutes = calendar.get(calendar.MINUTE);

        setContentView(R.layout.activity_main);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        turnAlarm = (Switch) findViewById(R.id.switch_turnalarm);

        turnAlarm.setChecked(true);
        turnAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    turnAlarm.setText("Turn Alarm On");
                    setAlarm();
                    showNotification(getCurrentFocus());
                } else {
                    turnAlarm.setText("Turn Alarm Off");
                    stopNotification();
                }
            }
        });
    }

    public void showNotification(View view) {
        NotificationCompat.Builder notificBuilder = new
                NotificationCompat.Builder(this)
                .setContentTitle("Message")
                .setContentText("New Message")
                .setTicker("Alert New Message")
                .setSmallIcon(R.drawable.ic_alarm_on_white_24dp);

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

    public void stopNotification() {
        if (isNotificActive){
            notificationManager.cancel(notifID);
        }
    }

    public void setAlarm(){
        Long alertTime = new GregorianCalendar().getTimeInMillis()+(hour+minutes)*1000;

        Intent alertIntent = new Intent(this, AlertReceiver.class);

        AlarmManager alarmManager = (AlarmManager)
                getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime,
                PendingIntent.getBroadcast(this, 1, alertIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Toast.makeText(this,String.valueOf(hour) + String.valueOf(minute) , Toast.LENGTH_SHORT).show();
    }
}
