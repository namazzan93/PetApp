package com.iruka.myhealingpet_test;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class Quest_Call_IconTextListAdapter extends BaseAdapter {

	private Quest_Call_IconTextItem mItem;

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

	public View getView( int position, View convertView,  ViewGroup parent) {
		final int pos = position;
		Quest_Call_IconTextView itemView;
		if (convertView == null) {
			itemView = new Quest_Call_IconTextView(mContext, mItems.get(position));
		} else {
			itemView = (Quest_Call_IconTextView) convertView;
			
			itemView.setIcon(mItems.get(position).getIcon());
			itemView.setText(0, mItems.get(position).getData(0));
			itemView.setText(1, mItems.get(position).getData(1));
			itemView.setText(2, mItems.get(position).getData(2));

			Button btn = (Button) convertView.findViewById(R.id.btn_test);
			btn.setFocusable(false);
			btn.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					// 터치 시 해당 아이템 이름 출력
					//Toast.makeText(fparent.getContext(), mItems.get(fposition).getData(0), Toast.LENGTH_SHORT).show();
					//((Quest_Call) Quest_Call.mContext).call(mItems.get(position).getData(1));
					Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mItems.get(pos).getData(1)));
					mContext.startActivity(intent);

				}
			});

		}


		return itemView;
	}

}
