package com.iruka.myhealingpet_test;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Sung TaeHun on 2015-12-03.
 */

/*
    업적 관리 다이얼로그
    업적리스트에서 하나의 업적을 클릭시 해당 업적에 대한 상세 설명과 진행 상태를 보여주는 다이얼로그이다.
    업적 완료에 대한 요건이 충족되면 업적 완료 버튼을 활성화시킨다.
 */
public class Pet_Achievement_Check extends Dialog {

    private Button achieve_btn, back_btn;
    private ProgressBar achieve_progressbar;
    private TextView txtAchieveName, txtAchieveComnet, progress_txt;
    private Manager_DB db;
    private int mission_number;
    private View.OnClickListener checkListener;
    @Override
    protected void onCreate(Bundle saveIndstanceState){
        super.onCreate(saveIndstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.pet_achievement_check_layout);

        db = new Manager_DB(this.getContext());

        setLayout();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        achieve_btn.setOnClickListener(checkListener);

        switch(mission_number){
            case 0:
                mission1();
                break;
            case 1:
                mission2();
                break;
            case 2:
                mission3();
                break;
        }
    }

    public Pet_Achievement_Check(Context context, int position, View.OnClickListener checkListener){
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        mission_number = position;
        this.checkListener = checkListener;
    }

    private void setLayout(){
        achieve_btn = (Button)findViewById(R.id.achieve_btn);
        achieve_progressbar = (ProgressBar)findViewById(R.id.achieve_progressBar);
        txtAchieveName = (TextView)findViewById(R.id.txtAchieveName);
        txtAchieveComnet = (TextView)findViewById(R.id.txtAchieveComent);
        progress_txt = (TextView)findViewById(R.id.progress_txt);
        back_btn = (Button)findViewById(R.id.back_btn);
    }

    private void mission1(){
        txtAchieveName.setText("손가락 운동 좋아해");
        txtAchieveComnet.setText("고양이를 10번 터치하세요.");
        achieve_progressbar.setMax(10);

        int mCount = db.selectValue("mission1");
        achieve_progressbar.setProgress(mCount);
        progress_txt.setText( mCount + "/10");

        if(mCount == 10) achieve_btn.setEnabled(true);
        else achieve_btn.setEnabled(false);
    }

    private void mission2(){
        txtAchieveName.setText("하나로 만족 못하는 나");
        txtAchieveComnet.setText("모든 종류의 고양이를 모아보세요");
        achieve_progressbar.setMax(10);

        //int mCount = db.selectValue("mission2");
        achieve_progressbar.setProgress(0);
        progress_txt.setText( 0 + "/10");
        achieve_btn.setEnabled(false);
    }

    private void mission3(){
        txtAchieveName.setText("너가 먹는 모습만 봐도 배불러");
        txtAchieveComnet.setText("배고픈 고양이에게 먹이를 10번 주세요");
        achieve_progressbar.setMax(10);

        int mCount = db.selectValue("mission3");
        achieve_progressbar.setProgress(mCount);
        progress_txt.setText( mCount + "/10");
        if(mCount == 10) achieve_btn.setEnabled(true);
        else achieve_btn.setEnabled(false);
    }
}
