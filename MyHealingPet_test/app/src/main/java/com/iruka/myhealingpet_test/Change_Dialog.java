package com.iruka.myhealingpet_test;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Sung TaeHun on 2015-12-10.
 */
public class Change_Dialog extends Dialog {
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

        setContentView(R.layout.change_dialog_layout);
        img = (ImageView)findViewById(R.id.imageView);
        txt_msg = (TextView)findViewById(R.id.txt_msg);

        img.setImageResource(R.drawable.frame_change);
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

    public Change_Dialog(Context context, View.OnClickListener singleListener){
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.startListener = singleListener;
    }
    private void setClickListener(View.OnClickListener start){
        img.setOnClickListener(start);
        txt_msg.setOnClickListener(start);
    }
}