package com.iruka.myhealingpet_test;


import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class SignUpActivity extends Activity {
    private boolean dupOk = false;
    private boolean passwordOk = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);
    }

    public void onCancelButtonClicked(View v) {
        finish();
    }

    public void onConfirmButtonClicked(View v) {
        if (!dupOk) {
            Toast.makeText(getApplicationContext(), "아이디 중복체크 해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        EditText e1 = (EditText) findViewById(R.id.entryId);
        EditText e2 = (EditText) findViewById(R.id.pwd);
        EditText e3 = (EditText) findViewById(R.id.pwdCheck);

        if(!e2.getText().toString().equals(e3.getText().toString())) {
            Toast.makeText(getApplicationContext(), "비밀번호 입력은 서로 일치해야 합니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayList<String> params = new ArrayList<String>();
        params.add("3");
        params.add(e1.getText().toString());
        params.add(e2.getText().toString());

        ConnectThread thread = new ConnectThread(params);

        try {
            thread.start();
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean result = thread.getResult();
        if (result) {
            Toast.makeText(getApplicationContext(), "회원가입 완료", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_SHORT).show();
        }
    }

    public void onDupCheckButtonClicked(View v) {
        EditText e1 = (EditText) findViewById(R.id.entryId);

        ArrayList<String> params = new ArrayList<String>();
        params.add("2");
        params.add(e1.getText().toString());
        ConnectThread thread = new ConnectThread(params);

        try {
            thread.start();
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean result = thread.getResult();
        if (result) {
            dupOk = true;
            Toast.makeText(getApplicationContext(), "고유한 아이디입니다.", Toast.LENGTH_SHORT).show();
        } else {
            dupOk = false;
            Toast.makeText(getApplicationContext(), "중복된 아이디입니다.", Toast.LENGTH_SHORT).show();
        }

    }

}
