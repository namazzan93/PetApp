package com.iruka.myhealingpet_test;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.widget.ListView;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;


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
                logVector.put(a, 0);
            }
        }
        cursor.close();

        sorted_map.putAll(logVector);

        //result.append(sorted_map.get("0").name + "/" + sorted_map.get("0").number + "/"
        //        + sorted_map.get("0").count +"\n" );

        ListView listView;
        Quest_Call_IconTextListAdapter adapter;

        listView = (ListView) findViewById(R.id.listView);
        adapter = new Quest_Call_IconTextListAdapter(this);

        Resources res = getResources();

        Iterator<sCallLog> it = sorted_map.keySet().iterator(); // Iterator 로 Key들을 뽑아낸다
        sCallLog obj;
        while (it.hasNext()) {  // Key를 뽑아낸 Iterator 를 돌려가며
            obj = it.next(); // Kef 를 하나씩 뽑아;

                adapter.addItem(new Quest_Call_IconTextItem(res.getDrawable(R.drawable.call), obj.getName(), obj.getNumber(), logVector.get(obj) ));
        }

        listView.setAdapter(adapter);

        /*
        // 새로 정의한 리스너로 객체를 만들어 설정
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Quest_Call_IconTextItem curItem = (Quest_Call_IconTextItem) adapter.getItem(position);
                String[] curData = curItem.getData();

                Toast.makeText(getApplicationContext(), "Selected : " + curData[0], Toast.LENGTH_LONG).show();

            }
        });

        public static Object getKeyFromValue(Map hm, Object value) {
            for (Object o : hm.keySet()) {
                if (hm.get(o).equals(value)) {
                    return o;
                }
            }
            return null;
        }
        */
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