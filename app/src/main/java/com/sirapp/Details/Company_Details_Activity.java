package com.sirapp.Details;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.crystalviewpager.widgets.CrystalViewPager;
import com.sirapp.Constant.Constant;
import com.sirapp.Adapter.CustomViewPagerAdapter;
import com.sirapp.Base.BaseActivity;
import com.sirapp.R;

public class Company_Details_Activity extends BaseActivity {
    CrystalViewPager viewPager;
    TabLayout tabs;

    //String company_id,company_name,company_image,company_email,company_phone,company_website,company_address;

    public static Context contextOfApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company__details_);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        Constant.toolbar(Company_Details_Activity.this,getString(R.string.header_details));
//        Constant.bottomNav(Company_Details_Activity.this,-1);

        viewPager = findViewById(R.id.viewPagerLayout);
        tabs = findViewById(R.id.tabs);

        setUpViewPager(viewPager);

        tabs.setupWithViewPager(viewPager);

        TextView customers = (TextView) LayoutInflater.from(this).inflate(R.layout.tabview_stock, null);
        TextView addcustomer = (TextView) LayoutInflater.from(this).inflate(R.layout.tabview_stock, null);
        TextView addwarehouse = (TextView) LayoutInflater.from(this).inflate(R.layout.tabview_stock, null);

        customers.setTextColor(getResources().getColor(R.color.lightpurple));
        addcustomer.setTextColor(getResources().getColor(R.color.lightpurple));
        addwarehouse.setTextColor(getResources().getColor(R.color.lightpurple));

        customers.setText(getString(R.string.header_company_detail_company));
        addwarehouse.setText(getString(R.string.header_company_detail_add_warehouse));
        addcustomer.setText(getString(R.string.header_company_detail_warehouse));

//        customers.setTextSize(12);
//        addcustomer.setTextSize(12);
//        addwarehouse.setTextSize(12);

        customers.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Regular.otf"));
        addcustomer.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Regular.otf"));
        addwarehouse.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Regular.otf"));

        tabs.getTabAt(0).setCustomView(customers);
        tabs.getTabAt(1).setCustomView(addwarehouse);
        tabs.getTabAt(2).setCustomView(addcustomer);

        if (getIntent().hasExtra("key")){
            viewPager.setCurrentItem(1);
        }

        if (getIntent().hasExtra("warehouse")){
            viewPager.setCurrentItem(2);
        }

    }
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setUpViewPager(ViewPager pager) {
        CustomViewPagerAdapter adapter = new CustomViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Company_Details_Fragment(), getString(R.string.header_company_detail_company));
        adapter.addFragment(new Add_Warehouse_Fragment(), getString(R.string.header_company_detail_add_warehouse));
        adapter.addFragment(new Warehouse_Listing_Fragment(), getString(R.string.header_company_detail_warehouse));
        pager.setAdapter(adapter);

    }
}
