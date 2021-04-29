package com.receipt.invoice.stock.sirproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.receipt.invoice.stock.sirproject.Customer.Customer_Detail_Activity2;
import com.receipt.invoice.stock.sirproject.Details.Customer_Detail_Activity;
import com.receipt.invoice.stock.sirproject.Model.Customer_list;
import com.receipt.invoice.stock.sirproject.R;

import java.util.ArrayList;

public class Customer_Listing_Adapter extends RecyclerView.Adapter<Customer_Listing_Adapter.ViewHolderForCat> {

    private Context mcontext ;
    ArrayList<Customer_list> mlist=new ArrayList<>();

    public Customer_Listing_Adapter(Context mcontext , ArrayList<Customer_list> list){
        this.mcontext = mcontext;
        mlist=list;

    }


    @NonNull
    @Override
    public Customer_Listing_Adapter.ViewHolderForCat onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myitem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customers_itemlayout , viewGroup , false);
        ViewHolderForCat viewHolderForCat =new ViewHolderForCat(myitem);
        return viewHolderForCat;
    }

    @Override
    public void onBindViewHolder(@NonNull final Customer_Listing_Adapter.ViewHolderForCat viewHolderForCat, final int i) {
/*
        viewHolderForCat.companyname.setText(mcnames.get(i));
        viewHolderForCat.name.setText(mnames.get(i));
        viewHolderForCat.address.setText(maddresses.get(i));
        viewHolderForCat.image.setImageResource(mimages.get(i));

        viewHolderForCat.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext,Customer_Detail_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontext.startActivity(intent);
            }
        });*/


        Customer_list customer_list = mlist.get(i);

        final String company_id = customer_list.getCompany_id();
        final String customer_id = customer_list.getCustomer_id();
        final String customer_name = customer_list.getCustomer_name();
        final String customer_contact_person = customer_list.getCustomer_contact_person();
        final String customer_address = customer_list.getCustomer_address();
        final String customer_image = customer_list.getCustomer_image_path()+customer_list.getCustomer_image();
        Log.e("customer_image",customer_image);
        final String customer_email = customer_list.getCustomer_email();
        final String customer_phone = customer_list.getCustomer_phone();
        final String customer_mobile = customer_list.getCustomer_mobile();
        final String customer_website = customer_list.getCustomer_website();

        if (customer_name.equals("") || customer_name.equals("null"))
        {
            viewHolderForCat.companyname.setText("");
        }
        else
        {
            viewHolderForCat.companyname.setText(customer_name);
        }

        if (customer_contact_person.equals("") || customer_contact_person.equals("null"))
        {
            viewHolderForCat.name.setText("");
        }
        else
        {
            viewHolderForCat.name.setText(customer_contact_person);
        }

        if (customer_address.equals("") || customer_address.equals("null"))
        {
            viewHolderForCat.address.setText("");
           // viewHolderForCat.address.setVisibility(View.GONE);
        }
        else
        {
            viewHolderForCat.address.setText(customer_address);
        }

        //viewHolderForCat.desc.setText(customer_address);


        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.drawable.app_icon);
        Glide.with(mcontext)
                .load(customer_image)
                .apply(options)
                .into(viewHolderForCat.image);


        viewHolderForCat.details.setVisibility(View.VISIBLE);
        viewHolderForCat.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, Customer_Detail_Activity2.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("company_id",company_id);
                intent.putExtra("customer_id",customer_id);
                intent.putExtra("customer_name",customer_name);
                intent.putExtra("customer_contact_person",customer_contact_person);
                intent.putExtra("customer_image",customer_image);
                intent.putExtra("customer_email",customer_email);
                intent.putExtra("customer_phone",customer_phone);
                intent.putExtra("customer_mobile",customer_mobile);
                intent.putExtra("customer_website",customer_website);
                intent.putExtra("customer_address",customer_address);
                mcontext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mlist.size();
        //return 2;
    }
    //for removing redundancy
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    //end for removing redundancy

    public class ViewHolderForCat extends RecyclerView.ViewHolder {


        TextView companyname,name,address,details, desc;
        RoundedImageView image;


        public ViewHolderForCat(@NonNull View itemView) {
            super(itemView);

            companyname = itemView.findViewById(R.id.companyname);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.invoicepricetxt);
            details = itemView.findViewById(R.id.details);
            image = itemView.findViewById(R.id.image);
//            desc = itemView.findViewById(R.id.desc);
//            desc.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));

            companyname.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
            name.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Light.otf"));
            address.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Light.otf"));
            details.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/Ubuntu-Light.ttf"));

        }

    }
    public void updateList(ArrayList<Customer_list> list){
        mlist = list;
        notifyDataSetChanged();
    }
}