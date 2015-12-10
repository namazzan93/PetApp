package com.iruka.myhealingpet_test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class DataInOutActivity extends Activity {
    private ArrayList<Boolean> userInfo = new ArrayList<Boolean>();
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        userId = intent.getStringExtra("userID");
        System.out.println("넘겨받은 아이디 : " + userId);
        setContentView(R.layout.datainout_layout);
    }

    public String getUserInfo(){
        StringBuilder str = new StringBuilder();
        userInfo = new ArrayList<Boolean>();
        userInfo.add(true); userInfo.add(false); userInfo.add(true);
        for(int i=0; i<userInfo.size(); ++i){
            str.append(userInfo.get(i) ? "1" : "0");
        }
        return str.toString();
    }
    public void setUserInfo(ConnectThread thread){
        String userInfoStr = ((ArrayList<String>)thread.getResultObject()).get(1);
        userInfo.clear();
        for(int i=0; i<userInfoStr.length(); ++i){
            userInfo.add(userInfoStr.charAt(i)=='1' ? true : false);
            System.out.println(userInfo.get(i));
        }
    }

    public void onSaveButtonClicked(View v) {
        ArrayList<String> params = new ArrayList<String>();
        params.add("4");
        params.add(userId);
        params.add(getUserInfo());

        ConnectThread thread = new ConnectThread(params);
        try{
            thread.start();
            thread.join();
        }catch(Exception e){
            e.printStackTrace();
        }

        boolean result = thread.getResult();
        if(result)
            Toast.makeText(getApplicationContext(), "데이터를 저장하였습니다.", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), "데이터 저장에 실패하였습니다.", Toast.LENGTH_SHORT).show();
    }

    public void onLoadButtonClicked(View v) {
        ArrayList<String> params = new ArrayList<String>();
        params.add("5");
        params.add(userId);

        ConnectThread thread = new ConnectThread(params);
        try{
            thread.start();
            thread.join();
        }catch(Exception e){
            e.printStackTrace();
        }

        boolean result = thread.getResult();
        if(result) {
            setUserInfo(thread);
            Toast.makeText(getApplicationContext(), "데이터를 불러왔습니다.", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(getApplicationContext(), "데이터 로드에 실패하였습니다.", Toast.LENGTH_SHORT).show();
    }

    public void onCancelButtonClicked(View v) {
        finish();
    }
}
