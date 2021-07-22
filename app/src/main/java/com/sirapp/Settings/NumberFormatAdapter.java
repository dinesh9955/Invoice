package com.sirapp.Settings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sirapp.R;

import java.util.ArrayList;

public class NumberFormatAdapter extends RecyclerView.Adapter<NumberFormatAdapter.ViewHolderForCat> {
    private static final String TAG = "LanguageAdapter";

    //InvoiceCallBack invoiceCallBack;

    private Activity mcontext;

    ArrayList<String> arrayListNames = new ArrayList<>();

    private int selectedPos = -1;

    SettingsAdapter settingsAdapter;

    public NumberFormatAdapter(Activity mcontext, ArrayList<String> list, SettingsAdapter settingsAdapter11) {
        this.mcontext = mcontext;
        arrayListNames = list;
        settingsAdapter = settingsAdapter11;
    }




    @NonNull
    @Override
    public NumberFormatAdapter.ViewHolderForCat onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myitem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_number_format, viewGroup, false);
        NumberFormatAdapter.ViewHolderForCat viewHolderForCat = new NumberFormatAdapter.ViewHolderForCat(myitem);
        return viewHolderForCat;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final NumberFormatAdapter.ViewHolderForCat viewHolderForCat, final int i) {

        viewHolderForCat.textViewName.setText(""+arrayListNames.get(i));

        if(i == selectedPos)
        {
            viewHolderForCat.imageViewCheck.setImageResource(R.drawable.radio_check);
        } else {
            viewHolderForCat.imageViewCheck.setImageResource(R.drawable.radio_uncheck);
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

                settingsAdapter.numberFormatPosition(selectedPos);

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