package com.receipt.invoice.stock.sirproject.Base;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.receipt.invoice.stock.sirproject.Home.Model.InvoiceModel;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseRecyclerViewAdapter extends RecyclerView.Adapter<BaseRecyclerViewAdapter.BaseViewHolder> {

    public int layout_id;
    protected ArrayList<InvoiceModel> dataList = new ArrayList<>();
    protected Activity Context;



    public BaseRecyclerViewAdapter(Activity context) {
        this.Context = context;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout_id, viewGroup, false);
        return new BaseViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder viewHolder, int position) {
        onViewHolderBind(viewHolder,position, dataList.get(position));
    }

    @Override
    public int getItemCount() {
        Log.e("TAAAAG" , ""+dataList.size());
        return dataList.size();
    }

    public abstract void onViewHolderBind(BaseViewHolder viewHolder, int position, Object data);

    public class BaseViewHolder extends RecyclerView.ViewHolder {
        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


    public void setData22(ArrayList<InvoiceModel> newList) {
        this.dataList.clear();
        this.dataList.addAll(newList);
        notifyDataSetChanged();


    }
}