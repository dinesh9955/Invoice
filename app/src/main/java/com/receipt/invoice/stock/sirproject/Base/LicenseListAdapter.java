package com.receipt.invoice.stock.sirproject.Base;

import android.app.Activity;

import com.receipt.invoice.stock.sirproject.Home.Model.InvoiceModel;
import com.receipt.invoice.stock.sirproject.R;

import java.util.ArrayList;

public class LicenseListAdapter extends BaseRecyclerViewAdapter {

    public ArrayList<InvoiceModel> invoiceModelArrayList = new ArrayList<>() ;

    public LicenseListAdapter(Activity appLicensePlanActivity) {
        super(appLicensePlanActivity);
        layout_id = R.layout.layout_customer;
//        invoiceModelArrayList = models;
    }


    @Override
    public void onViewHolderBind(BaseViewHolder viewHolder, int position, Object data) {




//        Button btnBuyButtonOfLicencePlaneILLP;
//        TextView lblAmountOfLicencePlaneILLP, lblDayOfLicencePlaneILLP;
//
//        lblAmountOfLicencePlaneILLP = viewHolder.itemView.findViewById(R.id.lblAmountOfLicencePlaneILLP);
//        lblDayOfLicencePlaneILLP = viewHolder.itemView.findViewById(R.id.lblDayOfLicencePlaneILLP);
//        btnBuyButtonOfLicencePlaneILLP = viewHolder.itemView.findViewById(R.id.btnBuyButtonOfLicencePlaneILLP);
//
//        final LicenseRatePlanResponse tempLicensePlan = (LicenseRatePlanResponse) data;
//
//        lblAmountOfLicencePlaneILLP.setText(KSUtility.GetFloatWithOutPrecesion(tempLicensePlan.getAmount()).toString() +" only");
//        lblDayOfLicencePlaneILLP.setText(tempLicensePlan.getValidityDays().toString() +" days");
//        btnBuyButtonOfLicencePlaneILLP.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PaymentHelper.OpenPaymentDialog(Context, TransactionType.LicenseRenew, tempLicensePlan.getAmount(),tempLicensePlan.getLicenseRatePlanId());
//            }
//        });
    }

    public void setData(ArrayList<InvoiceModel> newList) {
        this.invoiceModelArrayList.clear();
        this.invoiceModelArrayList.addAll(newList);
        notifyDataSetChanged();
        setData22(dataList);
    }
}