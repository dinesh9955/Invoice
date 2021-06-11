package com.receipt.invoice.stock.sirproject.Settings;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.receipt.invoice.stock.sirproject.Base.BaseActivity;
import com.receipt.invoice.stock.sirproject.R;

public class FAQsDetailActivity extends BaseActivity {

    private static final String TAG = "FAQsDetailActivity";

    TextView textView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_faq_detail);
        //Constant.bottomNav(Create_Invoice_Activity.this,1);

       // overridePendingTransition(R.anim.flip_out, R.anim.flip_in);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        Toolbar toolbar = findViewById(R.id.toolbarprint);
        TextView titleView = toolbar.findViewById(R.id.title1);
        ImageView backbtn = toolbar.findViewById(R.id.backbtn);

        TextView textViewDone = toolbar.findViewById(R.id.textViewDone);
        textViewDone.setVisibility(View.GONE);

        titleView.setText("FAQs");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        textViewDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        textView = findViewById(R.id.name);

        int position = getIntent().getExtras().getInt("keyPosition");

        if(position == 0){

        }else if(position == 1){

        }else if(position == 2){

        }else if(position == 3){

        }else if(position == 4){

        }else if(position == 5){

        }else if(position == 6){

        }else if(position == 7){

        }else if(position == 8){

        }else if(position == 9){
            textView.setText("Stock value is calculated as per the product rate entered while creating product in My Products module.");
        }else if(position == 10){

        }else if(position == 11){
            textView.setText("Warehouse should be selected only when product is selected to enable inventory tracking. If you select an item, do not select warehouse.");
        }else if(position == 12){

        }else if(position == 13){
            textView.setText("To perform any of the above functions, go to Invoices, then select the company, then go to the specific Invoice and swipe left, then click on more and select your preference.");
        }else if(position == 14){
            textView.setText("While creating the company, you need to select color as per preference or matching your brand identity. The color selected will be applied to the different templates in Invoices / Estimates. Select any template as per your choice while creating Invoice / Estimate. If no color has been selected, all templates will display the standard format.");
        }else if(position == 15){

        }else if(position == 16){

        }else if(position == 17){

        }else if(position == 18){

        }else if(position == 19){

        }else if(position == 20){

        }else if(position == 20){

        }else if(position == 21){

        }else if(position == 22){

        }else if(position == 23){

        }else if(position == 24){

        }else if(position == 25){

        }else if(position == 26){

        }else if(position == 27){
            textView.setText("To change comma format in numbers, go to settings then click on comma format selection then select your preference. Format selected will be applicable throughout the user account.");
        }else if(position == 28){
            textView.setText("To share an invoice with payment links, first you need to set up online payment gateways from settings. Then, while creating invoice, select the respective payment gateway you wish to get paid through. You can select both options as well. Then share invoice as link only. The payment links will be automatically displayed in the email.");
        }else if(position == 29){
            textView.setText("If an invoice has been paid through the shared payment links, then particular invoice will display \"Paid\" in Invoices summary. Alternatively, you will be notified by the respective payment gateway platform.");
        }else if(position == 30){

        }


    }
}
