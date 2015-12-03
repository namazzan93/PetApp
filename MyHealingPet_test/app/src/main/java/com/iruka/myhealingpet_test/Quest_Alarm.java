package com.iruka.myhealingpet_test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Random;


/**
 * Created by iRuKa on 2015-11-18.
 */
public class Quest_Alarm extends BroadcastReceiver {

    Random mRand;
    public int intQuestRand;


    public void onReceive(Context context, Intent intent) {
        // TODO: Return the communication channel to the service.
        //Toast.makeText(context, "hi", Toast.LENGTH_LONG).show();

        mRand = new Random();
        intQuestRand = mRand.nextInt(3);

        switch(intQuestRand){
            case 0:
                ((Quest_Main) Quest_Main.mContext).onNotificationGPS();
                ((Quest_Main) Quest_Main.mContext).gpsButton();
                break;
            case 1:
                ((Quest_Main) Quest_Main.mContext).onNotificationShake();
                ((Quest_Main) Quest_Main.mContext).shakeButton();
                break;
            case 2:
                ((Quest_Main) Quest_Main.mContext).onNotificationCall();
                ((Quest_Main) Quest_Main.mContext).callButton();
                break;
            default:
                break;
        }
    }
}