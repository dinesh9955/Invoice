package com.sirapp.Invoice;

import android.app.Activity;
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

import com.sirapp.Constant.Constant;
import com.sirapp.Adapter.CustomViewPagerAdapter;
import com.sirapp.Base.BaseActivity;
import com.sirapp.R;
import com.sirapp.Utils.LockableViewPager;

public class InvoiceActivity extends BaseActivity {

    public static Activity activity;


    private static final String TAG = "Create_Invoice_Activity";
    LockableViewPager viewPager;
    TabLayout tabs;
    public static Context contextOfApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__invoice_);
        //Constant.bottomNav(Create_Invoice_Activity.this,1);

        overridePendingTransition(R.anim.flip_out,R.anim.flip_in);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Constant.toolbar(InvoiceActivity.this,getString(R.string.header_invoices));

        activity = this;
        //pref.setCustomerName("");


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        viewPager = findViewById(R.id.viewPagerLayout);
        tabs = findViewById(R.id.tabs);

        setUpViewPager(viewPager);

        tabs.setupWithViewPager(viewPager);

        TextView customers = (TextView) LayoutInflater.from(this).inflate(R.layout.tabview, null);
        TextView addcustomer = (TextView) LayoutInflater.from(this).inflate(R.layout.tabview, null);

        customers.setTextColor(getResources().getColor(R.color.lightpurple));
        addcustomer.setTextColor(getResources().getColor(R.color.lightpurple));
        addcustomer.setText(getString(R.string.header_invoices_list));
        customers.setText(getString(R.string.header_invoices_create));

        customers.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Regular.otf"));
        addcustomer.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Regular.otf"));

        tabs.getTabAt(1).setCustomView(customers);
        tabs.getTabAt(0).setCustomView(addcustomer);

        if (getIntent().hasExtra("key")){
            viewPager.setCurrentItem(1);
        }

        if (getIntent().hasExtra("add")){
            viewPager.setCurrentItem(1);
            viewPager.setOffscreenPageLimit(1);
        }

        viewPager.setSwipeable(false);
    }

    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }
   private void setUpViewPager(ViewPager pager) {

       CustomViewPagerAdapter adapter = new CustomViewPagerAdapter(getSupportFragmentManager());
       adapter.addFragment(new  List_of_Invoices(), getString(R.string.header_invoices_list));
       adapter.addFragment(new Fragment_Create_Invoice(), getString(R.string.header_invoices_create));
       pager.setAdapter(adapter);



   }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        Log.e(TAG, "requestCode Path"+requestCode);
//        Log.e(TAG, "resultCode Path"+resultCode);

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

}






