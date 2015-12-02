package com.iruka.myhealingpet_test;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends Activity {

    private SplashDialog mSplashDialog;
    private Toast toast;
    private Manager_DB db;
    private SQLiteDatabase SQL;
    private View.OnClickListener startListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSplashDialog.dismiss();
            startService(new Intent(getApplicationContext(), Pet_Service.class));
            moveTaskToBack(true);
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity_layout);
        Manager_Process.getInstance().addActivity(this);
        mSplashDialog = new SplashDialog(this, startListener);
        mSplashDialog.show();
        db = new Manager_DB(this.getApplication());
        SQL = db.getWritableDatabase();
        //this.getApplication().deleteDatabase("MyDB");
        SQL.execSQL("insert or ignore into Pet values(null, 'level', 0);");
        SQL.execSQL("insert or ignore into Pet values(null, 'heart', 0);");
        SQL.execSQL("insert or ignore into Pet values(null, 'hungry', 100);");
        db.updateData("hungry", 20);
        db.updateData("heart", 30);

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Manager_Process.getInstance().deleteActivity(this);
    }
}