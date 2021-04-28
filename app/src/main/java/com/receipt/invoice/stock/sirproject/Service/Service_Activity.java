package com.receipt.invoice.stock.sirproject.Service;

import android.graphics.Typeface;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.TextView;

import com.crystalviewpager.widgets.CrystalViewPager;
import com.receipt.invoice.stock.sirproject.Adapter.CustomViewPagerAdapter;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.R;
public class Service_Activity extends AppCompatActivity {
    CrystalViewPager viewPager;
    TabLayout tabs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        Constant.toolbar(Service_Activity.this,"Items");
        Constant.bottomNav(Service_Activity.this,-1);
        viewPager = findViewById(R.id.viewPagerLayout);
        tabs = findViewById(R.id.tabs);


        setUpViewPager(viewPager);

        tabs.setupWithViewPager(viewPager);

        TextView customers = (TextView) LayoutInflater.from(this).inflate(R.layout.tabview, null);
        TextView addcustomer = (TextView) LayoutInflater.from(this).inflate(R.layout.tabview, null);

        customers.setTextColor(getResources().getColor(R.color.lightpurple));
        addcustomer.setTextColor(getResources().getColor(R.color.lightpurple));
        customers.setText("ITEMS");
        addcustomer.setText("ADD ITEMS");

        customers.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Regular.otf"));
        addcustomer.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Regular.otf"));

        tabs.getTabAt(0).setCustomView(customers);
        tabs.getTabAt(1).setCustomView(addcustomer);


    }

    private void setUpViewPager(ViewPager pager) {

        CustomViewPagerAdapter adapter = new CustomViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Service_Listing(), "ITEMS");
        adapter.addFragment(new Add_Services(), "ADD ITEMS");
        pager.setAdapter(adapter);

    }
}

