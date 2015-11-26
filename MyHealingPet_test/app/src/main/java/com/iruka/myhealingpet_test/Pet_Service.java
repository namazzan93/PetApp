package com.iruka.myhealingpet_test;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Sung TaeHun on 2015-10-13.
 */
public class Pet_Service extends Service {
        private WindowManager windowManager;
        private WindowManager.LayoutParams params;
        private RelativeLayout chatHeadView;
        private ImageView chatHead;
        private long time_start, time_end;
        private float START_X, START_Y;
        private int PREV_X, PREV_Y;
        private int MAX_X = -1, MAX_Y = -1;
        private int x_init_cord, y_init_cord, x_init_margin, y_init_margin;
        private GestureDetector mDoubleTapGesture;
        private AnimationDrawable mAni;
        private boolean isDoubleClick = false;
        public static boolean running = false;
        private Animation anim;
        private Thread thread;
        private Handler mHandler;
        private Manager_DB db;
        private int cnt_heart;
        private int cnt_level;
        private int level = 0;
        private int hungry = 0;
        private int heart = 0;
        public static Pet_Service myPet;
        private Point szWindow = new Point();
        private boolean isLeft = true;
        private LinearLayout txtView, txt_linearlayout;
        private TextView txt;
        private String sMsg = "심심하다냥";
        private String hungryMsg = "배고프다냥";


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
                setDBDate();
                isDoubleClick = false;
                if(mDoubleTapGesture != null) {
                    mDoubleTapGesture.onTouchEvent(event);    //제스처는 더블탭만 인식, 사용.
                }

                WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) chatHeadView.getLayoutParams();

                int x_cord = (int) event.getRawX();
                int y_cord = (int) event.getRawY();
                int x_cord_Destination, y_cord_Destination;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        time_start = System.currentTimeMillis();
                        /*
                        if (MAX_X == -1) setMaxPosition();
                        START_X = event.getRawX();
                        START_Y = event.getRawY();
                        PREV_X = layoutParams.x;
                        PREV_Y = layoutParams.y; */

                        x_init_cord = x_cord;
                        y_init_cord = y_cord;

                        x_init_margin = layoutParams.x;
                        y_init_margin = layoutParams.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        time_end = System.currentTimeMillis();
                        if(time_end - time_start > 150) chatHead.setImageResource(R.drawable.drag);

                        int x_diff_move = x_cord - x_init_cord;
                        int y_diff_move = y_cord - y_init_cord;

                        x_cord_Destination = x_init_margin + x_diff_move;
                        y_cord_Destination = y_init_margin + y_diff_move;

                        layoutParams.x = x_cord_Destination;
                        layoutParams.y = y_cord_Destination;
                        /*
                        int x = (int) (event.getRawX() - START_X);
                        int y = (int) (event.getRawY() - START_Y);
                        params.x = PREV_X + x;
                        params.y = PREV_Y + y;
                        optimizePosition();  */
                        windowManager.updateViewLayout(chatHeadView, layoutParams);
                        break;

                    case MotionEvent.ACTION_UP:
                        int x_diff = x_cord - x_init_cord;
                        int y_diff = y_cord - y_init_cord;

                        x_cord_Destination = x_init_margin + x_diff;
                        y_cord_Destination = y_init_margin + y_diff;

                        int x_start;
                        x_start = x_cord_Destination;


                        int BarHeight = getStatusBarHeight();
                        if (y_cord_Destination < 0) {
                            y_cord_Destination = 0;
                        } else if (y_cord_Destination + (chatHeadView.getHeight() + BarHeight) > szWindow.y) {
                            y_cord_Destination = szWindow.y - (chatHeadView.getHeight() + BarHeight);
                        }
                        layoutParams.y = y_cord_Destination;

                        resetPosition(x_start);

                        chatHead.setImageResource(R.drawable.normal1);
                        mAni.start();
                        time_end = System.currentTimeMillis();
                        if((time_end - time_start) > 45 && (time_end - time_start) < 300) {
                            cnt_heart++;
                            if(cnt_heart >= 3){
                                cnt_heart = 0;
                                if(heart < 100) {
                                    heart++;
                                    db.updateData("heart", heart);
                                }
                                cnt_level++;
                                if(cnt_level >= 2){
                                    cnt_level = 0;
                                    if(level < 100) {
                                        level++;
                                        db.updateData("level", level);
                                    }
                                }
                            }

                            if(hungry >= 30) {
                                showMsg(sMsg);
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
                                }, 1500);
                            }
                            else {
                                showMsg(hungryMsg);
                                chatHead.setImageResource(R.drawable.frame_angry);
                                mAni = (AnimationDrawable) chatHead.getDrawable();
                                mAni.start();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        chatHead.setImageResource(R.drawable.frame_normal);
                                        mAni = (AnimationDrawable) chatHead.getDrawable();
                                        mAni.start();
                                    }
                                }, 1500);
                            }
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

        @Override
        public void onCreate() {
            super.onCreate();
            myPet = Pet_Service.this;
            db = new Manager_DB(this.getApplication());
            setDBDate();
            cnt_heart = 0; cnt_level = 0;
            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            chatHeadView = (RelativeLayout) inflater.inflate(R.layout.pet_service_layout, null);
            chatHead = (ImageView)chatHeadView.findViewById(R.id.pet_img);
            chatHeadView.setOnTouchListener(mViewTouchListener);
            chatHead.setImageResource(R.drawable.frame_normal);
            mAni = (AnimationDrawable)chatHead.getDrawable();
            mDoubleTapGesture = new GestureDetector(this, mNullListener);    //더블탭 제스쳐 생성
            mDoubleTapGesture.setOnDoubleTapListener(mDoubleTapListener);        //더블 탭 리스너 등록

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                windowManager.getDefaultDisplay().getSize(szWindow);
            } else {
                int w = windowManager.getDefaultDisplay().getWidth();
                int h = windowManager.getDefaultDisplay().getHeight();
                szWindow.set(w, h);
            }
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    PixelFormat.TRANSLUCENT);
            params.gravity = Gravity.TOP | Gravity.LEFT;
            params.x = 0;
            params.y = 1000;
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
                            if(hungry > 0) {
                                hungry--;
                                db.updateData("hungry", hungry);
                                if (hungry <= 30) {
                                    if(heart > 0) {
                                        heart--;
                                        db.updateData("heart", heart);
                                    }
                                }
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            });
            thread.start();

            txtView = (LinearLayout) inflater.inflate(R.layout.pet_text_layout, null);
            txt = (TextView) txtView.findViewById(R.id.txt);
            txt_linearlayout = (LinearLayout) txtView.findViewById(R.id.txt_linearlayout);


            WindowManager.LayoutParams params_txt = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    PixelFormat.TRANSLUCENT);
            params_txt.gravity = Gravity.TOP | Gravity.LEFT;
            txtView.setVisibility(View.GONE);
            windowManager.addView(txtView, params_txt);
        }

        private void setDBDate(){
            level = db.selectValue("level");
            heart = db.selectValue("heart");
            hungry = db.selectValue("hungry");
        }


        @Override
        public void onConfigurationChanged(Configuration newConfig) {
            super.onConfigurationChanged(newConfig);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                windowManager.getDefaultDisplay().getSize(szWindow);
            } else {
                int w = windowManager.getDefaultDisplay().getWidth();
                int h = windowManager.getDefaultDisplay().getHeight();
                szWindow.set(w, h);
            }

            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) chatHeadView.getLayoutParams();

            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (txtView != null) {
                    txtView.setVisibility(View.GONE);
                }

                if (layoutParams.y + (chatHeadView.getHeight() + getStatusBarHeight()) > szWindow.y) {
                    layoutParams.y = szWindow.y - (chatHeadView.getHeight() + getStatusBarHeight());
                    windowManager.updateViewLayout(chatHeadView, layoutParams);
                }

                if (layoutParams.x != 0 && layoutParams.x < szWindow.x) {
                    resetPosition(szWindow.x);
                }

            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                if (txtView != null) {
                    txtView.setVisibility(View.GONE);
                }

                if (layoutParams.x > szWindow.x) {
                    resetPosition(szWindow.x);
                }

            }
        }

    private void resetPosition(int x_cord_now) {
        int w = chatHeadView.getWidth();

        if (x_cord_now == 0 || x_cord_now == szWindow.x - w) {

        } else if (x_cord_now + w / 2 <= szWindow.x / 2) {
            isLeft = true;
            moveToLeft(x_cord_now);

        } else if (x_cord_now + w / 2 > szWindow.x / 2) {
            isLeft = false;
            moveToRight(x_cord_now);

        }

    }

    private void moveToLeft(int x_cord_now) {
        final int x = x_cord_now;
        new CountDownTimer(500, 5) {
            WindowManager.LayoutParams mParams = (WindowManager.LayoutParams) chatHeadView.getLayoutParams();

            public void onTick(long t) {
                long step = (500 - t) / 5;
                mParams.x = (int) (double) bounceValue(step, x);
                windowManager.updateViewLayout(chatHeadView, mParams);
            }

            public void onFinish() {
                mParams.x = 0;
                windowManager.updateViewLayout(chatHeadView, mParams);
            }
        }.start();
    }

    private void moveToRight(int x_cord_now) {
        final int x = x_cord_now;
        new CountDownTimer(500, 5) {
            WindowManager.LayoutParams mParams = (WindowManager.LayoutParams) chatHeadView.getLayoutParams();

            public void onTick(long t) {
                long step = (500 - t) / 5;
                mParams.x = szWindow.x + (int) (double) bounceValue(step, x) - chatHeadView.getWidth();
                windowManager.updateViewLayout(chatHeadView, mParams);
            }

            public void onFinish() {
                mParams.x = szWindow.x - chatHeadView.getWidth();
                windowManager.updateViewLayout(chatHeadView, mParams);
            }
        }.start();
    }

    private double bounceValue(long step, long scale) {
        double value = scale * java.lang.Math.exp(-0.055 * step) * java.lang.Math.cos(0.08 * step);
        return value;
    }
    private int getStatusBarHeight() {
        int statusBarHeight = (int) Math.ceil(25 * getApplicationContext().getResources().getDisplayMetrics().density);
        return statusBarHeight;
    }

    private void showMsg(String sMsg) {
        if (txtView != null && chatHeadView != null) {
            txt.setText(sMsg);
            myHandler.removeCallbacks(myRunnable_text);

            WindowManager.LayoutParams param_chathead = (WindowManager.LayoutParams) chatHeadView.getLayoutParams();
            WindowManager.LayoutParams param_txt = (WindowManager.LayoutParams) txtView.getLayoutParams();

            txt_linearlayout.getLayoutParams().height = chatHeadView.getHeight();
            txt_linearlayout.getLayoutParams().width = szWindow.x / 2;

            if (isLeft) {
                param_txt.x = param_chathead.x + chatHead.getWidth() - 100;
                param_txt.y = param_chathead.y - 200;

                txt_linearlayout.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            } else {
                param_txt.x = param_chathead.x - szWindow.x / 2 + 100;
                param_txt.y = param_chathead.y - 200;

                txt_linearlayout.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            }

            txtView.setVisibility(View.VISIBLE);
            windowManager.updateViewLayout(txtView, param_txt);

            myHandler.postDelayed(myRunnable_text, 1000);
        }

    }

    Handler myHandler = new Handler();
    Runnable myRunnable_text = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (txtView != null) {
                txtView.setVisibility(View.GONE);
            }
        }
    };

        @Override
        public void onDestroy() {
            super.onDestroy();
            if(chatHeadView != null){
                windowManager.removeView(chatHeadView);
            }
            Manager_Process.getInstance().allEndActivity();
        }
}