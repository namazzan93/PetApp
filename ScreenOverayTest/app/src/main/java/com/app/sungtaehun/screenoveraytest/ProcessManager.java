package com.app.sungtaehun.screenoveraytest;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by Sung TaeHun on 2015-11-24.
 */
public class ProcessManager {
    private static ProcessManager instance = null;
    private ArrayList<Activity> mActivityArr;

    private ProcessManager(){
        mActivityArr = new ArrayList<Activity>();
    }

    public static ProcessManager getInstance(){
        if(ProcessManager.instance == null){
            synchronized (ProcessManager.class){
                if(ProcessManager.instance == null){
                    ProcessManager.instance = new ProcessManager();
                }
            }
        }
        return ProcessManager.instance;
    }

    public void addActivity(Activity activity){
        if(!isActivity(activity)){
            mActivityArr.add(activity);
        }
    }

    public void deleteActivity(Activity activity){
        if(isActivity(activity)){
            activity.finish();
            mActivityArr.remove(activity);
        }
    }

    public boolean isActivity(Activity activity){
        for(Activity chkActivity:mActivityArr){
            if(chkActivity == activity) return true;
        }
        return false;
    }

    public void allEndActivity(){
        for(Activity activity:mActivityArr) activity.finish();
    }
}
