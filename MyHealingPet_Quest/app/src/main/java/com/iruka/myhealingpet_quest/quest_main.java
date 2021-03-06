package com.iruka.myhealingpet_quest;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Random;

public class quest_main extends AppCompatActivity {

    Random mRand;
    public int intQuestRand;

    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_main);
        mContext=this;

        onAlarm();
    }

    public void onAlarm() {
        Toast.makeText(getApplicationContext(), "알람 설정", Toast.LENGTH_SHORT).show();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, 1);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, quest_alarm.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        //cal.getTimeInMillis()//


        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 10, pIntent);
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 1000 * 60 * 60, pIntent);
    }


    public void onButtonAlarmClicked(View view){
       onAlarm();
    }

    public void offButtonAlarmClicked(View view){
        Toast.makeText(getApplicationContext(), "알람 해제", Toast.LENGTH_SHORT).show();
        AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);

        Intent Intent = new Intent(this, quest_alarm.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, Intent, 0);
        alarmManager.cancel(pIntent);

        // 주석을 풀면 먼저 실행되는 알람이 있을 경우, 제거하고
        // 새로 알람을 실행하게 된다. 상황에 따라 유용하게 사용 할 수 있다.
//      alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 3000, pIntent);
    }

    public void onButtonGPSClicked(View view) {
        Intent intent = new Intent(quest_main.this, quest_gps.class);
        startActivity(intent);
    }

    public void onButtonShakeClicked(View view) {
        Intent intent = new Intent(quest_main.this, quest_shake.class);
        startActivity(intent);
    }

    public void onButtonNotifiClicked(View view) {
        mRand = new Random();
        intQuestRand = mRand.nextInt(2);

        if (intQuestRand == 0) {
            onNotificationGPS();
        }
        else {
            onNotificationShake();
        }
    }

    public void onNotificationGPS(){
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, quest_gps.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle("MyHeailngpet 미션도착!")
                .setContentText("같이 산책해주세요")
                .setTicker("MyHeailngpet 미션도착!")
                .setSmallIcon(R.drawable.bank)
                        //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.bank))
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);

        Notification n = builder.build();
        nm.notify(0001, n);

    }

    public void onNotificationShake(){
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, quest_shake.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle("MyHeailngpet 미션도착!")
                .setContentText("흔들어주세요!!")
                .setTicker("MyHeailngpet 미션도착!")
                .setSmallIcon(R.drawable.bank)
                        //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.bank))
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);

        Notification n = builder.build();
        nm.notify(0002, n);

    }
}
