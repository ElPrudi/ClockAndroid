package com.example.clock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Calendar;

public class Alarm {
    private static final long RUN_DAILY = 24 * 60 * 60 * 1000;

    private int id, hour, minute;
    private boolean started, recurring;

    public Alarm(int id, int hour, int minute, boolean started, boolean recurring) {
        this.id = id;
        this.hour = hour;
        this.minute = minute;
        this.started = started;
        this.recurring = recurring;
    }

    public void schedule(Context context, Window window, Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // RTC_WAKEUP wakes up the device, timeInMillis -> Time selected by the user, INTERVAL_DAY -> repeated every day
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), Alarm.RUN_DAILY, pendingIntent);
        this.started = true;
    }

    public void cancel(Context context, TextView text) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);
        alarmManager.cancel(pendingIntent);
        this.started = false;
        text.setText(R.string.no_alarm);
    }
}
