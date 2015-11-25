package com.iruka.myhealingpet_test;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by Sung TaeHun on 2015-10-13.
 */
public class Pet_Service extends Service {
    private WindowManager windowManager;
    private RelativeLayout chatHeadView;
    private ImageView chatHead;
    private WindowManager.LayoutParams params;
    private long time_start, time_end;
    private float START_X, START_Y;
    private int PREV_X, PREV_Y;
    private int MAX_X = -1, MAX_Y = -1;
    private int CNT = 0;
    private GestureDetector mDoubleTapGesture;
    private AnimationDrawable mAni;
    private boolean isDoubleClick = false;
    public static boolean running = false;
    private Animation anim;
    private Thread thread;
    private Handler mHandler;
    private Manager_DB db;
    private int cnt_heart = 0;
    private int cnt_level = 0;
    private int level = 0;
    private int hungry = 0;
    private int heart = 0;
    public static Pet_Service myPet;


    //아무것도 안하는 제스쳐 리스너
    private GestureDetector.OnGestureListener mNullListener = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onSingleTapUp(MotionEvent e) { return false; }
        @Override
        public void onShowPress(MotionEvent e) {}
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) { return false; }
        @Override
        public void onLongPress(MotionEvent e) {}
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) { return false; }
        @Override
        public boolean onDown(MotionEvent e) { return false; }
    };

    //더블탭 리스너
    private GestureDetector.OnDoubleTapListener mDoubleTapListener = new GestureDetector.OnDoubleTapListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) { return false; }
        @Override
        public boolean onDoubleTapEvent(MotionEvent e) { return false; }
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            isDoubleClick = true;
            if(Pet_MenuBar.MenuActive){
                Pet_MenuBar.myMenu.finish();
            }
            else {
                Intent it = new Intent(getApplication(), Pet_MenuBar.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(it);
            }
            return true;
        }
    };

    private View.OnTouchListener mViewTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            isDoubleClick = false;
            if(mDoubleTapGesture != null) {
                mDoubleTapGesture.onTouchEvent(event);    //제스처는 더블탭만 인식, 사용.
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    time_start = System.currentTimeMillis();
                    if (MAX_X == -1) setMaxPosition();
                    START_X = event.getRawX();
                    START_Y = event.getRawY();
                    PREV_X = params.x;
                    PREV_Y = params.y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    chatHead.setImageResource(R.drawable.drag);
                    int x = (int) (event.getRawX() - START_X);
                    int y = (int) (event.getRawY() - START_Y);
                    params.x = PREV_X + x;
                    params.y = PREV_Y + y;
                    optimizePosition();
                    windowManager.updateViewLayout(chatHeadView, params);
                    break;
                case MotionEvent.ACTION_UP:
                    chatHead.setImageResource(R.drawable.normal1);
                    mAni.start();
                    time_end = System.currentTimeMillis();
                    if((!isDoubleClick) && (time_end - time_start) < 300) {
                        cnt_heart++;
                        if(cnt_heart >= 3){
                            cnt_heart = 0;
                            heart++;
                            db.updateData("heart", heart);
                            cnt_level++;
                            if(cnt_level >= 2){
                                cnt_level = 0;
                                level++;
                                db.updateData("level", level);
                            }
                        }
                        /*
                        chatHead.setImageResource(R.drawable.frame_eye);
                        mAni = (AnimationDrawable) chatHead.getDrawable();
                        mAni.start();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                chatHead.setImageResource(R.drawable.frame_normal);
                                mAni = (AnimationDrawable) chatHead.getDrawable();
                                mAni.start();
                            }
                        },1500);
                        */
                    }
                    break;
            }
            return true;
        }
    };


   @Override
   public IBinder onBind(Intent intent) {
        // Not used
        return null;
    }

    private void playAnimation(){
        anim = null;
        anim = AnimationUtils.makeInAnimation(this, true);
        anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_PARENT, 1,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_PARENT, 1);
        anim.setDuration(2000);
        anim.setRepeatCount(3);
        anim.setFillEnabled(true);
        anim.setFillAfter(true);
        anim.setDetachWallpaper(true);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        myPet = Pet_Service.this;
        db = new Manager_DB(this.getApplication());
        setDBDate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        chatHeadView = (RelativeLayout) inflater.inflate(R.layout.pet_service_layout, null);
        chatHead = (ImageView)chatHeadView.findViewById(R.id.pet_img);
        chatHeadView.setOnTouchListener(mViewTouchListener);
        chatHead.setImageResource(R.drawable.frame_normal);
        mAni = (AnimationDrawable)chatHead.getDrawable();
        mDoubleTapGesture = new GestureDetector(this, mNullListener);    //더블탭 제스쳐 생성
        mDoubleTapGesture.setOnDoubleTapListener(mDoubleTapListener);        //더블 탭 리스너 등록

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        chatHead.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int image_width = chatHead.getMeasuredWidth();//((BitmapDrawable)chatHead.getDrawable()).getBounds().width();
        int image_height = chatHead.getMeasuredHeight();//((BitmapDrawable)chatHead.getDrawable()).getBounds().height();
        params.x = width/2 - image_width/2;
        params.y = height/2 - image_height/2;
        windowManager.addView(chatHeadView, params);
       // playAnimation();
       // chatHeadView.startAnimation(anim);

        chatHead.post(new Runnable() {
            @Override
            public void run() {
                mAni.start();
            }
        });
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try{
                        Thread.sleep(10000);
                        hungry--;
                        db.updateData("hungry", hungry);
                        if(hungry <= 30){
                            heart--;
                            db.updateData("heart", heart);
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    private void setDBDate(){
        level = db.selectValue("level");
        heart = db.selectValue("heart");
        hungry = db.selectValue("hungry");
    }

    private void setMaxPosition(){
        DisplayMetrics matrix = new DisplayMetrics();
        windowManager.getDefaultDisplay() .getMetrics(matrix);
        MAX_X = matrix.widthPixels - chatHeadView.getWidth();
        MAX_Y = matrix.heightPixels - chatHeadView.getHeight();
    }

    private void optimizePosition(){
        if(params.x > MAX_X) params.x = MAX_X;
        if(params.y > MAX_Y) params.y = MAX_Y;
        if(params.x < 0) params.x = 0;
        if(params.x < 0 ) params.y = 0;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        setMaxPosition();
        optimizePosition();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(chatHeadView != null){
            windowManager.removeView(chatHeadView);
        }
        Manager_Process.getInstance().allEndActivity();
    }
}