package com.sir.Adapter;

import android.app.Dialog;
import android.content.Context;
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

import com.makeramen.roundedimageview.RoundedImageView;
import com.sir.Invoice.SavePref;
import com.sir.Model.Service_list;
import com.sir.R;
import com.sir.Utils.Utility;

import java.util.ArrayList;

/**
 * Created by Fawad on 7/1/2020.
 */
//Service_bottom_Adapter

public class Service_bottom_Adapter extends RecyclerView.Adapter<Service_bottom_Adapter.ViewHolderForCat> {

    private static final String TAG = "Service_bottom_Adapter";
    private Context mcontext ;
    ArrayList<Service_list> mlist=new ArrayList<>();

    Callback callback;

    String show_name,show_price,show_quantity;
//    double sh_quantity;

    double sh_price;
//    int shquentityedt;

    public Service_bottom_Adapter(Context mcontext , ArrayList<Service_list>list,Callback callback){
        this.mcontext = mcontext;
        mlist=list;
        this.callback = callback;

    }


    @NonNull
    @Override
    public Service_bottom_Adapter.ViewHolderForCat onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myitem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.service_bottom_itemlayout , viewGroup , false);
        ViewHolderForCat viewHolderForCat =new ViewHolderForCat(myitem);
        return viewHolderForCat;
    }

    @Override
    public void onBindViewHolder(@NonNull final Service_bottom_Adapter.ViewHolderForCat viewHolderForCat, final int i) {

        SavePref pref = new SavePref();
        pref.SavePref(mcontext);
        int numberPostion = pref.getNumberFormatPosition();

        final Service_list service_list = mlist.get(i);
        String company_id = service_list.getCompany_id();
        final String service_id = service_list.getService_id();
        final String service_name = service_list.getService_name();
        final String service_price = service_list.getService_price();
        final String service_description = service_list.getService_description();
        final String service_taxable = service_list.getService_taxable();
        final String service_category = service_list.getService_category();
        String service_price_unit = service_list.getService_price_unit();
        final String measurement_unit = service_list.getMeasurement_unit();
        final String service_quantity = service_list.getService_quantity();


        if (service_name.equals("") || service_name.equals("null"))
        {
            viewHolderForCat.servicename.setText("");
        }
        else
        {
            viewHolderForCat.servicename.setText(service_name);
        }

        if (service_price.equals("") || service_price.equals("null"))
        {
            viewHolderForCat.servicecurrency.setText("");
        }
        else
        {
          //  DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");
            double vc = Double.parseDouble(service_price);
            String patternFormat= Utility.getPatternFormat(""+numberPostion, vc);
         //   String price = formatter.format(Double.parseDouble(service_price));
            viewHolderForCat.servicecurrency.setText(patternFormat);

//            viewHolderForCat.servicecurrency.setText(service_price.replace(".00",""));
        }
        if (service_price_unit.equals("") || service_price_unit.equals("null"))
        {
            viewHolderForCat.servicecurrencyunit.setText("");
        }
        else
        {
            viewHolderForCat.servicecurrencyunit.setText(service_price_unit);

        }
        if (service_quantity.equals("") || service_quantity.equals("null"))
        {
            viewHolderForCat.servicedescription.setText("");
        }
        else
        {
         //   DecimalFormat formatter = new DecimalFormat("##,##,##,##0.00");
           // String producttotatlps=formatter.format(Double.parseDouble(service_quantity));
            double vc = Double.parseDouble(service_quantity);
            String patternFormat= Utility.getPatternFormat(""+numberPostion, vc);
            viewHolderForCat.servicedescription.setText(mcontext.getString(R.string.invoice_QuantityAvailable)+" "+patternFormat);
        }


        viewHolderForCat.serviceunit.setText(""+" "+service_description);


        if (measurement_unit.equals("") || measurement_unit.equals("null"))
        {
            viewHolderForCat.serviceunit2.setText("");
            viewHolderForCat.serviceunit2.setVisibility(View.GONE);
        }
        else
        {
            viewHolderForCat.serviceunit2.setText(mcontext.getString(R.string.invoice_Unit)+" "+measurement_unit);
            viewHolderForCat.serviceunit2.setVisibility(View.VISIBLE);
        }




        viewHolderForCat.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                show_price = service_list.getService_price();
                show_name = service_list.getService_name();
                show_quantity = service_list.getService_quantity();

//                sh_quantity = Double.parseDouble(service_list.getService_quantity());

                servicedialog(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mlist.size();
        //return 2;
    }



    public class ViewHolderForCat extends RecyclerView.ViewHolder {


        TextView servicename,servicedescription,servicecurrency,servicecurrencyunit,serviceunit ,serviceunit2 ;
        RoundedImageView image;


        public ViewHolderForCat(@NonNull View itemView) {
            super(itemView);

            servicename = itemView.findViewById(R.id.servicename);
            servicedescription = itemView.findViewById(R.id.servicedescription);
            servicecurrency = itemView.findViewById(R.id.servicecurrency);
            servicecurrencyunit = itemView.findViewById(R.id.servicecurrencyunit);
            serviceunit = itemView.findViewById(R.id.serviceunit);
            image = itemView.findViewById(R.id.image);
            serviceunit2 = itemView.findViewById(R.id.serviceunit2);

            serviceunit2.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
            servicename.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
            servicedescription.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Light.otf"));
            serviceunit.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Light.otf"));
            servicecurrency.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/Ubuntu-Bold.ttf"));
            servicecurrencyunit.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/Ubuntu-Light.ttf"));

        }

    }
    public void updateList(ArrayList<Service_list> list){
        mlist = list;
        notifyDataSetChanged();
    }

    public void servicedialog(int i)
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

                try{
                    double en_quantity = Double.parseDouble(edquantity.getText().toString());
                    sh_price = Double.parseDouble(edprice.getText().toString());
                    //shquentityedt= Integer.parseInt(edquantity.getText().toString());

                    Log.e(TAG, "mlist.get(i) "+mlist.get(i).getCuurency_code());

                    callback.onPostExecutecall2(mlist.get(i), String.valueOf(en_quantity),String.valueOf(sh_price));
                    mybuilder.dismiss();
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
        void onPostExecutecall2(Service_list selected_item, String s, String price);
    }

}

