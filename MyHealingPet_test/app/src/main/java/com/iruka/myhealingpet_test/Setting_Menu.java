package com.iruka.myhealingpet_test;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


/**
 * Created by iRuKa on 2015-12-10.
 */
public class Setting_Menu extends AppCompatActivity {

    private Button btn_change_show, btn_hungry_show, btn_success_show, btn_egg, btn_cat, btn_backup;
    private Manager_DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_menu_layout);
        db = new Manager_DB(this);

        setLayout();

        btn_change_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(getApplicationContext(), Pet_Service.class));
                stopService(new Intent(getApplicationContext(), Egg_Service.class));
                startService(new Intent(getApplicationContext(), Egg_Service.class));
                db.updateData("egg", 0);
                db.updateData("level", 100);
            }
        });

        btn_egg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(getApplicationContext(), Pet_Service.class));
                stopService(new Intent(getApplicationContext(), Egg_Service.class));
                startService(new Intent(getApplicationContext(), Egg_Service.class));
                db.updateData("egg", 0);
                db.updateData("level", 0);
            }
        });

        btn_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(getApplicationContext(), Pet_Service.class));
                stopService(new Intent(getApplicationContext(), Egg_Service.class));
                startService(new Intent(getApplicationContext(), Pet_Service.class));
                db.updateData("egg", 1);
                db.updateData("level", 0);
            }
        });

        btn_hungry_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (db.selectValue("egg") == 1) {
                    db.updateData("hungry", 20);
                }
            }
        });
        btn_success_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (db.selectValue("egg") == 1) {
                    db.updateData("mission1", 8);
                }
            }
        });


    }

    public void onClickBackUpButton(View v) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    private void setLayout() {
        btn_change_show = (Button) findViewById(R.id.btn_change_show);
        btn_hungry_show = (Button) findViewById(R.id.btn_hungry_show);
        btn_success_show = (Button) findViewById(R.id.btn_success_show);
        btn_cat = (Button) findViewById(R.id.btn_cat);
        btn_egg = (Button) findViewById(R.id.btn_egg);
        btn_backup = (Button) findViewById(R.id.btn_backup);
    }

}
