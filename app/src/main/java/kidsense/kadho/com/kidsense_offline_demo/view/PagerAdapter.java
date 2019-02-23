package kidsense.kadho.com.kidsense_offline_demo.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import kidsense.kadho.com.kidsense_offline_demo.featureControl;
import kidsense.kadho.com.kidsense_offline_demo.security;
import kidsense.kadho.com.kidsense_offline_demo.userControl;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    public PagerAdapter(FragmentManager fm) {
        super(fm);
        this.mNumOfTabs = 3;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new security();
            case 1:
                return new featureControl();
            case 2:
                return new userControl();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
