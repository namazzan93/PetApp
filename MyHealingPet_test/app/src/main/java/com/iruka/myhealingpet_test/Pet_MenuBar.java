package com.iruka.myhealingpet_test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Sung TaeHun on 2015-10-30.
 */

/*
    메뉴 클래스
    펫을 더블탭하여 나타나는 메뉴 클래스
    상태, 미션, 상점, 업적, 설정, 종료 의 총 6가지 버튼을 가지고 있다.
    각 버튼에 따라 그에 해당하는 액티비티를 띄운다.
    종료 버튼은 어플리케이션을 종료시킨다.
 */
public class Pet_MenuBar extends Activity {
    public static boolean MenuActive = false;
    public static Pet_MenuBar myMenu;
    private Button btnexit, btnstore, btnquest, btnachieve, btnoption, btnstatus;
    private View top;
    private Manager_DB db;


    protected void onCreate(Bundle saveIndstanceState) {
        super.onCreate(saveIndstanceState);
        setContentView(R.layout.pet_menubar_layout);
        Manager_Process.getInstance().addActivity(this);
        myMenu = Pet_MenuBar.this;
        db = new Manager_DB(this.getApplication());
        setLayout();
        setClickListener(MenuListener);
        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setLayout(){
        this.btnexit = (Button)findViewById(R.id.btnexit);
        this.btnquest = (Button)findViewById(R.id.btnquest);
        this.btnstatus = (Button)findViewById(R.id.btnstauts);
        this.btnstore = (Button)findViewById(R.id.btnstore);
        this.btnoption = (Button)findViewById(R.id.btnoption);
        this.btnachieve = (Button)findViewById(R.id.btnachieve);
        this.top = (View)findViewById(R.id.dialog_top);
    }

    private void setClickListener(View.OnClickListener menulistener){
        btnexit.setOnClickListener(menulistener);
        btnstore.setOnClickListener(menulistener);
        btnstatus.setOnClickListener(menulistener);
        btnquest.setOnClickListener(menulistener);
        btnoption.setOnClickListener(menulistener);
        btnachieve.setOnClickListener(menulistener);
    }

    private View.OnClickListener MenuListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int temp = db.selectValue("egg");
            switch(view.getId()){
                case R.id.btnstauts:
                    Intent it = new Intent(getApplication(), Pet_Status.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(it);
                    break;
                case R.id.btnquest:
                     if(temp != 0){
                        Intent quest = new Intent(getApplication(), Quest_Main.class);
                        startActivity(quest);
                    }
                    break;
                case R.id.btnstore:
                    break;
                case R.id.btnachieve:
                    if(temp != 0) {
                        Intent achieve = new Intent(getApplication(), Pet_Achievement.class);
                        startActivity(achieve);
                    }
                    break;
                case R.id.btnoption:
                    Intent setting = new Intent(getApplication(), Setting_Menu.class);
                    startActivity(setting);
                    break;
                case R.id.btnexit:
                    if(Quest_Main.QuestCall == true) {
                        ((Quest_Main) Quest_Main.mContext).offAlarm();
                    }
                    //if (((Quest_Main) Quest_Main.mContext).isQuestCall() == true )

                    if(temp != 0) {
                        stopService(new Intent(getApplicationContext(), Pet_Service.class));
                        finish();
                    }
                    else{
                        stopService(new Intent(getApplicationContext(), Egg_Service.class));
                        finish();
                    }
                    break;
            }

    }};
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MenuActive = true;
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MenuActive = false;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        MenuActive = false;
        Manager_Process.getInstance().deleteActivity(this);
    }

}
