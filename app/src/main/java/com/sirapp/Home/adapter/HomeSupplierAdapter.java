package com.sirapp.Home.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sirapp.Home.Model.SupplierModel;
import com.sirapp.R;
import com.sirapp.Utils.GlideApp;

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
//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.e("TAG","gdgfdg");
//            }
//        });

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        GlideApp.with(context).load(supplierModelArrayList.get(position).getSupplier_image_path()+supplierModelArrayList.get(position).getImage()).placeholder(R.drawable.app_icon).into(holder.img_supplier);
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
