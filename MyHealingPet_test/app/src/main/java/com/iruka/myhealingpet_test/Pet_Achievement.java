package com.iruka.myhealingpet_test;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Sung TaeHun on 2015-12-02.
 */

/*
    업적 클래스
    2개의 리스트뷰를 사용한다. 하나는 업적을 보여준다. 다른 하나는 업적 완료시 이를 보여준다.
 */
public class Pet_Achievement extends Activity {
    private ArrayList<String> mArrayList = new ArrayList<String>();
    private ArrayList<String> mCompleteList = new ArrayList<String>();
    private ListView achievevList, completeList;
    private ArrayAdapter adapter, complete_adapter;
    private Pet_Achievement_Check mCheck;
    private Context mContext;
    private Manager_DB db;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pet_achievement_layout);

        mContext = this;

        db = new Manager_DB(this.getApplication());

        mArrayList.add("손가락 운동 좋아해");
        mArrayList.add("하나로 만족 못하는 나");
        mArrayList.add("너가 먹는 모습만 봐도 배불러");

        mCompleteList.add("");
        mCompleteList.add("");
        mCompleteList.add("");
        for(int i = 0; i < 3; ++i){
            if(db.selectValue("mission" + (i + 1)) >= 11) mCompleteList.set(i, "완료");
        }

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mArrayList);
        complete_adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, mCompleteList);

        achievevList = (ListView)findViewById(R.id.achieve_listView);
        completeList = (ListView)findViewById(R.id.complete_listView);
        achievevList.setAdapter(adapter);
        completeList.setAdapter(complete_adapter);


        achievevList.setOnItemClickListener(listener);
    }

    private View.OnClickListener checkListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            for(int i = 0; i < 3; ++i){
                int temp = db.selectValue("mission" + (i + 1));
                if(temp >= 10){
                    mCompleteList.set(i, "완료");
                    ++temp;
                    db.updateData("mission"+(i + 1), temp);
                    int gold = db.selectValue("gold");
                    db.updateData("gold", gold + 200);
                    Toast.makeText(getApplication(), "업적 완료", Toast.LENGTH_SHORT).show();
                }
            }
            complete_adapter.notifyDataSetChanged();
            mCheck.dismiss();
        }
    };

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                mCheck = new Pet_Achievement_Check(mContext, position, checkListener);
                mCheck.show();

        }
    };

}
