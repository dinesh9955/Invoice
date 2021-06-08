package com.receipt.invoice.stock.sirproject.Settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.receipt.invoice.stock.sirproject.R;

import java.util.ArrayList;

public class SubscribeAdapter extends RecyclerView.Adapter<SubscribeAdapter.ViewHolderForCat> {
    private static final String TAG = "LanguageAdapter";

    //InvoiceCallBack invoiceCallBack;

    private Context mcontext;

    ArrayList<ItemSubscribe> arrayListNames = new ArrayList<>();

    private int selectedPos = 0;


    public SubscribeAdapter(Context mcontext, ArrayList<ItemSubscribe> list) {
        this.mcontext = mcontext;
        arrayListNames = list;

    }




    @NonNull
    @Override
    public SubscribeAdapter.ViewHolderForCat onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myitem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_subscribe, viewGroup, false);
        SubscribeAdapter.ViewHolderForCat viewHolderForCat = new SubscribeAdapter.ViewHolderForCat(myitem);
        return viewHolderForCat;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final SubscribeAdapter.ViewHolderForCat viewHolderForCat, final int i) {

        viewHolderForCat.textViewName.setText(""+arrayListNames.get(i).getPlanName());
        viewHolderForCat.textViewDesc.setText(""+arrayListNames.get(i).getDescription());

        if(i == selectedPos)
        {
            viewHolderForCat.imageViewCheck.setImageResource(R.drawable.checked_green);
            viewHolderForCat.relativeLayoutRoot.setBackgroundResource(R.drawable.subscribe_white);
            viewHolderForCat.textViewName.setTextColor(Color.BLACK);
            viewHolderForCat.textViewDesc.setTextColor(Color.BLACK);
        } else {
            viewHolderForCat.imageViewCheck.setImageResource(R.drawable.checked);
            viewHolderForCat.relativeLayoutRoot.setBackgroundResource(R.drawable.subscribe_blue);
            viewHolderForCat.textViewName.setTextColor(Color.WHITE);
            viewHolderForCat.textViewDesc.setTextColor(Color.WHITE);
        }

       // viewHolderForCat.relativeLayoutRoot.setBackgroundResource(R.drawable.subscribe_white);

//        viewHolderForCat.imageViewCheck.setImageResource(R.drawable.radio_check);

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

                //activity.onLanguageClickBack(selectedPos);

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
        TextView textViewName;
        TextView textViewDesc;
        View view;
        RelativeLayout relativeLayoutRoot;

        public ViewHolderForCat(@NonNull View itemView) {
            super(itemView);

            relativeLayoutRoot = itemView.findViewById(R.id.relative_root);
            textViewName = itemView.findViewById(R.id.text_setting);
            textViewDesc = itemView.findViewById(R.id.text_setting2);
            imageViewCheck = itemView.findViewById(R.id.imageView);

            view = itemView;


        }

    }
    public void updateList(ArrayList<ItemSubscribe> list){
        arrayListNames = list;
        notifyDataSetChanged();
    }
}