package com.receipt.invoice.stock.sirproject.Home.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.receipt.invoice.stock.sirproject.Home.Model.InvoiceModel;
import com.receipt.invoice.stock.sirproject.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class HomeInvoiceAdapter extends RecyclerView.Adapter<HomeInvoiceAdapter.MyViewHolder> {

    public Context context;
    public ArrayList<InvoiceModel> invoiceModelArrayList = new ArrayList<>() ;

    public HomeInvoiceAdapter(Context context) {
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

        DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");
        double stratingvalue = Double.parseDouble(invoiceModelArrayList.get(position).getTotal());
        holder.amount.setText(formatter.format(stratingvalue) + " "+invoiceModelArrayList.get(position).getPayment_currency());



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
