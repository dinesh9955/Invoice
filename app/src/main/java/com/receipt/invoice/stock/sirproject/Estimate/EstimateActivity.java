package com.receipt.invoice.stock.sirproject.Estimate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.crystalviewpager.widgets.CrystalViewPager;
import com.google.android.material.tabs.TabLayout;
import com.receipt.invoice.stock.sirproject.Adapter.CustomViewPagerAdapter;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.Invoice.Create_Invoice_Activity;
import com.receipt.invoice.stock.sirproject.Invoice.Fragment_Create_Invoice;
import com.receipt.invoice.stock.sirproject.Invoice.List_of_Invoices;
import com.receipt.invoice.stock.sirproject.R;
import com.receipt.invoice.stock.sirproject.Receipts.ReceiptsActivity;
import com.receipt.invoice.stock.sirproject.Utils.LockableViewPager;

public class EstimateActivity extends AppCompatActivity {


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

            Constant.toolbar(EstimateActivity.this,"Create Estimate");


            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            viewPager = findViewById(R.id.viewPagerLayout);
            tabs = findViewById(R.id.tabs);

            setUpViewPager(viewPager);

            tabs.setupWithViewPager(viewPager);

            TextView customers = (TextView) LayoutInflater.from(this).inflate(R.layout.tabview, null);
            TextView addcustomer = (TextView) LayoutInflater.from(this).inflate(R.layout.tabview, null);

            customers.setTextColor(getResources().getColor(R.color.lightpurple));
            addcustomer.setTextColor(getResources().getColor(R.color.lightpurple));
            customers.setText("CREATE ESTIMATES");
            addcustomer.setText("LIST OF ESTIMATES");

            customers.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Regular.otf"));
            addcustomer.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Regular.otf"));

            tabs.getTabAt(1).setCustomView(customers);
            tabs.getTabAt(0).setCustomView(addcustomer);

            if (getIntent().hasExtra("add")){
                viewPager.setCurrentItem(1);
            }


            viewPager.setSwipeable(false);
        }

        public static Context getContextOfApplication()
        {
            return contextOfApplication;
        }
        private void setUpViewPager(ViewPager pager) {

            CustomViewPagerAdapter adapter = new CustomViewPagerAdapter(getSupportFragmentManager());
            adapter.addFragment(new List_of_Estimate(),"LIST OF ESTIMATES");
            adapter.addFragment(new Fragment_Create_Estimate(), "CREATE ESTIMATES");
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






