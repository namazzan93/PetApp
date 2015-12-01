package com.iruka.myhealingpet_test;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.widget.TextView;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


/**
 * Created by iRuKa on 2015-12-01.
 */

class CallLogs{
    String name;
    String number;
    String type;
    int count = 0;
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

        HashMap logVector = new HashMap();
        ValueComparator bvc = new ValueComparator(logVector) {
            @Override
            public int compare(String s, String t1) {
                return 0;
            }
        };
        TreeMap <String, CallLogs>sorted_map = new TreeMap<>(bvc);
        int hashKey = 0;

        StringBuilder result = new StringBuilder();

        while(cursor.moveToNext()) {


            String sname = cursor.getString(nameidx);
            if (sname == null) {
                sname = cursor.getString(numidx);
            }

            String snumber = cursor.getString(numidx);

            int type = cursor.getInt(typeidx);
            String stype;
            switch (type) {
                case CallLog.Calls.INCOMING_TYPE:
                    stype = "수신";
                    break;
                case CallLog.Calls.OUTGOING_TYPE:
                    stype = "발신";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    stype = "부재중";
                    break;
                default:
                    stype = "기타";
                    break;
            }
            CallLogs a = new CallLogs();
            a.name = sname;
            a.number = snumber;
            a.type = stype;

            if (logVector.containsValue(a.number)) {
                logVector.put(getKeyFromValue(logVector, snumber), a.count++);
            } else {
                hashKey++;
                String shashKey = Integer.toString(hashKey);
                logVector.put(shashKey, a);
            }
        }
        cursor.close();

        sorted_map.putAll(logVector);

        result.append(sorted_map.get("0").name + "/" + sorted_map.get("0").number + "/"
                + sorted_map.get("0").count +"\n" );

        TextView txtResult = (TextView)findViewById(R.id.result);
        txtResult.setText(result);
    }

    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }
}


abstract class ValueComparator implements Comparator<String> {

    Map<String, CallLogs> base;
    public ValueComparator(Map<String, CallLogs> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.
    public int compare(CallLogs a, CallLogs b) {
        if (base.get(a).count <= base.get(b).count) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}