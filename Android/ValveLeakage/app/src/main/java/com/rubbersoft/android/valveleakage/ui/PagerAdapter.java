package com.rubbersoft.android.valveleakage.ui;

import android.app.ListFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.rubbersoft.android.valveleakage.utils.ConfigConstants;

/**
 * Created by Faiz on 31/12/2015.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs = 4;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return ItemFragment.newInstance(ConfigConstants.TABLE_NODE1);
            case 1:
                return ItemFragment.newInstance(ConfigConstants.TABLE_NODE2);
            case 2:
                return ItemFragment.newInstance(ConfigConstants.TABLE_NODE3);
            case 3:
                return ItemFragment.newInstance(ConfigConstants.TABLE_NODE4);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
