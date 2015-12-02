package com.iruka.myhealingpet_test;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class Quest_Call_IconTextListAdapter extends BaseAdapter {

	private Context mContext;

	private List<Quest_Call_IconTextItem> mItems = new ArrayList<Quest_Call_IconTextItem>();

	public Quest_Call_IconTextListAdapter(Context context) {
		mContext = context;
	}

	public void addItem(Quest_Call_IconTextItem it) {
		mItems.add(it);
	}

	public void setListItems(List<Quest_Call_IconTextItem> lit) {
		mItems = lit;
	}

	public int getCount() {
		return mItems.size();
	}

	public Object getItem(int position) {
		return mItems.get(position);
	}

	public boolean areAllItemsSelectable() {
		return false;
	}

	public boolean isSelectable(int position) {
		try {
			return mItems.get(position).isSelectable();
		} catch (IndexOutOfBoundsException ex) {
			return false;
		}
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Quest_Call_IconTextView itemView;
		if (convertView == null) {
			itemView = new Quest_Call_IconTextView(mContext, mItems.get(position));
		} else {
			itemView = (Quest_Call_IconTextView) convertView;
			
			itemView.setIcon(mItems.get(position).getIcon());
			itemView.setText(0, mItems.get(position).getData(0));
			itemView.setText(1, mItems.get(position).getData(1));
			itemView.setText(2, mItems.get(position).getData(2));
		}

		return itemView;
	}

}
