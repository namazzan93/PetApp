package com.iruka.myhealingpet_test;



import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;




/**
 * Created by iRuKa on 2015-12-10.
 */
public class Setting_Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_menu_layout);
    }

    public void onButtonAlarmClicked(View view){
        if(Quest_Main.QuestCall == false) {
            ((Quest_Main) Quest_Main.mContext).onAlarm();
        }
    }

    public void offButtonAlarmClicked(View view){
        if(Quest_Main.QuestCall == true) {
            ((Quest_Main) Quest_Main.mContext).offAlarm();
        }
    }

}
