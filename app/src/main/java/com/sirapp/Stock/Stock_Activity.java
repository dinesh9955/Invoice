package com.sirapp.Stock;

import android.graphics.Typeface;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.TextView;

import com.crystalviewpager.widgets.CrystalViewPager;
import com.sirapp.Adapter.CustomViewPagerAdapter;
import com.sirapp.Constant.Constant;
import com.sirapp.Base.BaseActivity;
import com.sirapp.R;

public class Stock_Activity extends BaseActivity {
    CrystalViewPager viewPager;
    TabLayout tabs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        Constant.toolbar(Stock_Activity.this, getString(R.string.header_stock));
//        Constant.bottomNav(Stock_Activity.this,-1);

        viewPager = findViewById(R.id.viewPagerLayout);
        tabs = findViewById(R.id.tabs);

        setUpViewPager(viewPager);

        tabs.setupWithViewPager(viewPager);

        TextView customers = (TextView) LayoutInflater.from(this).inflate(R.layout.tabview_stock, null);
        TextView addcustomer = (TextView) LayoutInflater.from(this).inflate(R.layout.tabview_stock, null);
        TextView products = (TextView) LayoutInflater.from(this).inflate(R.layout.tabview_stock, null);
        TextView wastage = (TextView) LayoutInflater.from(this).inflate(R.layout.tabview_stock, null);

//        customers.setTextSize(R.dimen._8sdp);
//        addcustomer.setTextSize(R.dimen._8sdp);
//        products.setTextSize(R.dimen._8sdp);
//        wastage.setTextSize(R.dimen._8sdp);

        customers.setTextColor(getResources().getColor(R.color.lightpurple));
        addcustomer.setTextColor(getResources().getColor(R.color.lightpurple));
        products.setTextColor(getResources().getColor(R.color.lightpurple));
        wastage.setTextColor(getResources().getColor(R.color.lightpurple));


        products.setText(getString(R.string.header_stock_product));
        customers.setText(getString(R.string.header_stock_update));
        addcustomer.setText(getString(R.string.header_stock_movement));
        wastage.setText(getString(R.string.header_stock_waste));

        customers.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Regular.otf"));
        addcustomer.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Regular.otf"));
        products.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Regular.otf"));
        wastage.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Regular.otf"));

        tabs.getTabAt(0).setCustomView(products);
        tabs.getTabAt(1).setCustomView(customers);
        tabs.getTabAt(2).setCustomView(addcustomer);
        tabs.getTabAt(3).setCustomView(wastage);
    }
    private void setUpViewPager(ViewPager pager) {

        CustomViewPagerAdapter adapter = new CustomViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Stock_Product_List(), getString(R.string.header_stock_product));
        adapter.addFragment(new Update_Stock(), getString(R.string.header_stock_update));
        adapter.addFragment(new Stock_Movement(), getString(R.string.header_stock_movement));
        adapter.addFragment(new Wastage_Damage(), getString(R.string.header_stock_waste));
        pager.setAdapter(adapter);

    }
}
