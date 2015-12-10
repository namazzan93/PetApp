package com.iruka.myhealingpet_test;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Sung TaeHun on 2015-10-26.
 */

/*
    시작로딩화면 다이얼로그
    가장 처음 어플리케이션을 실행시 화면에 나타나는 스플래쉬 화면
    화면에 떠있는 그림을 탭하면 이 화면이 사라지고 메인엑티비티에서 펫을 화면에 띄운다.
 */
public class SplashDialog extends Dialog {

    private ImageView img;
    private TextView txt_msg;
    private View.OnClickListener startListener;

    @Override
        protected void onCreate(Bundle saveIndstanceState){
            super.onCreate(saveIndstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.splashdialog_layout);
        img = (ImageView)findViewById(R.id.imageView);
        txt_msg = (TextView)findViewById(R.id.txt_msg);

        img.setImageResource(R.drawable.frame_normal_bell);
        final AnimationDrawable mAni = (AnimationDrawable)img.getDrawable();
        img.post(new Runnable() {
            @Override
            public void run() {
                mAni.start();
            }
        });


        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500);
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        txt_msg.startAnimation(anim);

        setClickListener(startListener);
    }

    public SplashDialog(Context context, View.OnClickListener singleListener){
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.startListener = singleListener;
    }
    private void setClickListener(View.OnClickListener start){
        img.setOnClickListener(start);
        txt_msg.setOnClickListener(start);
    }
}
