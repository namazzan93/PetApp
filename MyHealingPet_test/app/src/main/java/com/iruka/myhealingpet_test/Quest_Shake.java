package com.iruka.myhealingpet_test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.widget.TextView;

import java.util.Random;

/*
쉐이크 클래스
 */
/**
 * Created by iRuKa on 2015-11-18.
 */
public class Quest_Shake extends Activity implements SensorEventListener {

    private long lastTime;
    private float speed;
    private float lastX;
    private float lastY;
    private float lastZ;

    private float x, y, z;
    private static final int SHAKE_THRESHOLD = 3000; // 작을 수록 느린 스피드에서도 감지를 한다.

    private static final int DATA_X = SensorManager.DATA_X;
    private static final int DATA_Y = SensorManager.DATA_Y;
    private static final int DATA_Z = SensorManager.DATA_Z;

    private SensorManager sensorManager;
    private Sensor accelerormeterSensor;

    private TextView counttext=null;
    private int count = 0;

    private PowerManager pm; // 조명 유지
    private PowerManager.WakeLock wl; // 조명 유지

    Random mRand;
    private int intSahkeRand;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerormeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        setContentView(R.layout.quest_shake_layout);

        mRand = new Random();
        intSahkeRand = mRand.nextInt(10) + 10;

        TextView maxText = (TextView)findViewById(R.id.maxTextView);
        maxText.setText(Integer.toString(intSahkeRand));

        // 조명 유지
        pm= (PowerManager)getSystemService(Context.POWER_SERVICE);
        wl= pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag");
    }

    @Override
    public void onStart() {
        super.onStart();

        if(accelerormeterSensor!=null) {
            sensorManager.registerListener(this, accelerormeterSensor, SensorManager.SENSOR_DELAY_GAME);
        }
        wl.acquire(); // 조명 유지
    }

    @Override
    public void onStop() {
        super.onStop();

        if(sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
        wl.release(); // 조명 유지
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // 정확도 설정
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Sensor 정보가 변하면 실행됨.
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            long gabOfTime = (currentTime - lastTime);
            //최근 측정한 시간과 현재 시간을 비교하여 0.1초 이상되었을 때 흔듬을 감지한다.
            if(gabOfTime > 100) {
                lastTime = currentTime;
                x = event.values[SensorManager.DATA_X];
                y = event.values[SensorManager.DATA_Y];
                z = event.values[SensorManager.DATA_Z];

                speed = Math.abs(x + y + z - lastX - lastY - lastZ)/gabOfTime * 10000;

                if(speed>SHAKE_THRESHOLD) {
                    // 이벤트 발생!!
                    counttext = (TextView)findViewById(R.id.mainTextView);
                    counttext.setText(Integer.toString(++count));


                    if(count >= intSahkeRand){
                        //Toast.makeText(this, "10번이상 흔듬!!", Toast.LENGTH_SHORT).show();
                        questFinish();
                    }
                    //else
                        //Toast.makeText(this, "흔들기!", Toast.LENGTH_SHORT).show();
                }
                lastX = event.values[DATA_X];
                lastY = event.values[DATA_Y];
                lastZ = event.values[DATA_Z];
            }
        }
    }


    public void questFinish(){
        AlertDialog.Builder alert = new AlertDialog.Builder(Quest_Shake.this);
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //닫기
                finish();
            }
        });
        alert.setMessage("테스트 메세지");
        alert.show();
    }
}