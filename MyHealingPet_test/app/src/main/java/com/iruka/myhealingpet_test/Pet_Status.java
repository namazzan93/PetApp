package com.iruka.myhealingpet_test;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Sung TaeHun on 2015-11-11.
 */
public class Pet_Status extends Activity {

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
    private Manager_DB db;
    private SQLiteDatabase sql;
    public static Pet_Status myStatus;
    private Thread level_thread;
    private Thread heart_thread;
    private Thread hungry_thread;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pet_status_layout);
        Manager_Process.getInstance().addActivity(this);
        setLayout();
        myStatus = Pet_Status.this;

        db = new Manager_DB(this.getApplication());

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
        txtlevel.setText(pblevel.getProgress() + "/100");
        txtheart.setText(pbheart.getProgress()+"/100");
        txthungry.setText(pbhungry.getProgress()+"/100");
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
        Manager_Process.getInstance().deleteActivity(this);
    }
}
