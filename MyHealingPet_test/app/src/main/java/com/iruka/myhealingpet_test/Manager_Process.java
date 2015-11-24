package com.iruka.myhealingpet_test;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by Sung TaeHun on 2015-11-24.
 */
public class Manager_Process {
    private static Manager_Process instance = null;
    private ArrayList<Activity> mActivityArr;

    private Manager_Process(){
        mActivityArr = new ArrayList<Activity>();
    }

    public static Manager_Process getInstance(){
        if(Manager_Process.instance == null){
            synchronized (Manager_Process.class){
                if(Manager_Process.instance == null){
                    Manager_Process.instance = new Manager_Process();
                }
            }
        }
        return Manager_Process.instance;
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
