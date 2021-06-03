package com.receipt.invoice.stock.sirproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.receipt.invoice.stock.sirproject.Details.Service_Detail_Activity;
import com.receipt.invoice.stock.sirproject.Invoice.SavePref;
import com.receipt.invoice.stock.sirproject.Model.Tax_List;
import com.receipt.invoice.stock.sirproject.R;
import com.receipt.invoice.stock.sirproject.Tax.Tax_Edit;
import com.receipt.invoice.stock.sirproject.Utils.Utility;

import java.util.ArrayList;

/**
 * Created by Fawad on 4/15/2020.
 */


public class Tax_Listing_Adapter extends RecyclerView.Adapter<Tax_Listing_Adapter.ViewHolderForCat> {

    private Context mcontext;
    ArrayList<Tax_List> mlist= new ArrayList<>();

    public Tax_Listing_Adapter(Context mcontext, ArrayList<Tax_List>list) {
        this.mcontext = mcontext;
        mlist = list;

    }


    @NonNull
    @Override
    public Tax_Listing_Adapter.ViewHolderForCat onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myitem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tax_itemlayout, viewGroup, false);
        ViewHolderForCat viewHolderForCat = new ViewHolderForCat(myitem);
        return viewHolderForCat;
    }

    @Override
    public void onBindViewHolder(@NonNull final Tax_Listing_Adapter.ViewHolderForCat viewHolderForCat, final int i) {

       /* viewHolderForCat.taxname.setText(mcnames.get(i));
        viewHolderForCat.taxpercent.setText(mnames.get(i));
        viewHolderForCat.taxtype.setText(maddresses.get(i));

        viewHolderForCat.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext,Tax_Activity.class);
                intent.putExtra("edit","edit");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontext.startActivity(intent);            }
        });*/


       final Tax_List tax_list = mlist.get(i);

       String tax_id = tax_list.getTax_id();
       String tax_name = tax_list.getTax_name();
       String company_name = tax_list.getCompany_name();
        String typestr = tax_list.getType();
       if (tax_name.equals("") || tax_name.equals("null"))
       {
           viewHolderForCat.taxname.setText("");
       }
       else
       {
           viewHolderForCat.taxname.setText(tax_name);
       }

       String tax_rate = tax_list.getTax_rate();
       if (tax_rate.equals("") || tax_rate.equals("null"))
       {
           viewHolderForCat.taxpercent.setText("");
       }
       else
       {
           SavePref pref = new SavePref();
           pref.SavePref(mcontext);
           int numberPostion = pref.getNumberFormatPosition();
           double vc = 0.0;
           try {
               vc = Double.parseDouble(tax_rate);
           }catch (Exception e){

           }
           String stringFormat = Utility.getPatternFormat(""+numberPostion, vc);

           if (typestr.equals("P")) {
               viewHolderForCat.taxpercent.setText(stringFormat + "%");
           }

           else  {
               viewHolderForCat.taxpercent.setText(stringFormat );
           }
       }

        if (company_name.equals("") || company_name.equals("null"))
        {
            viewHolderForCat.companyname.setText("");
        }
        else
        {
            viewHolderForCat.companyname.setText(company_name);
        }



        viewHolderForCat.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext,Tax_Edit.class);
                intent.putExtra("edit","edit");
                intent.putExtra("companyid",tax_list.getCompanyid());
                intent.putExtra("taxid",tax_list.getTax_id());
                intent.putExtra("name",tax_list.getTax_name());
                intent.putExtra("rate",tax_list.getTax_rate());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontext.startActivity(intent);            }
        });


    }

    @Override
    public int getItemCount() {
        return mlist.size();
        //return 2;
    }


    public class ViewHolderForCat extends RecyclerView.ViewHolder {


        TextView taxname, taxpercent, companyname, edit;


        public ViewHolderForCat(@NonNull View itemView) {
            super(itemView);

            taxname = itemView.findViewById(R.id.taxname);
            taxpercent = itemView.findViewById(R.id.taxpercent);
            companyname = itemView.findViewById(R.id.companyname);
            edit = itemView.findViewById(R.id.edit);

            taxname.setTypeface(Typeface.createFromAsset(mcontext.getAssets(), "Fonts/AzoSans-Medium.otf"));
            taxpercent.setTypeface(Typeface.createFromAsset(mcontext.getAssets(), "Fonts/AzoSans-Light.otf"));
            companyname.setTypeface(Typeface.createFromAsset(mcontext.getAssets(), "Fonts/AzoSans-Light.otf"));
            edit.setTypeface(Typeface.createFromAsset(mcontext.getAssets(), "Fonts/Ubuntu-Light.ttf"));

        }

    }

    public void updateList(ArrayList<Tax_List> list) {
        mlist = list;
        notifyDataSetChanged();
    }
}