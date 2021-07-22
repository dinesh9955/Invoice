package com.sir.Adapter;

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

import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.sir.Details.Vendor_Detail_Activity;
import com.sir.Model.Vendor_list;
import com.sir.R;
import com.sir.Utils.GlideApp;

import java.util.ArrayList;

public class Vendro_Listing_Adapter extends RecyclerView.Adapter<Vendro_Listing_Adapter.ViewHolderForCat> {

    private Context mcontext ;
    ArrayList<Vendor_list> mlist=new ArrayList<>();

    public Vendro_Listing_Adapter(Context mcontext , ArrayList<Vendor_list> list){
        this.mcontext = mcontext;
        mlist=list;

    }

    @NonNull
    @Override
    public Vendro_Listing_Adapter.ViewHolderForCat onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myitem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vendors_itemlayout , viewGroup , false);
        ViewHolderForCat viewHolderForCat =new ViewHolderForCat(myitem);
        return viewHolderForCat;
    }

    @Override
    public void onBindViewHolder(@NonNull final Vendro_Listing_Adapter.ViewHolderForCat viewHolderForCat, final int i) {

        /*viewHolderForCat.companyname.setText(mcnames.get(i));
        viewHolderForCat.name.setText(mnames.get(i));
        viewHolderForCat.address.setText(maddresses.get(i));
        viewHolderForCat.image.setImageResource(mimages.get(i));

        viewHolderForCat.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext,Vendor_Detail_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontext.startActivity(intent);            }
        });*/


       Vendor_list vendor_list = mlist.get(i);

        final String vendor_id = vendor_list.getVendor_id();
        final String vendor_email = vendor_list.getVendor_email();
        final String vendor_phone = vendor_list.getVendor_phone();
        final String vendor_mobile = vendor_list.getVendor_mobile();
        final String vendor_website = vendor_list.getVendor_website();
        final String contact_person = vendor_list.getVendor_contact_person();
        final String image_path = vendor_list.getVendor_image_path() + vendor_list.getVendor_image();


        Log.e("imaga path",image_path);

        final String vendor_name = vendor_list.getVendor_name();

        if (vendor_name.equals("") || vendor_name.equals(" ") || vendor_name.equals("null")) {

            viewHolderForCat.companyname.setText("");
        }
        else {
            viewHolderForCat.companyname.setText(vendor_name);
        }

        if ( contact_person.equals("") || contact_person.equals(" ") || contact_person.equals("null")) {
            viewHolderForCat.name.setText("");
        } else {
            viewHolderForCat.name.setText(contact_person);
        }


        final String company_address = vendor_list.getVendor_address();
        if (company_address.equals("") || company_address.equals(" ") || company_address.equals("null")) {
            viewHolderForCat.address.setText("");
        } else {
            viewHolderForCat.address.setText(company_address);
        }

        String logo = vendor_list.getVendor_image();
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.drawable.app_icon);
        GlideApp.with(mcontext)
                .load(image_path)
                .apply(options)
                .into(viewHolderForCat.image);

        viewHolderForCat.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, Vendor_Detail_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("vendor_id",vendor_id);
                intent.putExtra("vendor_name",vendor_name);
                intent.putExtra("vendor_contact_person",contact_person);
                intent.putExtra("image_path",image_path);
                intent.putExtra("vendor_email",vendor_email);
                intent.putExtra("vendor_phone",vendor_phone);
                intent.putExtra("vendor_website",vendor_website);
                intent.putExtra("vendor_address",company_address);
                intent.putExtra("vendor_mobile",vendor_mobile);
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


        TextView companyname,name,address,details;
        RoundedImageView image;


        public ViewHolderForCat(@NonNull View itemView) {
            super(itemView);

            companyname = itemView.findViewById(R.id.companyname);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.invoicepricetxt);
            details = itemView.findViewById(R.id.details);
            image = itemView.findViewById(R.id.image);

            companyname.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
            name.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
            address.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
            details.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/Ubuntu-Medium.ttf"));

        }

    }
    public void updateList(ArrayList<Vendor_list> list){
        mlist = list;
        notifyDataSetChanged();
    }
}