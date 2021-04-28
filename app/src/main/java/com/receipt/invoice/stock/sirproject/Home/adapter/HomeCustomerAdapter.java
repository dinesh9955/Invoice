package com.receipt.invoice.stock.sirproject.Home.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.receipt.invoice.stock.sirproject.Home.Model.CustomerModel;
import com.receipt.invoice.stock.sirproject.R;

import java.util.ArrayList;

public class HomeCustomerAdapter extends RecyclerView.Adapter<HomeCustomerAdapter.MyViewHolder> {

    public Context context;
    public ArrayList<CustomerModel> customerModelArrayList = new ArrayList<>() ;


    public HomeCustomerAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<CustomerModel> newList) {
        this.customerModelArrayList.clear();
        this.customerModelArrayList.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_customer, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        Glide.with(context).load(customerModelArrayList.get(position).getCustomer_image_path()+customerModelArrayList.get(position).getImage()).placeholder(R.drawable.app_icon).into(holder.img_customer);

       // Log.e("image eepath",imageurldd);
        holder.txtCustomerName.setText(customerModelArrayList.get(position).getCustomerName());

    }

    @Override
    public int getItemCount() {

        return customerModelArrayList.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView img_customer;
        private final TextView txtCustomerName;

        public MyViewHolder(View itemView) {
            super(itemView);
            img_customer = itemView.findViewById(R.id.img_customer);
            txtCustomerName = itemView.findViewById(R.id.txtCustomerName);
        }
    }
}
