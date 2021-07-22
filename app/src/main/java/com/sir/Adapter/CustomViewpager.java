package com.sir.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by Fawad on 3/30/2020.
 */

public class CustomViewpager extends FragmentStatePagerAdapter {

    ArrayList<Fragment> fragments;

    public CustomViewpager(FragmentManager fm, ArrayList<Fragment>fragments) {
        super(fm);
        this.fragments = fragments;
    }


    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
