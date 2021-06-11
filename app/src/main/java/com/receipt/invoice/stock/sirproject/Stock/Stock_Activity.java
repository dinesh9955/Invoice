package com.receipt.invoice.stock.sirproject.Stock;

import android.graphics.Typeface;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.TextView;

import com.crystalviewpager.widgets.CrystalViewPager;
import com.receipt.invoice.stock.sirproject.Adapter.CustomViewPagerAdapter;
import com.receipt.invoice.stock.sirproject.Base.BaseActivity;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.R;

public class Stock_Activity extends BaseActivity {
    CrystalViewPager viewPager;
    TabLayout tabs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        Constant.toolbar(Stock_Activity.this,"Stocks");
        Constant.bottomNav(Stock_Activity.this,-1);

        viewPager = findViewById(R.id.viewPagerLayout);
        tabs = findViewById(R.id.tabs);

        setUpViewPager(viewPager);

        tabs.setupWithViewPager(viewPager);

        TextView customers = (TextView) LayoutInflater.from(this).inflate(R.layout.tabview, null);
        TextView addcustomer = (TextView) LayoutInflater.from(this).inflate(R.layout.tabview, null);
        TextView products = (TextView) LayoutInflater.from(this).inflate(R.layout.tabview, null);
        TextView wastage = (TextView) LayoutInflater.from(this).inflate(R.layout.tabview, null);

        customers.setTextSize(10);
        addcustomer.setTextSize(10);
        products.setTextSize(10);
        wastage.setTextSize(10);

        customers.setTextColor(getResources().getColor(R.color.lightpurple));
        addcustomer.setTextColor(getResources().getColor(R.color.lightpurple));
        products.setTextColor(getResources().getColor(R.color.lightpurple));
        wastage.setTextColor(getResources().getColor(R.color.lightpurple));


        customers.setText("UPDATE STOCK");
        addcustomer.setText("STOCK MOVEMENT");
        products.setText("PRODUCTS");
        wastage.setText("WASTAGE/DAMAGE");

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
        adapter.addFragment(new Stock_Product_List(), "PRODUCTS");
        adapter.addFragment(new Update_Stock(), "UPDATE STOCK");
        adapter.addFragment(new Stock_Movement(), "STOCK MOVEMENT");
        adapter.addFragment(new Wastage_Damage(), "WASTAGE & DAMAGE");
        pager.setAdapter(adapter);

    }
}
