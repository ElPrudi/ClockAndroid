package com.example.clock;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clock.service.AlarmService;

import java.util.Calendar;
import java.util.Random;

public class RingActivity extends AppCompatActivity {
    Button dismiss, snooze;
    ImageView clock;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring);

        dismiss = findViewById(R.id.dismiss);
        snooze = findViewById(R.id.snooze);
        clock = findViewById(R.id.clock);

        dismiss.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AlarmService.class);
            getApplicationContext().stopService(intent);
            finish();
        });

        snooze.setOnClickListener(view -> {
            Context context = getApplicationContext();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.MINUTE, 10);

            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            Alarm alarm = new Alarm(new Random().nextInt(Integer.MAX_VALUE), hour, minute, true, false);
            alarm.schedule(context, getWindow(), calendar);

            Intent intent = new Intent(context, AlarmService.class);
            context.stopService(intent);
            finish();
        });

        animateClock();
    }

    private void animateClock() {
        ObjectAnimator rotateAnimation = ObjectAnimator.ofFloat(clock, "rotation", 0f, 20f, 0f, -20f, 0f);
        rotateAnimation.setRepeatCount(ValueAnimator.INFINITE);
        rotateAnimation.setDuration(800);
        rotateAnimation.start();
    }
}
