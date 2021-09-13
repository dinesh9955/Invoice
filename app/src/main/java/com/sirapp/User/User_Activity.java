package com.sirapp.User;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.TextView;

import com.crystalviewpager.widgets.CrystalViewPager;
import com.sirapp.Adapter.CustomViewPagerAdapter;
import com.sirapp.Constant.Constant;
import com.sirapp.Base.BaseActivity;
import com.sirapp.R;


public class User_Activity extends BaseActivity {
    CrystalViewPager viewPager;
    TabLayout tabs;
    public static Context contextOfApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        Constant.toolbar(User_Activity.this, getString(R.string.header_users));
//        Constant.bottomNav(User_Activity.this,-1);

        viewPager = findViewById(R.id.viewPagerLayout);
        tabs = findViewById(R.id.tabs);

        setUpViewPager(viewPager);

        tabs.setupWithViewPager(viewPager);

        TextView customers = (TextView) LayoutInflater.from(this).inflate(R.layout.tabview, null);
        TextView addcustomer = (TextView) LayoutInflater.from(this).inflate(R.layout.tabview, null);

        customers.setTextColor(getResources().getColor(R.color.lightpurple));
        addcustomer.setTextColor(getResources().getColor(R.color.lightpurple));
        customers.setText(getString(R.string.header_users_list));
        addcustomer.setText(getString(R.string.header_users_add));

        customers.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Regular.otf"));
        addcustomer.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Regular.otf"));

        tabs.getTabAt(0).setCustomView(customers);
        tabs.getTabAt(1).setCustomView(addcustomer);
    }
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }
    private void setUpViewPager(ViewPager pager) {

        CustomViewPagerAdapter adapter = new CustomViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new User_Listing(), getString(R.string.header_users_list));
        adapter.addFragment(new Add_User(), getString(R.string.header_users_add));
        pager.setAdapter(adapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}

