package com.example.clock;

import static java.lang.String.format;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.example.clock.databinding.ActivityMainBinding;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static final String CHANNEL_ID = "clock";
    public static final String CHANNEL_NAME = "T.T. Alarm Manager";
    public static final String CHANNEL_DESC = "Alarm Manager Channel";

    private Calendar calendar;
    private MaterialTimePicker timePicker;
    private AlarmManager alarmManager;

    public ActivityMainBinding binding;
    private Alarm currentAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        binding.selectedTime.setText(R.string.no_alarm);
        setContentView(binding.getRoot());
        createNotificationChannel();
        initListeners();
    }

    private void initListeners() {
        binding.setTime.setOnClickListener(view -> showTimePicker());
        binding.cancelTime.setOnClickListener(view -> cancelAlarm());
    }

    private void cancelAlarm() {
        if (currentAlarm != null) currentAlarm.cancel(this, binding.selectedTime);
    }

    private void setAlarm() {
        Alarm alarm = new Alarm(new Random().nextInt(Integer.MAX_VALUE), timePicker.getHour(), timePicker.getMinute(), true, true);
        alarm.schedule(this, getWindow(), calendar);
        currentAlarm = alarm;
    }

    @SuppressLint("DefaultLocale")
    private void showTimePicker() {
        timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Alarm Time")
                .build();
        timePicker.show(getSupportFragmentManager(), CHANNEL_ID);
        timePicker.addOnPositiveButtonClickListener(view -> {
            if (timePicker.getHour() > 12) {
                binding.selectedTime.setText(format("Alarm set for: %s:%sPM",
                        format("%02d", timePicker.getHour() - 12),
                        format("%02d", timePicker.getMinute())));
            } else {
                binding.selectedTime.setText(format("Alarm set for: %s:%sAM",
                        format("%02d", timePicker.getHour()),
                        format("%02d", timePicker.getMinute())));
            }
            calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR_OF_DAY, timePicker.getHour());
            calendar.add(Calendar.MINUTE, timePicker.getMinute());
            calendar.add(Calendar.SECOND, 0);
            calendar.add(Calendar.MILLISECOND, 0);

            setAlarm();
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final int CHANNEL_PRIO = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, CHANNEL_PRIO);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}