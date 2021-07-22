package com.sir.Settings;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sir.Base.BaseActivity;
import com.sir.R;

public class FAQsActivity extends BaseActivity {

    private static final String TAG = "FAQsActivity";
    RecyclerView recycler_invoices;

    FAQsAdapter invoicelistAdapterdt;

 //   ArrayList<String> arrayListNames = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_language);
        //Constant.bottomNav(Create_Invoice_Activity.this,1);

        overridePendingTransition(R.anim.flip_out, R.anim.flip_in);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        Toolbar toolbar = findViewById(R.id.toolbarprint);
        TextView titleView = toolbar.findViewById(R.id.title1);
        ImageView backbtn = toolbar.findViewById(R.id.backbtn);

        TextView textViewDone = toolbar.findViewById(R.id.textViewDone);
        textViewDone.setVisibility(View.GONE);

        titleView.setText(getString(R.string.header_faqs));

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


        recycler_invoices = findViewById(R.id.recycler_invoices);


        String[] faqArray = getResources().getStringArray(R.array.faqQuestionArray);

//        arrayListNames.add("How to get started / How does the app function?");
//        arrayListNames.add("If data is created from add new functionality directly from creation page, newly created data not display?");
//        arrayListNames.add("How to add users?");
//        arrayListNames.add("How to add a warehouse?");
//        arrayListNames.add("How to track warehouse?");
//        arrayListNames.add("How to record opening stock?");
//        arrayListNames.add("How to account physical stock taking records?");
//        arrayListNames.add("How to move stock between warehouse?");
//        arrayListNames.add("How to inventory will be updated automaticallty?");
//        arrayListNames.add("How is stock value calculated?");
//        arrayListNames.add("How to create an invoice?");
//        arrayListNames.add("When should I select warehouse while creating Invoice / Purchase order?");
//        arrayListNames.add("When should I create a Product?");
//        arrayListNames.add("How to view, duplicate or share an invoice?");
//        arrayListNames.add("How will my brand color reflect in templates?");
//        arrayListNames.add("If warehouse selected does not have sufficient quantity for invoicing, how can I process the invoices?");
////        arrayListNames.add("How to view, duplicate, share or change templates for an Invoice?");
//        arrayListNames.add("How to check invoice seen status?");
//        arrayListNames.add("How to Mark invoices as paid?");
//        arrayListNames.add("How to convert invoice to receipt?");
//        arrayListNames.add("How to Mark purchase order as delivery received?");
//        arrayListNames.add("How to delete an Invoice / Purchase order?");
//        arrayListNames.add("How to Mark an Invoice / Purchase order as void?");
//        arrayListNames.add("How to create debit / credit note and their effect?");
//        arrayListNames.add("How will estimate status change?");
//        arrayListNames.add("Which details are visible on home screen and how to switch companies?");
//        arrayListNames.add("How can I set up online payments?");
//        arrayListNames.add("How much does it cost to integrate payment gateways?");
//        arrayListNames.add("How to change number format?");
//        arrayListNames.add("How to share invoice with payment links?");
//        arrayListNames.add("How will I know if an invoice has been paid online?");
//        arrayListNames.add("When will I receive the money in my bank account?");
//        arrayListNames.add("How does SIR handle card payments?");
//        arrayListNames.add("How to switch / restore / upgrade subscription?");
//        arrayListNames.add("How to change language?");
//        arrayListNames.add("Is my data secure?");
//        arrayListNames.add("My query is not listed here?");

        invoicelistAdapterdt = new FAQsAdapter(FAQsActivity.this, faqArray);
        recycler_invoices.setAdapter(invoicelistAdapterdt);
        recycler_invoices.setLayoutManager(new LinearLayoutManager(FAQsActivity.this, LinearLayoutManager.VERTICAL, false));
        recycler_invoices.setHasFixedSize(true);
        invoicelistAdapterdt.notifyDataSetChanged();

    }


}
