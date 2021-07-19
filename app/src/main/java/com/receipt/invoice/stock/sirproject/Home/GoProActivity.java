package com.receipt.invoice.stock.sirproject.Home;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.receipt.invoice.stock.sirproject.Base.BaseActivity;
import com.receipt.invoice.stock.sirproject.R;

public class GoProActivity extends BaseActivity {

    LinearLayout linearLayout_12Month, linearLayout_1Month, linearLayoutUpgradeNow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_pro);
        overridePendingTransition(R.anim.flip_out, R.anim.flip_in);

        linearLayout_12Month = (LinearLayout) findViewById(R.id.linear_12_month);
        linearLayout_1Month = (LinearLayout) findViewById(R.id.linear_1_month);
        linearLayoutUpgradeNow = (LinearLayout) findViewById(R.id.linear_upgrade_now);


    }
}
