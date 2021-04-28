package com.receipt.invoice.stock.sirproject.Home.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.receipt.invoice.stock.sirproject.Home.Model.SupplierModel;
import com.receipt.invoice.stock.sirproject.R;

import java.util.ArrayList;

public class HomeSupplierAdapter extends RecyclerView.Adapter<HomeSupplierAdapter.MyViewHolder> {

    public Context context;
    public ArrayList<SupplierModel> supplierModelArrayList = new ArrayList<>() ;

    public HomeSupplierAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<SupplierModel> newList) {
        this.supplierModelArrayList.clear();
        this.supplierModelArrayList.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_supplier, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        Glide.with(context).load(supplierModelArrayList.get(position).getSupplier_image_path()+supplierModelArrayList.get(position).getImage()).placeholder(R.drawable.app_icon).into(holder.img_supplier);
        holder.txtCustomerName.setText(supplierModelArrayList.get(position).getSupplierName());

    }

    @Override
    public int getItemCount() {

        return supplierModelArrayList.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView img_supplier;
        private final TextView txtCustomerName;

        public MyViewHolder(View itemView) {
            super(itemView);
            img_supplier = itemView.findViewById(R.id.img_supplier);
            txtCustomerName = itemView.findViewById(R.id.txtCustomerName);
        }
    }
}
