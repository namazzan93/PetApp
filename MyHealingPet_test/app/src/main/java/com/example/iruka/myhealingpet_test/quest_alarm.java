package com.example.iruka.myhealingpet_test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Random;


/**
 * Created by iRuKa on 2015-11-18.
 */
public class quest_alarm extends BroadcastReceiver {

    Random mRand;
    public int intQuestRand;

    public void onReceive (Context context, Intent intent) {
        // TODO: Return the communication channel to the service.
        //Toast.makeText(context, "hi", Toast.LENGTH_LONG).show();

        mRand = new Random();
        intQuestRand = mRand.nextInt(2);

        if (intQuestRand == 0) {
            ((quest_main) quest_main.mContext).onNotificationGPS();
        } else {
            ((quest_main) quest_main.mContext).onNotificationShake();
        }


    }
}
