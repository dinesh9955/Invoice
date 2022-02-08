package com.sirapp.Home.adapter;

import static android.content.Context.MODE_PRIVATE;
import static com.sirapp.Constant.Constant.createDialogOpenClass;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sirapp.Constant.Constant;
import com.sirapp.Invoice.InvoiceActivity;
import com.sirapp.Invoice.SavePref;
import com.sirapp.Utils.Utility;
import com.sirapp.Home.Model.InvoiceModel;
import com.sirapp.R;

import java.util.ArrayList;

public class HomeInvoiceAdapter extends RecyclerView.Adapter<HomeInvoiceAdapter.MyViewHolder> {

    private static final String TAG = "HomeInvoiceAdapter";


    public Activity context;
    public ArrayList<InvoiceModel> invoiceModelArrayList = new ArrayList<>() ;

    public HomeInvoiceAdapter(Activity context) {
        this.context = context;
    }

    public void setData(ArrayList<InvoiceModel> newList) {
        this.invoiceModelArrayList.clear();
        this.invoiceModelArrayList.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.businessactivities_itemlayout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.invoicestxt.setText(invoiceModelArrayList.get(position).getInvoice_no());

      //  DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");

        SavePref pref = new SavePref();
        pref.SavePref(context);

        double stratingvalue = Double.parseDouble(invoiceModelArrayList.get(position).getTotal());
        int numberPostion = pref.getNumberFormatPosition();
        Log.e(TAG, "numberPostionAA "+numberPostion);
        String stringFormat = Utility.getPatternFormat(""+numberPostion, stratingvalue);

        holder.amount.setText(stringFormat + " "+invoiceModelArrayList.get(position).getPayment_currency());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(mcontext,"Coming Soon",Toast.LENGTH_SHORT).show();
                SharedPreferences preferences = context.getSharedPreferences(Constant.PREF_BASE,MODE_PRIVATE);

                if(preferences.getString(Constant.ROLE,"").equalsIgnoreCase("USER")){
                    Intent intent = new Intent(context, InvoiceActivity.class);
                    intent.putExtra("keyList" , "keyList");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }else{
                    if(preferences.getString(Constant.SUB_ADMIN,"").equalsIgnoreCase("1")){
                        Intent intent = new Intent(context, InvoiceActivity.class);
                        intent.putExtra("keyList" , "keyList");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }else{
                        if(preferences.getString(Constant.INVOICE,"").equalsIgnoreCase("1")){
                            Intent intent = new Intent(context, InvoiceActivity.class);
                            intent.putExtra("keyList" , "keyList");
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }else{
                            createDialogOpenClass(context);
                        }
                    }

                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return invoiceModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView amount;
        private final TextView invoicestxt;
        public MyViewHolder(View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.amount);
            invoicestxt = itemView.findViewById(R.id.invoicestxt);
        }
    }
}
