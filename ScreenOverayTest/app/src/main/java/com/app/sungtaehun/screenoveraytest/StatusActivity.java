package com.app.sungtaehun.screenoveraytest;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Sung TaeHun on 2015-11-11.
 */
public class StatusActivity extends Activity {

    private View top;
    private TextView txtlevel;
    private TextView txtheart;
    private TextView txthungry;
    private ProgressBar pblevel;
    private ProgressBar pbheart;
    private ProgressBar pbhungry;
    private int level;
    private int heart;
    private int hungry;
    private DBManager db;
    private SQLiteDatabase sql;
    public static StatusActivity myStatus;
    private Thread level_thread;
    private Thread heart_thread;
    private Thread hungry_thread;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_layout);
        ProcessManager.getInstance().addActivity(this);
        setLayout();
        myStatus = StatusActivity.this;

        db = new DBManager(this.getApplication());

        setDBDate();

        pblevel.setMax(100);
        pbheart.setMax(100);
        pbhungry.setMax(100);
        pblevel.setProgress(level);
        pbheart.setProgress(heart);
        pbhungry.setProgress(hungry);

        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        txtlevel.setText("" + pblevel.getProgress());
        txtheart.setText("" + pbheart.getProgress());
        txthungry.setText("" + pbhungry.getProgress());
    }

    private void setLayout(){
        this.txtlevel = (TextView)findViewById(R.id.txtlevel);
        this.txtheart = (TextView)findViewById(R.id.txtheart);
        this.txthungry = (TextView)findViewById(R.id.txthungry);
        this.pblevel = (ProgressBar)findViewById(R.id.pblevel);
        this.pbheart = (ProgressBar)findViewById(R.id.pbheart);
        this.pbhungry = (ProgressBar)findViewById(R.id.pbhungry);
        this.top = (View)findViewById(R.id.status_top);
    }

    public int getLevel(){return level;}
    public int getHungry(){return hungry;}
    public int getHeart(){return heart;}

    public void setDBDate(){
        level = db.selectValue("level");
        heart = db.selectValue("heart");
        hungry = db.selectValue("hungry");
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        ProcessManager.getInstance().deleteActivity(this);
    }
}
