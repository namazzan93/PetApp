package com.iruka.myhealingpet_test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/*
통화목록 중 최상위 3건을 리스트뷰에 뿌려주는 클래스

 */
/**
 * Created by iRuKa on 2015-12-01.
 */
class sCallLog{
    String name;
    String number;

    @Override
    public String toString() {
        String str = "name: " + name + "/number : " + number;
        return str;
    }

    public String getName() {return name;}

    public String getNumber() {return number;}
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof sCallLog)) {
            return false;
        } else {
            sCallLog that = (sCallLog)obj;
            return this.number.equals(that.number) &&
                    this.name.equals(that.name);
        }
    }
    @Override
    public int hashCode() {
        int hash = this.name.hashCode();
        hash = hash * 31 + this.number.hashCode();
        return hash;
    }
}


public class Quest_Call extends Activity {
    ListView listView;
    private ArrayList<Custom_List_Data> Array_Data;
    private Custom_List_Data data;
    private Quest_Call_Adapter adapter;


    public static Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quest_call_layout);

        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC");

        int nameidx = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
        int numidx = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int typeidx = cursor.getColumnIndex(CallLog.Calls.TYPE);

        HashMap<sCallLog, Integer> logVector = new HashMap<sCallLog, Integer>();
        ValueComparator bvc = new ValueComparator(logVector);
        TreeMap<sCallLog, Integer> sorted_map = new TreeMap<sCallLog, Integer>(bvc);

        StringBuilder result = new StringBuilder();

        while (cursor.moveToNext()) {
            String sname = cursor.getString(nameidx);
            if (sname == null) {
                sname = cursor.getString(numidx);
            }

            String snumber = cursor.getString(numidx);

            sCallLog a = new sCallLog();
            a.name = sname;
            a.number = snumber;

            if (logVector.containsKey(a)) {
                Integer count = logVector.get(a);
                logVector.put(a, count+1);
            } else {
                logVector.put(a, 1);
            }
        }
        cursor.close();

        sorted_map.putAll(logVector);

        Array_Data = new ArrayList<Custom_List_Data>();

        Iterator<sCallLog> it = sorted_map.keySet().iterator(); // Iterator 로 Key들을 뽑아낸다
        sCallLog obj;
        int count = 0;

        while (it.hasNext()) {  // Key를 뽑아낸 Iterator 를 돌려가며
            obj = it.next(); // Kef 를 하나씩 뽑아;
            data = new Custom_List_Data(R.drawable.call, obj.getName(), obj.getNumber(), logVector.get(obj).toString());
            Array_Data.add(data);
            if(count == 3)
                break;
        }

        ListView custom_list = (ListView) findViewById(R.id.listView);
        adapter = new Quest_Call_Adapter(this, android.R.layout.simple_list_item_1, Array_Data);
        custom_list.setAdapter(adapter);

    }

    public void questFinish(){
        AlertDialog.Builder alert = new AlertDialog.Builder(Quest_Call.this);
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //닫기
                finish();
            }
        });
        alert.setMessage("테스트 메세지");
        alert.show();
    }
}




class ValueComparator implements Comparator<sCallLog> {

    Map<sCallLog, Integer> base;
    public ValueComparator(Map<sCallLog, Integer> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.
    public int compare(sCallLog a, sCallLog b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}