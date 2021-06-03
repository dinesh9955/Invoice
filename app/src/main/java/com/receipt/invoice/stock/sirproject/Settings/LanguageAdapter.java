package com.receipt.invoice.stock.sirproject.Settings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.receipt.invoice.stock.sirproject.R;

import java.util.ArrayList;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolderForCat> {
    private static final String TAG = "LanguageAdapter";

    //InvoiceCallBack invoiceCallBack;

    private Context mcontext;

    ArrayList<String> arrayListNames = new ArrayList<>();

    private int selectedPos = -1;

    LanguageActivity activity;

    public LanguageAdapter(Context mcontext, ArrayList<String> list, LanguageActivity languageActivity) {
        this.mcontext = mcontext;
        arrayListNames = list;
        activity = languageActivity;
    }




    @NonNull
    @Override
    public LanguageAdapter.ViewHolderForCat onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myitem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_language, viewGroup, false);
        LanguageAdapter.ViewHolderForCat viewHolderForCat = new LanguageAdapter.ViewHolderForCat(myitem);
        return viewHolderForCat;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final LanguageAdapter.ViewHolderForCat viewHolderForCat, final int i) {

        viewHolderForCat.textViewName.setText(""+arrayListNames.get(i));

        if(i == selectedPos)
        {
            viewHolderForCat.imageViewCheck.setVisibility(View.VISIBLE);
        } else {
            viewHolderForCat.imageViewCheck.setVisibility(View.INVISIBLE);
        }

        viewHolderForCat.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewHolderForCat.getAdapterPosition() == selectedPos)
                {
                    selectedPos = -1;
                } else {
                    selectedPos = viewHolderForCat.getAdapterPosition();
                }

                Log.e(TAG, "viewHolderForCat.getAdapterPosition() "+viewHolderForCat.getAdapterPosition());

                activity.onLanguageClickBack(selectedPos);

                notifyDataSetChanged();
            }
        });



    }




    @Override
    public int getItemCount() {
        return arrayListNames.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateLanguagePosition(int languagePostion) {
        selectedPos = languagePostion;
        notifyDataSetChanged();
    }
    //end for removing redundancy

    public class ViewHolderForCat extends RecyclerView.ViewHolder {


        ImageView imageViewCheck;
        TextView textViewName ;
        View view;

        public ViewHolderForCat(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_setting);
            imageViewCheck = itemView.findViewById(R.id.imageView);

            view = itemView;


        }

    }
    public void updateList(ArrayList<String> list){
        arrayListNames = list;
        notifyDataSetChanged();
    }
}