package com.iruka.myhealingpet_test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

/*
목적지 도착이 메시지출력
 */
/**
 * Created by iRuKa on 2015-11-24.
 */
public class Quest_Gps_Alarm extends BroadcastReceiver{
    private String mExpectedAction;
    private Intent mLastReceivedIntent;

    public Quest_Gps_Alarm(String expectedAction) {
        mExpectedAction = expectedAction;
        mLastReceivedIntent = null;
    }

    public IntentFilter getFilter() {
        IntentFilter filter = new IntentFilter(mExpectedAction);
        return filter;
    }

    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            mLastReceivedIntent = intent;

            int id = intent.getIntExtra("id", 0);
            double latitude = intent.getDoubleExtra("latitude", 0.0D);
            double longitude = intent.getDoubleExtra("longitude", 0.0D);
Log.d("!!!!","!!!!!!!!!!!!!!!!!!!");
            Toast.makeText(context, "근접한타겟 : " + id + ", " + latitude + ", " + longitude, Toast.LENGTH_LONG).show();

            ((Quest_Gps) Quest_Gps.mContext).questFinish();
        }
    }

    public Intent getLastReceivedIntent() {
        return mLastReceivedIntent;
    }

    public void clearReceivedIntents() {
        mLastReceivedIntent = null;
    }

}
