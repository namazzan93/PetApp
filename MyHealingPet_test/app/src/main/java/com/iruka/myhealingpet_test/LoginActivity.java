package com.iruka.myhealingpet_test;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
    }

    public void onLoginOkButtonClicked(View v) {
        EditText e1 = (EditText) findViewById(R.id.entryId);
        EditText e2 = (EditText) findViewById(R.id.pwd);
        ArrayList<String> params = new ArrayList<String>();
        params.add("1");
        params.add(e1.getText().toString());
        params.add(e2.getText().toString());

        ConnectThread thread = new ConnectThread(params);

        try {
            thread.start();
            thread.join();
        }catch(Exception e){
            e.printStackTrace();
        }

        boolean result = thread.getResult();

        if(result) {
            Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), DataInOutActivity.class);
            intent.putExtra("userID", e1.getText().toString());
            startActivityForResult(intent, 1);
        }else{
            Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
        }
//

    }

    public void onSignUpButtonClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
    }

    public void onCancelButtonClicked(View v) {
        finish();
    }
}
