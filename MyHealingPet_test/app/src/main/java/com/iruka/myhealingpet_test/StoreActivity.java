package com.iruka.myhealingpet_test;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

public class StoreActivity extends Activity {

    final String TAG = "MainActivity";

    int mCurrentFragmentIndex;
    public final static int FRAGMENT_NECK = 0;
    public final static int FRAGMENT_TAIL = 1;
    public final static int FRAGMENT_CLOTH = 2;
    public final static int FRAGMENT_TOY = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.store_layout);

        setViewingFragment(new NeckMenuFragment());
    }

    public void setViewingFragment(Fragment fr) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_place, fr);
        fragmentTransaction.commit();
    }

    public void selectFrag(View view) {
        Fragment newFragment = null;

        if (view == findViewById(R.id.neckMenuButton)) {
            newFragment = new NeckMenuFragment();
        } else if (view == findViewById(R.id.tailMenuButton)) {
            newFragment = new TailMenuFragment();
        } else if (view == findViewById(R.id.foodMenuButton)) {
            newFragment = new FoodMenuFragment();
        } else if (view == findViewById(R.id.toyMenuButton)) {
            newFragment = new ToyMenuFragment();
        }

        setViewingFragment(newFragment);
    }
}
