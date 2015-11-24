package com.example.iruka.myhealingpet_test;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends Activity {

    private SplashDialog mSplashDialog;
    private Toast toast;
    private DBManager db;
    private SQLiteDatabase SQL;
    private View.OnClickListener startListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSplashDialog.dismiss();
            startService(new Intent(getApplicationContext(), PetService.class));
            moveTaskToBack(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProcessManager.getInstance().addActivity(this);
        mSplashDialog = new SplashDialog(this, startListener);
        mSplashDialog.show();
        db = new DBManager(this.getApplication());
        SQL = db.getWritableDatabase();
        //this.getApplication().deleteDatabase("MyDB");
        SQL.execSQL("insert or ignore into Pet values(null, 'level', 0);");
        SQL.execSQL("insert or ignore into Pet values(null, 'heart', 0);");
        SQL.execSQL("insert or ignore into Pet values(null, 'hungry', 100);");

    }

    public boolean isServiceRunningCheck() {
        ActivityManager manager = (ActivityManager) this.getSystemService(Activity.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.app.sungtaehun.CHAT".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}