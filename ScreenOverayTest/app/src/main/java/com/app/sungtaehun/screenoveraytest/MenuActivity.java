package com.app.sungtaehun.screenoveraytest;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Sung TaeHun on 2015-10-30.
 */
public class MenuActivity extends Activity {
    public static boolean MenuActive = false;
    public static MenuActivity myMenu;
    private Button btnExit;
    private Button btn1, btn2, btn3, btn4, btn5;
    private View top;
    public static int level;
    public static int heart;
    public static int hungry;
    //private StatusActivity myStatus;

    protected void onCreate(Bundle saveIndstanceState) {
        super.onCreate(saveIndstanceState);
        setContentView(R.layout.menu_layout);
        ProcessManager.getInstance().addActivity(this);
        myMenu = MenuActivity.this;
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
        this.btnExit = (Button)findViewById(R.id.button6);
        this.btn1 = (Button)findViewById(R.id.button);
        this.btn2 = (Button)findViewById(R.id.button2);
        this.btn3 = (Button)findViewById(R.id.button3);
        this.btn4 = (Button)findViewById(R.id.button4);
        this.btn5 = (Button)findViewById(R.id.button5);
        this.top = (View)findViewById(R.id.dialog_top);
    }

    private void setClickListener(View.OnClickListener menulistener){
        btnExit.setOnClickListener(menulistener);
        btn1.setOnClickListener(menulistener);
        btn2.setOnClickListener(menulistener);
        btn3.setOnClickListener(menulistener);
        btn4.setOnClickListener(menulistener);
        btn5.setOnClickListener(menulistener);
    }

    private View.OnClickListener MenuListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.button6:
                    stopService(new Intent(getApplicationContext(), PetService.class));
                    Toast.makeText(getApplicationContext(), "삭제 버튼입니다.", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case R.id.button:
                    //Toast.makeText(getApplicationContext(), "버튼1 입니다.", Toast.LENGTH_SHORT).show();
                    Intent it = new Intent(getApplication(), StatusActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(it);
                    break;
                case R.id.button2:
                    Toast.makeText(getApplicationContext(), "버튼2 입니다.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.button3:
                    Toast.makeText(getApplicationContext(), "버튼3 입니다.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.button4:
                    Toast.makeText(getApplicationContext(), "버튼4 입니다.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.button5:
                    Toast.makeText(getApplicationContext(), "버튼5 입니다.", Toast.LENGTH_SHORT).show();
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
        ProcessManager.getInstance().deleteActivity(this);
    }

}
