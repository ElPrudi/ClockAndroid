package com.example.clock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.Vibrator;

import com.example.clock.service.AlarmService;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, AlarmService.class);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) context.startForegroundService(intent1);
        else context.startService(intent1);
    }
}
