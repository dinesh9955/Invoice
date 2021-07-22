package com.sirapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.sirapp.API.AllSirApi;
import com.sirapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class mno extends AppCompatActivity {
    private static final String TAG = "mno";
    private BillingProcessor bp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mno);

        Button button = (Button) findViewById(R.id.button3);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bp.subscribe(mno.this, "com.test.purchased");
            }
        });




        bp = new BillingProcessor(this, AllSirApi.LICENSE_KEY, new BillingProcessor.IBillingHandler() {
            @Override
            public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
//                showToast("onProductPurchased: " + productId);
//                updateTextViews();

                String productID = details.productId;
                String orderID = details.orderId;
                String purchaseToken = details.purchaseToken;
                Date purchaseTime = details.purchaseTime;
                DateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
                Log.e(TAG, "datemillis22 "+simple.format(purchaseTime));
                String date = simple.format(purchaseTime);


            }
            @Override
            public void onBillingError(int errorCode, @Nullable Throwable error) {
//                showToast("onBillingError: " + Integer.toString(errorCode));
                 Log.e(TAG, "onBillingError "+error.getMessage());
            }
            @Override
            public void onBillingInitialized() {
//                showToast("onBillingInitialized");
//                readyToPurchase = true;
//                updateTextViews();
            }
            @Override
            public void onPurchaseHistoryRestored() {
                //showToast("onPurchaseHistoryRestored");
                for(String sku : bp.listOwnedProducts())
                    Log.e(TAG, "Owned Managed Product: " + sku);
                for(String sku : bp.listOwnedSubscriptions())
                    Log.e(TAG, "Owned Subscription: " + sku);
                //updateTextViews();
            }
        });

    }




}
