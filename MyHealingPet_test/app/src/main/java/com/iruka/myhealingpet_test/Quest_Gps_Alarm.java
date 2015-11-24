package com.iruka.myhealingpet_test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

/**
 * Created by iRuKa on 2015-11-24.
 */
public class Quest_Gps_Alarm extends BroadcastReceiver{

  //  private class TargetIntentReceiver  {

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

    /**
     * 받았을 때 호출되는 메소드
     *
     * @param context
     * @param intent
     */
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            mLastReceivedIntent = intent;

            int id = intent.getIntExtra("id", 0);
            double latitude = intent.getDoubleExtra("latitude", 0.0D);
            double longitude = intent.getDoubleExtra("longitude", 0.0D);

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
