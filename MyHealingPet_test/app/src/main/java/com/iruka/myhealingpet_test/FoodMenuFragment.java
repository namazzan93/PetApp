package com.iruka.myhealingpet_test;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class FoodMenuFragment extends Fragment {
    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceStat) {
        View view = inflater.inflate(R.layout.food_fragment, container, false);
        arrayList = new ArrayList<String>();
        arrayList.add("Pet_food");
        arrayList.add("Raw_fish");
        arrayList.add("Special_drug");
        arrayList.add("Two_fish");

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayList);

        listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = (String) adapter.getItem(position);
                Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
