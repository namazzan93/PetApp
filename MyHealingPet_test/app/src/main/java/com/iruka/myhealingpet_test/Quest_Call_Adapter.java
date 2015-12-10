package com.iruka.myhealingpet_test;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/*
리스트뷰를 뿌려주는 클래스
 */
/**
 * Created by iRuKa on 2015-12-09.
 */
public class Quest_Call_Adapter extends ArrayAdapter<Custom_List_Data>
{
    private Context m_Context   = null;

    public Quest_Call_Adapter(Context context, int textViewResourceId, ArrayList<Custom_List_Data> items) {
        super(context, textViewResourceId, items);
        this.m_Context = context;
    }

    @Override
    public View getView(int nPosition, View convertView, ViewGroup parent) {
        PointerView pView = null;

        View view = convertView;

        if(view == null)
        {
            view = LayoutInflater.from(m_Context).inflate(R.layout.quest_call_listitem, null);
            pView = new PointerView(view);
            view.setTag(pView);
        }

        pView = (PointerView)view.getTag();
        final PointerView fpView = pView;

        // 데이터 클래스에서 해당 리스트목록의 데이터를 가져온다.
        Custom_List_Data custom_list_data = getItem(nPosition);

        if(custom_list_data != null)
        {
            // 현재 item의 position에 맞는 이미지와 글을 넣어준다.
            pView.GetIconView().setBackgroundResource(custom_list_data.getImage_ID());
            pView.GetNameView().setText(custom_list_data.getName());
            pView.GetNumberView().setText(custom_list_data.getNumber());
            pView.GetCounnterView().setText(custom_list_data.getCounter());
        }

        Button btn = (Button) view.findViewById(R.id.btn_test);
        btn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 터치 시 전화
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + fpView.GetNumberView().getText()));
                m_Context.startActivity(intent);

            }
        });

        return view;
    }
    
    private class PointerView
    {
        private View        m_BaseView      = null;
        private ImageView   m_ivIcon        = null;
        private TextView    m_tvName        = null;
        private TextView    m_tvNumber      = null;
        private TextView    m_tvCounter     = null;

        public PointerView(View BaseView)
        {
            this.m_BaseView = BaseView;
        }

        public ImageView GetIconView() {
            if(m_ivIcon == null) {
                m_ivIcon = (ImageView)m_BaseView.findViewById(R.id.iconItem);
            }
            return m_ivIcon;
        }

        public TextView GetNameView() {
            if(m_tvName == null) {
                m_tvName = (TextView)m_BaseView.findViewById(R.id.dataItem01);
            }
            return m_tvName;
        }

        public TextView GetNumberView() {
            if(m_tvNumber == null) {
                m_tvNumber = (TextView)m_BaseView.findViewById(R.id.dataItem02);
            }
            return m_tvNumber;
        }

        public TextView GetCounnterView() {
            if(m_tvCounter == null) {
                m_tvCounter = (TextView)m_BaseView.findViewById(R.id.dataItem03);
            }
            return m_tvCounter;
        }
    }
}


class Custom_List_Data
{
    private int     Image_ID;
    private String  Name;
    private String  Number;
    private String  Counter;

    public Custom_List_Data(int _Image_ID, String _Name, String _Number, String _Counter) {
        this.setImage_ID(_Image_ID);
        this.setName(_Name);
        this.setNumber(_Number);
        this.setCounter(_Counter);
    }

    public int getImage_ID()
    {
        return Image_ID;
    }

    public void setImage_ID(int image_ID)
    {
        Image_ID = image_ID;
    }

    public String getName()
    {
        return Name;
    }

    public void setName(String _Name)
    {
        Name = _Name;
    }

    public String getNumber()
    {
        return Number;
    }

    public void setNumber(String _Number)
    {
        Number = _Number;
    }

    public String getCounter()
    {
        return Counter;
    }

    public void setCounter(String _Counter)
    {
        Counter = _Counter;
    }
}