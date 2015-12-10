package com.iruka.myhealingpet_test;


import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

// 메인엑티비티, 처음 어플리케이션 실행 시 수행
// 로딩화면을 띄운다.
// 로딩화면이 사라지면 DB 데이터를 삽입하고 펫을 화면에 띄운다.
// 그리고 메인엑티비티를 강제 종료 시킨다.
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
        SQL.execSQL("insert or ignore into Pet values(null, 'mission1', 0);");
        SQL.execSQL("insert or ignore into Pet values(null, 'mission2', 0);");
        SQL.execSQL("insert or ignore into Pet values(null, 'mission3', 0);");
        db.updateData("hungry", 70);
        db.updateData("heart", 30);

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub

        super.onDestroy();
        Manager_Process.getInstance().deleteActivity(this);
    }
}