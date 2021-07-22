package com.sir.Tax;

import android.graphics.Typeface;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.TextView;

import com.crystalviewpager.widgets.CrystalViewPager;
import com.sir.Adapter.CustomViewPagerAdapter;
import com.sir.Base.BaseActivity;
import com.sir.Constant.Constant;
import com.sir.R;


public class Tax_Activity extends BaseActivity {
    CrystalViewPager viewPager;
    TabLayout tabs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tax_);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        Constant.toolbar(Tax_Activity.this, getString(R.string.header_tax));
        Constant.bottomNav(Tax_Activity.this,-1);
        viewPager = findViewById(R.id.viewPagerLayout);
        tabs = findViewById(R.id.tabs);

        setUpViewPager(viewPager);

        tabs.setupWithViewPager(viewPager);

        TextView customers = (TextView) LayoutInflater.from(this).inflate(R.layout.tabview, null);
        TextView addcustomer = (TextView) LayoutInflater.from(this).inflate(R.layout.tabview, null);

        customers.setTextColor(getResources().getColor(R.color.lightpurple));
        addcustomer.setTextColor(getResources().getColor(R.color.lightpurple));
        customers.setText(getString(R.string.header_tax_list));
        addcustomer.setText(getString(R.string.header_tax_add));

        customers.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Regular.otf"));
        addcustomer.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Regular.otf"));

        tabs.getTabAt(0).setCustomView(customers);
        tabs.getTabAt(1).setCustomView(addcustomer);

    }
    private void setUpViewPager(ViewPager pager) {

        CustomViewPagerAdapter adapter = new CustomViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tax_Listing(), getString(R.string.header_tax_list));
        adapter.addFragment(new Add_Tax(), getString(R.string.header_tax_add));
        pager.setAdapter(adapter);

    }
}
