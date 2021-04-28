package com.receipt.invoice.stock.sirproject.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.receipt.invoice.stock.sirproject.R;

import java.util.ArrayList;

/**
 * Created by Fawad on 5/6/2020.
 */
//List_of_Invoices_Adapter

public class List_of_Invoices_Adapter extends RecyclerView.Adapter<List_of_Invoices_Adapter.ViewHolderForCat> {

    private Context mcontext ;
    ArrayList<String> mlist=new ArrayList<>();


    public List_of_Invoices_Adapter(Context mcontext , ArrayList<String>list){
        this.mcontext = mcontext;
        mlist=list;

    }


    @NonNull
    @Override
    public List_of_Invoices_Adapter.ViewHolderForCat onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myitem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_of_invoices_itemlayout , viewGroup , false);
        ViewHolderForCat viewHolderForCat =new ViewHolderForCat(myitem);
        return viewHolderForCat;
    }

    @Override
    public void onBindViewHolder(@NonNull final List_of_Invoices_Adapter.ViewHolderForCat viewHolderForCat, final int i) {

        /*viewHolderForCat.productname.setText(mproductname.get(i));
       *//* viewHolderForCat.name.setText(mnames.get(i));
        viewHolderForCat.address.setText(maddresses.get(i));
        viewHolderForCat.image.setImageResource(mimages.get(i));*//*
//       viewHolderForCat.itemView.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View view) {
//               Intent intent = new Intent(mcontext,Product_Detail_Activity.class);
//               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//               mcontext.startActivity(intent);
//           }
//       });



        //reorder
/*
        viewHolderForCat.productstock.setText(mninimum);

        if (Integer.parseInt(quantity) > Integer.parseInt(mninimum)){
            viewHolderForCat.productstock.setText(" In Stock");
            viewHolderForCat.productstock.setTextColor(mcontext.getResources().getColor(R.color.green));
        }
        else{
            viewHolderForCat.productstock.setText(" Re Order");
            viewHolderForCat.productstock.setTextColor(mcontext.getResources().getColor(R.color.red));
        }
*/

    }

    @Override
    public int getItemCount() {
        return mlist.size();

    }


    public class ViewHolderForCat extends RecyclerView.ViewHolder {


        TextView invoicename,invoiceamount,invoicenum,invoiceaddress,invoicestatus;
        RoundedImageView image;


        public ViewHolderForCat(@NonNull View itemView) {
            super(itemView);

            invoicename = itemView.findViewById(R.id.invoicename);
            invoiceamount = itemView.findViewById(R.id.invoiceamount);
            invoicenum = itemView.findViewById(R.id.invoicenum);
            invoiceaddress = itemView.findViewById(R.id.invoiceaddress);
            invoicestatus = itemView.findViewById(R.id.invoicestatus);


            invoicename.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
            invoiceamount.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
            invoicenum.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Light.otf"));
            invoiceaddress.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Light.otf"));
            invoicestatus.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Light.otf"));


        }

    }
    /*public void updateList(ArrayList<Product_list> list){
        mlist = list;
        notifyDataSetChanged();
    }*/
}