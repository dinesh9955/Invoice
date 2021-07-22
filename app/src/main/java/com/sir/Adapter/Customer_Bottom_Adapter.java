package com.sir.Adapter;
//Customer_Bottom_Adapter

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.sir.Model.Customer_list;
import com.sir.R;
import com.sir.Utils.GlideApp;

import java.util.ArrayList;

public class Customer_Bottom_Adapter extends RecyclerView.Adapter<Customer_Bottom_Adapter.ViewHolderForCat> {

    private Context mcontext ;
    ArrayList<Customer_list> mlist=new ArrayList<>();
    Callback callback;
    String customer_namee,address,website,email,phone,clientcp;

    public Customer_Bottom_Adapter(Context mcontext , ArrayList<Customer_list> list,Callback callback){
        this.mcontext = mcontext;
        mlist=list;
        this.callback=callback;

    }

    @NonNull
    @Override
    public Customer_Bottom_Adapter.ViewHolderForCat onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myitem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customer_bottomsheet_itemlayout , viewGroup , false);
        ViewHolderForCat viewHolderForCat =new ViewHolderForCat(myitem);
        return viewHolderForCat;
    }



    @Override
    public void onBindViewHolder(@NonNull final Customer_Bottom_Adapter.ViewHolderForCat viewHolderForCat, final int i) {
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


       final Customer_list customer_list = mlist.get(i);



           final String customer_id = customer_list.getCustomer_id();
           final String customer_name = customer_list.getCustomer_name();
           final String customer_contact_person = customer_list.getCustomer_contact_person();
           final String customer_address = customer_list.getCustomer_address();
           final String customer_image = customer_list.getCustomer_image_path()+customer_list.getCustomer_image();
           final String customer_email = customer_list.getCustomer_email();
           final String customer_phone = customer_list.getCustomer_phone();
           final String customer_mobile = customer_list.getCustomer_mobile();
           final String customer_website = customer_list.getCustomer_website();


           if (customer_name.equals("") || customer_name.equals("null"))
           {
               viewHolderForCat.name.setText("");
           }
           else
           {
               viewHolderForCat.name.setText(customer_name);
           }

           if (customer_contact_person.equals("") || customer_contact_person.equals("null"))
           {
               viewHolderForCat.contactname.setText("");
           }
           else
           {
               viewHolderForCat.contactname.setText(customer_contact_person);
           }




           RequestOptions options = new RequestOptions();
           options.centerCrop();
           options.placeholder(R.drawable.app_icon);
           GlideApp.with(mcontext)
                   .load(customer_image)
                   .apply(options)
                   .into(viewHolderForCat.image);









        viewHolderForCat.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 callback.onPostExecute(mlist.get(i));

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


        TextView name,contactname;
        RoundedImageView image;


        public ViewHolderForCat(@NonNull View itemView) {
            super(itemView);


            name = itemView.findViewById(R.id.name);
            contactname = itemView.findViewById(R.id.contactname);

            image = itemView.findViewById(R.id.image);

            name.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
            contactname.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));


        }

    }
    public void updateList(ArrayList<Customer_list> list){
        mlist = list;
        notifyDataSetChanged();
    }

    public interface Callback{
        void onPostExecute(Customer_list customer_list);
    }



}
