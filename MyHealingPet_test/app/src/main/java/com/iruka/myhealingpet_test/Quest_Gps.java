package com.iruka.myhealingpet_test;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/*
내 위치 지도 표시 및 목적지 지도표시 위치비교
 */
/**
 * Created by iRuKa on 2015-11-18.
 */
public class Quest_Gps extends ActionBarActivity {

    private RelativeLayout mainLayout;
    private GoogleMap map;

    private SensorManager mSensorManager;

    private LocationManager mLocationManager;
    private Quest_Gps_Alarm mIntentReceiver;

    public static Context mContext;

    private Marker marker;

    ArrayList mPendingIntentList;

    String intentKey = "Proximity";
    static double targetlatitude = 37.449392;
    static double targetlongitude = 126.655757;
    static Double latitude;
    static Double longitude;

    private Manager_DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quest_gps_layout);

        mContext=this;
        // 메인 레이아웃 객체 참조
        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);

        // 지도 객체 참조
        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

        // 센서 관리자 객체 참조
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mPendingIntentList = new ArrayList();

        // 버튼 이벤트 처리
        Button stopBtn = (Button) findViewById(R.id.stopBtn);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unregister();
                Toast.makeText(getApplicationContext(), "근접 리스너 해제", Toast.LENGTH_LONG).show();

                marker.remove();
            }
        });

        Button targetBtn1 = (Button) findViewById(R.id.targetBtn1);
        targetBtn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                targetlatitude = 37.446138;
                targetlongitude = 126.6600345;

                //targetlatitude = 37.450603;
                ///targetlongitude = 126.657326;

                int countTargets = 1;
                register(1001, targetlatitude, targetlongitude, 100, -1);//위도 경도 반경 미터 무제한대기

                TextView textView01 = (TextView) findViewById(R.id.textView01);
                textView01.setText("목표 : " + targetlatitude + ", " + targetlongitude);

                // 수신자 객체 생성하여 등록
                mIntentReceiver = new Quest_Gps_Alarm(intentKey);
                registerReceiver(mIntentReceiver, mIntentReceiver.getFilter());

                Toast.makeText(getApplicationContext(), countTargets + "개 지점에 대한 근접 리스너 등록", Toast.LENGTH_LONG).show();

                marker = map.addMarker(new MarkerOptions().position(new LatLng(targetlatitude, targetlongitude)).alpha(0.8f).title("도착지"));

            }
        });
        /*
        Button targetBtn2 = (Button) findViewById(R.id.targetBtn2);
        targetBtn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                targetlatitude = 37.451396;
                targetlongitude = 126.655848;

                int countTargets = 1;
                register(1001, targetlatitude, targetlongitude, 100, -1);//위도 경도 반경 미터 무제한대기

                TextView textView01 = (TextView) findViewById(R.id.textView01);
                textView01.setText("목표 : " + targetlatitude + ", " + targetlongitude);

                // 수신자 객체 생성하여 등록
                mIntentReceiver = new Quest_Gps_Alarm(intentKey);
                registerReceiver(mIntentReceiver, mIntentReceiver.getFilter());

                Toast.makeText(getApplicationContext(), countTargets + "개 지점에 대한 근접 리스너 등록", Toast.LENGTH_LONG).show();

                marker = map.addMarker(new MarkerOptions().position(new LatLng(targetlatitude, targetlongitude)).alpha(0.8f).title("도착지"));

            }
        });

        Button targetBtn3 = (Button) findViewById(R.id.targetBtn3);
        targetBtn3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                targetlatitude = 37.449235;
                targetlongitude = 126.655855;

                int countTargets = 1;
                register(1001, targetlatitude, targetlongitude, 100, -1);//위도 경도 반경 미터 무제한대기

                TextView textView01 = (TextView) findViewById(R.id.textView01);
                textView01.setText("목표 : " + targetlatitude + ", " + targetlongitude);

                // 수신자 객체 생성하여 등록
                mIntentReceiver = new Quest_Gps_Alarm(intentKey);
                registerReceiver(mIntentReceiver, mIntentReceiver.getFilter());

                Toast.makeText(getApplicationContext(), countTargets + "개 지점에 대한 근접 리스너 등록", Toast.LENGTH_LONG).show();

                marker = map.addMarker(new MarkerOptions().position(new LatLng(targetlatitude, targetlongitude)).alpha(0.8f).title("도착지"));

            }
        });
        */
        // 위치 확인하여 위치 표시 시작
        startLocationService();
    }

    @Override
    public void onResume() {
        super.onResume();

        // 내 위치 자동 표시 enable
        map.setMyLocationEnabled(true);
    }

    @Override
    public void onPause() {
        super.onPause();

        // 내 위치 자동 표시 disable
        map.setMyLocationEnabled(false);
    }

    private void startLocationService() {
        // 위치 관리자 객체 참조
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // 리스너 객체 생성
        GPSListener gpsListener = new GPSListener();
        long minTime = 1000;
        float minDistance = 1;

        // GPS 기반 위치 요청
        manager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime,
                minDistance,
                gpsListener);

        // 네트워크 기반 위치 요청
        manager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                minTime,
                minDistance,
                gpsListener);

        Toast.makeText(getApplicationContext(), "위치 확인 시작함. 로그를 확인하세요.", Toast.LENGTH_SHORT).show();
    }

    private class GPSListener implements LocationListener {
        /**
         * 위치 정보가 확인되었을 때 호출되는 메소드
         */
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            String msg = "Latitude : "+ latitude + "\nLongitude:"+ longitude;
            Log.i("GPSLocationService", msg);

            // 현재 위치의 지도를 보여주기 위해 정의한 메소드 호출
            showCurrentLocation(latitude, longitude);

            TextView textView02 = (TextView) findViewById(R.id.textView02);
            textView02.setText("현재 : " + latitude + ", " + longitude);
        }

        public void onProviderDisabled(String provider) {        }

        public void onProviderEnabled(String provider) {        }

        public void onStatusChanged(String provider, int status, Bundle extras) {        }

    }

    private void showCurrentLocation(Double latitude, Double longitude) {
        // 현재 위치를 이용해 LatLon 객체 생성
        LatLng curPoint = new LatLng(latitude, longitude);

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));

        // 지도 유형 설정. 지형도인 경우에는 GoogleMap.MAP_TYPE_TERRAIN, 위성 지도인 경우에는 GoogleMap.MAP_TYPE_SATELLITE
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // 현재 위치 주위에 아이콘을 표시하기 위해 정의한 메소드

    }

    private final SensorEventListener mListener = new SensorEventListener() {
        private int iOrientation = -1;
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        // 센서의 값을 받을 수 있도록 호출되는 메소드
        public void onSensorChanged(SensorEvent event) {
            if (iOrientation < 0) {
                iOrientation = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
            }

        }

    };

    private void register(int id, double latitude, double longitude, float radius, long expiration) {
        Intent proximityIntent = new Intent(intentKey);
        proximityIntent.putExtra("id", id);
        proximityIntent.putExtra("latitude", latitude);
        proximityIntent.putExtra("longitude", longitude);
        PendingIntent intent = PendingIntent.getBroadcast(this, id, proximityIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        mLocationManager.addProximityAlert(latitude, longitude, radius, expiration, intent);

        mPendingIntentList.add(intent);
    }

    public void onStart() { super.onStart(); }

    public void onStop() {super.onStop(); unregister(); }

    private void unregister() {
        if (mPendingIntentList != null) {
            for (int i = 0; i < mPendingIntentList.size(); i++) {
                PendingIntent curIntent = (PendingIntent) mPendingIntentList.get(i);
                mLocationManager.removeProximityAlert(curIntent);
                mPendingIntentList.remove(i);
            }
        }

        if (mIntentReceiver != null) {
            unregisterReceiver(mIntentReceiver);
            mIntentReceiver = null;
        }
    }

    public void questFinish(){
        db = new Manager_DB(this.getApplication());
        int _level = db.selectValue("level");
        int _gold = db.selectValue("gold");
        db.updateData("level", _level + 10);
        db.updateData("gold", _gold + 200);

        ((Quest_Main) Quest_Main.mContext).offButton();

        AlertDialog.Builder alert = new AlertDialog.Builder(Quest_Gps.this);
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
