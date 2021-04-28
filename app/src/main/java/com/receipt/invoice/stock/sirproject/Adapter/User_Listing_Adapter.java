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
import com.receipt.invoice.stock.sirproject.Details.User_Detail_Activity;
import com.receipt.invoice.stock.sirproject.Model.User_list;
import com.receipt.invoice.stock.sirproject.R;

import java.util.ArrayList;

/**
 * Created by Fawad on 4/15/2020.
 */



public class User_Listing_Adapter extends RecyclerView.Adapter<User_Listing_Adapter.ViewHolderForCat> {

    private Context mcontext ;

    ArrayList<User_list> musername=new ArrayList<>();

     String logourl;

    public User_Listing_Adapter(Context mcontext , ArrayList<User_list> username){
        this.mcontext = mcontext;
        musername=username;

    }


    @NonNull
    @Override
    public User_Listing_Adapter.ViewHolderForCat onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myitem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_itemlayout , viewGroup , false);
        ViewHolderForCat viewHolderForCat =new ViewHolderForCat(myitem);
        return viewHolderForCat;
    }

    @Override
    public void onBindViewHolder(@NonNull final User_Listing_Adapter.ViewHolderForCat viewHolderForCat, final int i) {

      //  viewHolderForCat.username.setText(musername.get(i));

        User_list user_list = musername.get(i);

        final String user_id = user_list.getUser_id();
        final String fullname = user_list.getFull_name();
        final String user_email = user_list.getEmail_id();
        String user_role = user_list.getRole();
        String user_image = user_list.getImage();
        final String invoice = user_list.getInvoice();
        final String estimate = user_list.getEstimate();
        final String stock = user_list.getStock();
        final String receipt = user_list.getReceipt();
        final String purchase_order = user_list.getPurchase_order();
        final String payment = user_list.getPayment_voucher();
        final String tax = user_list.getTax();
        final String customer = user_list.getCustomer();
        final String supplier = user_list.getSupplier();
        final String product = user_list.getProduct();
        final String service = user_list.getService();
        final String debit = user_list.getDebit_note();
        final String credit = user_list.getCredit_note();
        final String subadmin = user_list.getSub_admin();

        final String logourl  = user_list.getImage_path() + user_list.getImage();


        if (fullname.equals("") || fullname.equals("null"))
        {
            viewHolderForCat.username.setText("");
        }
        else
        {
            viewHolderForCat.username.setText(fullname);
        }

        if (user_role.equals("") || user_role.equals("null"))
        {
            viewHolderForCat.userrole.setText("");
        }
        else
        {
            viewHolderForCat.userrole.setText(user_role);
        }

        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.drawable.app_icon);
        Glide.with(mcontext)
                .load(logourl)
                .apply(options)
                .into(viewHolderForCat.image);





        viewHolderForCat.userdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext,User_Detail_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("namee",fullname);
                intent.putExtra("email",user_email);
                intent.putExtra("invoice",invoice);
                intent.putExtra("estimate",estimate);
                intent.putExtra("stock",stock);
                intent.putExtra("receipt",receipt);
                intent.putExtra("purchase_order",purchase_order);
                intent.putExtra("payment",payment);
                intent.putExtra("tax",tax);
                intent.putExtra("customer",customer);
                intent.putExtra("supplier",supplier);
                intent.putExtra("product",product);
                intent.putExtra("service",service);
                intent.putExtra("debit",debit);
                intent.putExtra("credit",credit);
                intent.putExtra("subadmin",subadmin);
                intent.putExtra("user_id",user_id);

                intent.putExtra("userpath",logourl);


                mcontext.startActivity(intent);
                Log.e("sendname",fullname);
            }
        });

    }
    @Override
    public int getItemCount() {
        return musername.size();
        //return 2;
    }


    public class ViewHolderForCat extends RecyclerView.ViewHolder {


        TextView username,userrole,userdetail;
        RoundedImageView image;


        public ViewHolderForCat(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            userrole = itemView.findViewById(R.id.userrole);
            userdetail = itemView.findViewById(R.id.userdetail);
            image = itemView.findViewById(R.id.image);

            username.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
            userrole.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Light.otf"));
            userdetail.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/Ubuntu-Light.ttf"));

        }

    }
   /* public void updateList(List<String> list){
    musername = list;
    notifyDataSetChanged();
}*/
}