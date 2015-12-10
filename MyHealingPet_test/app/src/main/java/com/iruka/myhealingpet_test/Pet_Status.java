package com.iruka.myhealingpet_test;

import android.app.Activity;
import android.content.Intent;
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
    private Change_Dialog mChange_Dialog;
    private View.OnClickListener startListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mChange_Dialog.dismiss();
            db.updateData("egg", 1);
            db.updateData("level", 0);
            startService(new Intent(getApplicationContext(), Pet_Service.class));
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pet_status_layout);
        Manager_Process.getInstance().addActivity(this);
        setLayout();
        mChange_Dialog = new Change_Dialog(this, startListener);
        db = new Manager_DB(this.getApplication());

        setDBDate();

        if(db.selectValue("egg") == 0 && level == 100){
            stopService(new Intent(getApplicationContext(), Egg_Service.class));
            mChange_Dialog.show();
        }

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
