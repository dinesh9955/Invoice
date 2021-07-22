package com.sirapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.sirapp.Constant.Constant;
import com.sirapp.Details.Company_Details_Activity;
import com.sirapp.Model.Company_list;
import com.sirapp.R;
import com.sirapp.Utils.GlideApp;
//import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Company_Listing_Adapter extends RecyclerView.Adapter<Company_Listing_Adapter.ViewHolderForCat> {

    private static final String TAG = "Company_Listing_Adapter";
    private Context mcontext;
    ArrayList<Company_list> mlist = new ArrayList<>();


    public Company_Listing_Adapter(Context mcontext, ArrayList<Company_list> list) {
        this.mcontext = mcontext;
        mlist = list;

    }


    @NonNull
    @Override
    public Company_Listing_Adapter.ViewHolderForCat onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myitem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.company_itemlayout, viewGroup, false);
        ViewHolderForCat viewHolderForCat = new ViewHolderForCat(myitem);
        return viewHolderForCat;
    }

    @Override
    public void onBindViewHolder(@NonNull final Company_Listing_Adapter.ViewHolderForCat viewHolderForCat, final int i) {


        final SharedPreferences pref = mcontext.getSharedPreferences(Constant.PREF_BASE,Context.MODE_PRIVATE);
        Company_list company_list = mlist.get(i);

        final String company_id = company_list.getCompany_id();
        final String company_email = company_list.getCompany_email();
        final String company_phone = company_list.getCompany_phone();
        final String company_website = company_list.getCompany_website();
        final String currencyid = company_list.getCurrencyid();


        final String bankname = company_list.getPayment_bank_name();
        final String paypalemail = company_list.getPaypal_email();
        final String payment_swiftbic = company_list.getPayment_swift_bic();
        final String cheque_payableto = company_list.getCheque_payable_to();
        final String ibnnumber = company_list.getIbn_number();
        final String color = company_list.getColor();






        final String image_path = company_list.getCompany_image_path() + company_list.getCompany_logo();




        final String company_name = company_list.getCompany_name();
        if (company_name.equals("") && company_name.equals("null")) {
            viewHolderForCat.companyname.setText("");
        } else {
            viewHolderForCat.companyname.setText(company_name);
        }


        final String company_address = company_list.getCompany_address();
        if (company_address.equals("") && company_address.equals("null")) {
            viewHolderForCat.address.setText("");
        } else {

            if (company_address.length()>35){
                viewHolderForCat.address.setText(company_address.substring(0,33)+"...");
            }
            else {
                viewHolderForCat.address.setText(company_address);
            }
        }

        String logo = company_list.getCompany_logo();
       /* if (logo == null || logo.equals("") || logo.equals("null")) {
            viewHolderForCat.image.setImageResource(R.drawable.placeholder_big);
        } else {
            GlideApp.with(mcontext).load(image_path).into(viewHolderForCat.image);
        }*/
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.drawable.app_icon);

        Log.e(TAG, "image_path "+image_path);

//        Picasso.get()
//                .load("http://sir-app.com/app/uploads/company/60c08c6627181.jpg")
//                .resize(50, 50)
//                .centerCrop()
//                .into(viewHolderForCat.image);


        GlideApp.with(mcontext)
                .load(image_path)
                .apply(options)
                .into(viewHolderForCat.image);

//        GlideApp.with(mcontext)
//                .load(image_path)
//                .apply(options)
//                .into(viewHolderForCat.image);

//        GlideApp.with(context)
//                .load("http://via.placeholder.com/300.png")
//                .centerCrop()
//                .into(ivImg);
//        MyAppGlideModule.with(context).load(url).apply(RequestOptions.circleCropTransform()).into(viewHolderForCat.image);


        /*viewHolderForCat.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext,Company_Details_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("company_id",company_id);
                intent.putExtra("company_name",company_name);
                intent.putExtra("company_image",image_path);
                intent.putExtra("company_email",company_email);
                intent.putExtra("company_phone",company_phone);
                intent.putExtra("company_website",company_website);
                intent.putExtra("company_address",company_address);
                mcontext.startActivity(intent);
            }
        });*/

        viewHolderForCat.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, Company_Details_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                pref.edit().putString(Constant.COMPANY_NAME,company_name).commit();
                pref.edit().putString(Constant.COMPANY_LOGO,image_path).commit();
                pref.edit().putString(Constant.COMPANY_EMAIL,company_email).commit();
                pref.edit().putString(Constant.COMPANY_PHONE,company_phone).commit();
                pref.edit().putString(Constant.COMPANY_WEB,company_website).commit();
                pref.edit().putString(Constant.COMPANY_ID,company_id).commit();
                pref.edit().putString(Constant.COMPANY_ADDRESS,company_address).commit();
                pref.edit().putString(Constant.CURRENCY_ID,currencyid).commit();


                pref.edit().putString(Constant.Payment_bank_name,bankname).commit();
                pref.edit().putString(Constant.Paypal_email,paypalemail).commit();
                pref.edit().putString(Constant.Payment_swift_bic,payment_swiftbic).commit();
                pref.edit().putString(Constant.Cheque_payable_to,cheque_payableto).commit();
                pref.edit().putString(Constant.Ibn_number,ibnnumber).commit();
                pref.edit().putString("color",color).commit();




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
    public void updateList(ArrayList<Company_list> list){
        mlist = list;
        notifyDataSetChanged();
    }
}