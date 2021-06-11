package com.receipt.invoice.stock.sirproject.Settings;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.receipt.invoice.stock.sirproject.Base.BaseActivity;
import com.receipt.invoice.stock.sirproject.Constant.Constant;
import com.receipt.invoice.stock.sirproject.R;

public class StripeActivity extends BaseActivity {

    String companyID = "";
    String companyName = "";
    String companyImage = "";

    RoundedImageView roundedImageView;

    TextView textViewCompanyName, textViewCompanyNameDesc;

    EditText editTextEmail;

    Button buttonNext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stripe);

        Toolbar toolbar = findViewById(R.id.toolbarprint);
        TextView titleView = toolbar.findViewById(R.id.title1);
        ImageView backbtn = toolbar.findViewById(R.id.backbtn);

        TextView textViewDone = toolbar.findViewById(R.id.textViewDone);
        textViewDone.setVisibility(View.GONE);

        titleView.setText("Stripe");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        roundedImageView = findViewById(R.id.image);
        textViewCompanyName = findViewById(R.id.customerstxt1);
        textViewCompanyNameDesc = findViewById(R.id.customerstxt6);

        editTextEmail = findViewById(R.id.login_email);
        buttonNext  = findViewById(R.id.createinvoice);

        companyID = getIntent().getExtras().getString("companyID");
        companyName = getIntent().getExtras().getString("companyName");
        companyImage = getIntent().getExtras().getString("companyImage");

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




        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextEmail.getText().toString().equalsIgnoreCase("")){
                    Constant.ErrorToast(StripeActivity.this, "Enter Email");
                }else{

                }
            }
        });


//        RequestOptions options = new RequestOptions();
//        options.centerCrop();
//        options.placeholder(R.drawable.app_icon);
//        GlideApp.with(StripeActivity.this)
//                .load(companyImage)
//                .apply(options)
//                .into(roundedImageView);

//        textViewCompanyName.setText(""+companyName);
//        textViewCompanyNameDesc.setText(companyName+" partners with Stripe for secure payments.");


    }
}
