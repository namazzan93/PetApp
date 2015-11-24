package com.iruka.myhealingpet_test;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Sung TaeHun on 2015-10-26.
 */
public class SplashDialog extends Dialog {

    private ImageView img;
    private Button btnstart;
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
        btnstart = (Button)findViewById(R.id.btnstart);
        setClickListener(startListener);
    }

    public SplashDialog(Context context, View.OnClickListener singleListener){
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.startListener = singleListener;
    }
    private void setClickListener(View.OnClickListener start){
        btnstart.setOnClickListener(start);
    }
}
