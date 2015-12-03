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
            switch(view.getId()){
                case R.id.btnstauts:
                    Intent it = new Intent(getApplication(), Pet_Status.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(it);
                    break;
                case R.id.btnquest:
                    Intent quest = new Intent(getApplication(), Quest_Main.class);
                    startActivity(quest);
                    break;
                case R.id.btnstore:
                    break;
                case R.id.btnachieve:
                    Intent achieve = new Intent(getApplication(), Pet_Achievement.class);
                    startActivity(achieve);
                    break;
                case R.id.btnoption:
                    break;
                case R.id.btnexit:
                    //((Quest_Main) Quest_Main.mContext).offAlarm();
                    stopService(new Intent(getApplicationContext(), Pet_Service.class));
                    Toast.makeText(getApplicationContext(), "삭제 버튼입니다.", Toast.LENGTH_SHORT).show();
                    finish();
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
