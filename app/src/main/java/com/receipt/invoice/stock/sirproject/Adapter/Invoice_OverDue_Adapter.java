package com.receipt.invoice.stock.sirproject.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.receipt.invoice.stock.sirproject.Home.Model.InvoiceModel;
import com.receipt.invoice.stock.sirproject.R;
import com.receipt.invoice.stock.sirproject.Utils.GlideApp;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Fawad on 4/1/2020.
 */


public class Invoice_OverDue_Adapter extends RecyclerView.Adapter<Invoice_OverDue_Adapter.ViewHolderForCat> {

    private Context mcontext ;

    public ArrayList<InvoiceModel> invoiceModelArrayList = new ArrayList<>() ;


    public Invoice_OverDue_Adapter(Context mcontext){
        this.mcontext = mcontext;
    }

    public void setData(ArrayList<InvoiceModel> newList) {
        this.invoiceModelArrayList.clear();
        this.invoiceModelArrayList.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Invoice_OverDue_Adapter.ViewHolderForCat onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myitem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.invoiceoverdue_itemlayout , viewGroup , false);
        ViewHolderForCat viewHolderForCat =new ViewHolderForCat(myitem);
        return viewHolderForCat;
    }

    @Override
    public void onBindViewHolder(@NonNull final Invoice_OverDue_Adapter.ViewHolderForCat viewHolderForCat, final int i) {
        GlideApp.with(mcontext).load(invoiceModelArrayList.get(i).customer_logo).placeholder(R.drawable.app_icon).into(viewHolderForCat.image);
        viewHolderForCat.name.setText(invoiceModelArrayList.get(i).getInvoice_no());
        viewHolderForCat.companyname.setText(invoiceModelArrayList.get(i).getCustomer_name());

//        viewHolderForCat.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mcontext,"Coming Soon",Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return invoiceModelArrayList.size();
        //return 2;
    }


    public class ViewHolderForCat extends RecyclerView.ViewHolder {


        CircleImageView image;
        TextView invoiceoverduetxt,name,companyname;


        public ViewHolderForCat(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            invoiceoverduetxt = itemView.findViewById(R.id.invoiceoverduetxt);
            companyname = itemView.findViewById(R.id.companyname);
            image = itemView.findViewById(R.id.image);

            name.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
            invoiceoverduetxt.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Bold.otf"));
            companyname.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));

        }
    }
}
