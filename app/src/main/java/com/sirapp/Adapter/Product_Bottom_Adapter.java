package com.sirapp.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.makeramen.roundedimageview.RoundedImageView;
import com.sirapp.Constant.Constant;
import com.sirapp.Invoice.SavePref;
import com.sirapp.SAADApplication;
import com.sirapp.Model.Product_list;
import com.sirapp.R;
import com.sirapp.Utils.GlideApp;
import com.sirapp.Utils.Utility;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Fawad on 7/1/2020.
 */
//Product_Bottom_Adapter

public class Product_Bottom_Adapter extends RecyclerView.Adapter<Product_Bottom_Adapter.ViewHolderForCat> {

    private static final String TAG = "Product_Bottom_Adapter";
    private Activity mcontext ;
    ArrayList<Product_list> mlist=new ArrayList<>();


    BottomSheetDialog bottomSheetDialog;

    SAADApplication myAppClass = (SAADApplication)getApplicationContext();

    String show_price="",show_name="";
    Callback callback;
    double sh_quantity;
    double sh_price;
    String product_ida;
    String receipt = "";

    public Product_Bottom_Adapter(Activity mcontext , ArrayList<Product_list>list,Callback callback, BottomSheetDialog bottomSheetDialog, String receipt){
        this.mcontext = mcontext;
         mlist=list;
        this.callback = callback;
        this.bottomSheetDialog = bottomSheetDialog;
        this.receipt = receipt;
    }


    @NonNull
    @Override
    public Product_Bottom_Adapter.ViewHolderForCat onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myitem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_bottom_itemlayout , viewGroup , false);
        ViewHolderForCat viewHolderForCat =new ViewHolderForCat(myitem);
        return viewHolderForCat;
    }

    @Override
    public void onBindViewHolder(@NonNull final Product_Bottom_Adapter.ViewHolderForCat viewHolderForCat, final int i) {

        SavePref pref = new SavePref();
        pref.SavePref(mcontext);
        int numberPostion = pref.getNumberFormatPosition();


        final Product_list product_list = mlist.get(i);

        String company_id = product_list.getCompany_id();
        final String product_id = product_list.getProduct_id();
        final String product_name = product_list.getProduct_name();
        String product_image = product_list.getProduct_image();
        String product_image_path = product_list.getProduct_image_path();
        String product_price = product_list.getProduct_price();
        String product_status = product_list.getProduct_status();
        String product_description = product_list.getProduct_description();
        String product_taxable = product_list.getProduct_taxable();
        String product_category = product_list.getProduct_category();
        String quantity = product_list.getQuantity();
        String mninimum = product_list.getMinimum();
        String currency_code = product_list.getCurrency_code();

        Log.e("currency_code",currency_code);
        SAADApplication.setCurencycode(currency_code);

        if (product_name.equals("") || product_name.equals("null"))
        {
            viewHolderForCat.productname.setText("");
        }
        else
        {
            viewHolderForCat.productname.setText(product_name);
        }
        if (quantity.equals("") || quantity.equals("null"))
        {
            viewHolderForCat.productquantity.setText("");
        }
        else
        {
            Log.e(TAG, "quantityCCC "+quantity);
            //DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");
           // String producttotatlps=formatter.format(Double.parseDouble(quantity));
            double vc = Double.parseDouble(quantity);
            String patternFormat= Utility.getPatternFormat(""+numberPostion, vc);
            viewHolderForCat.productquantity.setText(mcontext.getString(R.string.invoice_QuantityAvailable)+" "+patternFormat);
        }
        if (product_price.equals("") || product_price.equals("null"))
        {
            viewHolderForCat.productcurrency.setText("");
        }
        else
        {
         //   DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");
            double vc = Double.parseDouble(product_price);
            String patternFormat= Utility.getPatternFormat(""+numberPostion, vc);
//            String price = formatter.format(Double.parseDouble(product_price));
            viewHolderForCat.productcurrency.setText(patternFormat);
//            viewHolderForCat.productcurrency.setText(product_price.replace(".00",""));

        }


        if (currency_code.equals("") || currency_code.equals("null"))
        {
            viewHolderForCat.productcurrencyunit.setText("");
        }
        else
        {
            viewHolderForCat.productcurrencyunit.setText(currency_code);
        }


        if (product_description.equals("") || product_description.equals("null"))
        {
            viewHolderForCat.description.setText("");
        }
        else
        {
            viewHolderForCat.description.setText(product_description);
        }


        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.drawable.app_icon);
        GlideApp.with(mcontext)
                .load(product_list.getProduct_image_path()+product_list.getProduct_image())
                .apply(options)
                .into(viewHolderForCat.image);

       // viewHolderForCat.productcurrencyunit.setText(product_list.getCurrency_code());



        viewHolderForCat.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(receipt.equalsIgnoreCase("report")){
                   // sh_price = Double.parseDouble(edprice.getText().toString().trim());
                    callback.onPostExecutecall(mlist.get(i),"0","0");
                }else{
                    show_price = product_list.getProduct_price();
                    show_name= product_list.getProduct_name();
                    product_ida= product_list.getProduct_id();
                    Log.e("product_id",product_ida);
                    String quentityproduct=product_list.getQuantity();
                    if(quentityproduct.equals("null"))
                    {
                        Constant.ErrorToast((Activity) mcontext, mcontext.getString(R.string.invoice_InsufficientQuantityAvailable));

                        if(bottomSheetDialog != null){
                            bottomSheetDialog.dismiss();
                        }
                    }
                    else {
                        sh_quantity = Double.parseDouble(product_list.getQuantity());
                    }
                    productdialog(i);
                }


            }
        });



    }

    @Override
    public int getItemCount() {
        return mlist.size();
        //return 2;
    }


    public class ViewHolderForCat extends RecyclerView.ViewHolder {


        TextView productname,productquantity,productcurrency,productcurrencyunit, description;
        RoundedImageView image;


        public ViewHolderForCat(@NonNull View itemView) {
            super(itemView);

            productname = itemView.findViewById(R.id.productname);
            productquantity = itemView.findViewById(R.id.productquantity);
            productcurrency = itemView.findViewById(R.id.productcurrency);
            productcurrencyunit = itemView.findViewById(R.id.productcurrencyunit);
            description = itemView.findViewById(R.id.description);

            image = itemView.findViewById(R.id.image);

            productname.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
            productquantity.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Light.otf"));
            productcurrency.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/Ubuntu-Bold.ttf"));
            productcurrencyunit.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/Ubuntu-Light.ttf"));

        }

    }
    public void updateList(ArrayList<Product_list> list){
        mlist = list;
        notifyDataSetChanged();
    }



    public void productdialog(int i)
    {
        final Dialog mybuilder=new Dialog(mcontext);
        mybuilder.setContentView(R.layout.product_itemlayout);

        TextView txtprice;
        final EditText edquantity,edprice;
        Button btnok,btncancel;

        txtprice=mybuilder.findViewById(R.id.txtprice);
        edquantity=mybuilder.findViewById(R.id.edquantity);
        edprice=mybuilder.findViewById(R.id.edprice);
        btnok=mybuilder.findViewById(R.id.btnok);
        btncancel=mybuilder.findViewById(R.id.btncancel);

        edprice.setText(show_price);


//        edquantity.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//                if (edquantity.getText().toString().endsWith("."))
//                {
//                    edquantity.setText(edquantity.getText().toString().replace(".",""));
//                }
//            }
//        });


        edquantity.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
        btnok.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
        btncancel.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
        txtprice.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
        edprice.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));


        mybuilder.show();
        mybuilder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Window window = mybuilder.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(R.color.transparent);

        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mybuilder.dismiss();
                callback.closeDialog();
                if(bottomSheetDialog != null){
                    bottomSheetDialog.dismiss();
                }
                Utility.hideKeypad(mcontext);
                Log.e(TAG, "po0000 "+receipt);

                try {
                    double en_quantity = Double.parseDouble(edquantity.getText().toString());

                    if(receipt.equalsIgnoreCase("po")){
                        Log.e(TAG, "po");
//                        if (sh_quantity < en_quantity) {
//                            Log.e(TAG, "invoice1");
//                            Constant.ErrorToast((Activity) mcontext,"Insufficient Quantity Available");
//                        } else{
//                            Log.e(TAG, "invoice2");
                            sh_price = Double.parseDouble(edprice.getText().toString().trim());
                            callback.onPostExecutecall(mlist.get(i),String.valueOf(en_quantity),String.valueOf(sh_price));
//
//                        }
                    } else if(receipt.equalsIgnoreCase("pv")){
                        Log.e(TAG, "pv");
//                        if (sh_quantity < en_quantity) {
//                            Log.e(TAG, "invoice1");
//                            Constant.ErrorToast((Activity) mcontext,"Insufficient Quantity Available");
//                        } else{
//                            Log.e(TAG, "invoice2");
                            sh_price = Double.parseDouble(edprice.getText().toString().trim());
                            callback.onPostExecutecall(mlist.get(i),String.valueOf(en_quantity),String.valueOf(sh_price));
//
//                        }
                    } else if(receipt.equalsIgnoreCase("invoice")){
                        Log.e(TAG, "invoice");
                        if (sh_quantity < en_quantity) {
                            Log.e(TAG, "invoice1");
                            Constant.ErrorToast((Activity) mcontext,mcontext.getString(R.string.invoice_InsufficientQuantityAvailable));
                        } else{
                            Log.e(TAG, "invoice2");
                            sh_price = Double.parseDouble(edprice.getText().toString().trim());
                            callback.onPostExecutecall(mlist.get(i),String.valueOf(en_quantity),String.valueOf(sh_price));

                        }
                    } else if(receipt.equalsIgnoreCase("receipt")){
                        Log.e(TAG, "receipt");
//                        if (sh_quantity < en_quantity) {
//                            Log.e(TAG, "invoice1");
//                            Constant.ErrorToast((Activity) mcontext,"Insufficient Quantity Available");
//                        } else{
                            sh_price = Double.parseDouble(edprice.getText().toString().trim());
                            callback.onPostExecutecall(mlist.get(i),String.valueOf(en_quantity),String.valueOf(sh_price));
//                        }
                    }  else if(receipt.equalsIgnoreCase("estimate")){
                        Log.e(TAG, "estimate");
//                        if (sh_quantity < en_quantity) {
//                            Log.e(TAG, "invoice1");
//                            Constant.ErrorToast((Activity) mcontext,"Insufficient Quantity Available");
//                        } else{
                            sh_price = Double.parseDouble(edprice.getText().toString().trim());
                            callback.onPostExecutecall(mlist.get(i),String.valueOf(en_quantity),String.valueOf(sh_price));
//                        }
                    } else if(receipt.equalsIgnoreCase("creditnotes")){
                        Log.e(TAG, "creditnotes");
//                        if (sh_quantity < en_quantity) {
//                            Log.e(TAG, "invoice1");
//                            Constant.ErrorToast((Activity) mcontext,"Insufficient Quantity Available");
//                        } else{
                            sh_price = Double.parseDouble(edprice.getText().toString().trim());
                            callback.onPostExecutecall(mlist.get(i),String.valueOf(en_quantity),String.valueOf(sh_price));
//                        }

                    } else if(receipt.equalsIgnoreCase("debitnotes")){
                        Log.e(TAG, "debitnotes");
                        if (sh_quantity < en_quantity) {
                            Log.e(TAG, "invoice1");
                            Constant.ErrorToast((Activity) mcontext, mcontext.getString(R.string.invoice_InsufficientQuantityAvailable));
                        } else{
                            sh_price = Double.parseDouble(edprice.getText().toString().trim());
                            callback.onPostExecutecall(mlist.get(i),String.valueOf(en_quantity),String.valueOf(sh_price));
                        }

                    } else if(receipt.equalsIgnoreCase("report")){
                        Log.e(TAG, "report");
//                        if (sh_quantity < en_quantity) {
//                            Log.e(TAG, "invoice1");
//                            Constant.ErrorToast((Activity) mcontext,"Insufficient Quantity Available");
//
//                        } else{
//
//                        }
                        sh_price = Double.parseDouble(edprice.getText().toString().trim());
                        callback.onPostExecutecall(mlist.get(i),String.valueOf(en_quantity),String.valueOf(sh_price));
                    }


                }catch (Exception e){

                }



            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mybuilder.dismiss();
            }
        });

    }

    public interface Callback{
        void onPostExecutecall(Product_list selected_item, String s,String price);
        void closeDialog();
    }

}
