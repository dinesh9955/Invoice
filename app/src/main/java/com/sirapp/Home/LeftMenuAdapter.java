package com.sirapp.Home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sirapp.CN.CreditNotesActivity;
import com.sirapp.Company.Companies_Activity;
import com.sirapp.Constant.Constant;
import com.sirapp.Customer.Customer_Activity;
import com.sirapp.DN.DebitNotesActivity;
import com.sirapp.Estimate.EstimateActivity;
import com.sirapp.Invoice.InvoiceActivity;
import com.sirapp.Invoice.SavePref;
import com.sirapp.InvoiceReminder.InvoiceReminderActivity;
import com.sirapp.PO.POActivity;
import com.sirapp.PV.PVActivity;
import com.sirapp.Product.Product_Activity;
import com.sirapp.R;
import com.sirapp.Receipts.ReceiptsActivity;
import com.sirapp.Report.ReportActivity;
import com.sirapp.Service.Service_Activity;
import com.sirapp.Settings.SettingsActivity;
import com.sirapp.SignupSignin.Signin_Activity;
import com.sirapp.Stock.Stock_Activity;
import com.sirapp.Tax.Tax_Activity;
import com.sirapp.ThankYouNote.ThankYouNoteActivity;
import com.sirapp.User.User_Activity;
import com.sirapp.Utils.LocaleHelper;
import com.sirapp.Vendor.Vendor_Activity;

import java.util.ArrayList;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.sirapp.Constant.Constant.createDialogOpenClass;

public class LeftMenuAdapter extends RecyclerView.Adapter<LeftMenuAdapter.ViewHolder> {
    private static SparseBooleanArray sSelectedItems;
    private static int row_index = -1 ;
    private static final String TAG = "LeftMenuAdapter";
    ArrayList<ItemLeftMenu> alName = new ArrayList<ItemLeftMenu>();
    static Activity activity;

    SharedPreferences preferences = null;

    public LeftMenuAdapter(Activity context11, ArrayList<ItemLeftMenu> alName) {
        super();
        this.activity = context11;
         this.alName = alName;
        // FacebookSdk.sdkInitialize(context);
        sSelectedItems = new SparseBooleanArray();

        preferences = activity.getSharedPreferences(Constant.PREF_BASE, MODE_PRIVATE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        final View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.left_menu_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.tvSpecies.setText(alName.get(position).getName());
        viewHolder.imgThumbnail.setImageResource(alName.get(position).getIcon());

        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(position==0)
                        {
                            if(preferences.getString(Constant.SUB_ADMIN,"").equalsIgnoreCase("1")){
                                Intent intent = new Intent(activity, User_Activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);
                            }else{
                                createDialogOpenClass(activity);
                            }
                        }

                        if(position==1)
                        {
                            if(preferences.getString(Constant.SUB_ADMIN,"").equalsIgnoreCase("1")){
                                Intent intent = new Intent(activity, Companies_Activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);
                            }else{
                                createDialogOpenClass(activity);
                            }

                        }

                        if(position==2)
                        {
                            if(preferences.getString(Constant.PRODUCT,"").equalsIgnoreCase("1")){
                                Intent intent = new Intent(activity, Product_Activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);
                            }else{
                                createDialogOpenClass(activity);
                            }
                        }

                        if(position==3) {
                            if(preferences.getString(Constant.SERVICE,"").equalsIgnoreCase("1")){
                                Intent intent = new Intent(activity, Service_Activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);
                            }else{
                                createDialogOpenClass(activity);
                            }
                        }

                        if(position==4){
                            if(preferences.getString(Constant.TAX,"").equalsIgnoreCase("1")){
                                Intent intent = new Intent(activity, Tax_Activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);
                            }else{
                                createDialogOpenClass(activity);
                            }
                        }
                        if(position==5)
                        {
                            if(preferences.getString(Constant.CUSTOMER,"").equalsIgnoreCase("1")){
                                Intent intent = new Intent(activity, Customer_Activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);
                            }else{
                                createDialogOpenClass(activity);
                            }
                        }
                        if(position==6)
                        {
                            if(preferences.getString(Constant.SUPPLIER,"").equalsIgnoreCase("1")){
                                Intent intent = new Intent(activity, Vendor_Activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);
                            }else{
                                createDialogOpenClass(activity);
                            }
                        }
                        if(position==7)
                        {
                            if(preferences.getString(Constant.STOCK,"").equalsIgnoreCase("1")){
                                Intent intent = new Intent(activity, Stock_Activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);
                            }else{
                                createDialogOpenClass(activity);
                            }
                        }

                        if (position==8)
                        {
                            if(preferences.getString(Constant.INVOICE,"").equalsIgnoreCase("1")){
                                Intent intent = new Intent(activity, InvoiceActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);
                            }else{
                                createDialogOpenClass(activity);
                            }
                        }

                        if(position==9)
                        {
                            if(preferences.getString(Constant.ESTIMATE,"").equalsIgnoreCase("1")){
                                Intent intent = new Intent(activity, EstimateActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);
                            }else{
                                createDialogOpenClass(activity);
                            }
                        }
                        if(position==10)
                        {
                            if(preferences.getString(Constant.PURCHASE_ORDER,"").equalsIgnoreCase("1")){
                                Intent intent = new Intent(activity, POActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);
                            }else{
                                createDialogOpenClass(activity);
                            }
                        }
                        if(position==11)
                        {
                            if(preferences.getString(Constant.RECEIPT,"").equalsIgnoreCase("1")){
                                Intent intent = new Intent(activity, ReceiptsActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);
                            }else{
                                createDialogOpenClass(activity);
                            }
                        }
                        if(position==12)
                        {
                            if(preferences.getString(Constant.PAYMENT_VOUCHER,"").equalsIgnoreCase("1")){
                                Intent intent = new Intent(activity, PVActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);
                            }else{
                                createDialogOpenClass(activity);
                            }
                        }
                        if(position==13)
                        {
                            Intent intent = new Intent(activity, ReportActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);
                        }
                        if(position==14)
                        {
                            if(preferences.getString(Constant.CREDIT_NOTE,"").equalsIgnoreCase("1")){
                                Intent intent = new Intent(activity, CreditNotesActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);
                            }else{
                                createDialogOpenClass(activity);
                            }

                        }
                        if(position==15)
                        {
                            if(preferences.getString(Constant.DEBIT_NOTE,"").equalsIgnoreCase("1")){
                                Intent intent = new Intent(activity, DebitNotesActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);
                            }else{
                                createDialogOpenClass(activity);
                            }
                        }

                        if(position==16)
                        {
                            Intent intent = new Intent(activity, ThankYouNoteActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);
                        }

                        if(position==17)
                        {
                            Intent intent = new Intent(activity, InvoiceReminderActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);
                        }

                        if(position==18)
                        {
                            Intent intent = new Intent(activity, SettingsActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);
                        }


                        if(position==19)
                        {

                            preferences.edit().remove(Constant.LOGGED_IN).commit();
                            preferences.edit().remove(Constant.INVOICE).commit();
                            preferences.edit().remove(Constant.ESTIMATE).commit();
                            preferences.edit().remove(Constant.STOCK).commit();
                            preferences.edit().remove(Constant.RECEIPT).commit();
                            preferences.edit().remove(Constant.PURCHASE_ORDER).commit();
                            preferences.edit().remove(Constant.PAYMENT_VOUCHER).commit();
                            preferences.edit().remove(Constant.TAX).commit();
                            preferences.edit().remove(Constant.CUSTOMER).commit();
                            preferences.edit().remove(Constant.SUPPLIER).commit();
                            preferences.edit().remove(Constant.PRODUCT).commit();
                            preferences.edit().remove(Constant.SERVICE).commit();
                            preferences.edit().remove(Constant.DEBIT_NOTE).commit();
                            preferences.edit().remove(Constant.CREDIT_NOTE).commit();
                            preferences.edit().remove(Constant.SUB_ADMIN).commit();
                            preferences.edit().remove(Constant.ACCESS_TOKEN).commit();
                            preferences.edit().remove(Constant.FULLNAME).commit();
                            preferences.edit().remove(Constant.EMAIL).commit();
                            SavePref pref = new SavePref();
                            pref.SavePref(activity);
                            pref.setSubsType("");
                            pref.setNumberFormatPosition(0);
                            pref.setLanguagePosition(0);

                            Context context = LocaleHelper.setLocale(activity, "en");
                            Locale myLocale = new Locale("en");
                            Resources res = context.getResources();
                            DisplayMetrics dm = res.getDisplayMetrics();
                            Configuration conf = res.getConfiguration();
                            conf.locale = myLocale;
                            res.updateConfiguration(conf, dm);



                            Intent intent = new Intent(activity, Signin_Activity.class);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);
                            activity.finishAffinity();
                            activity.finish();

                        }

            }
        });


//        if(row_index==i){
//            viewHolder.layout.setBackgroundColor(Color.parseColor("#f8f8f8"));
//        }
//        else
//        {
//            viewHolder.layout.setBackgroundColor(Color.WHITE);
//
//        }
    }







    @Override
    public int getItemCount() {
        return alName.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        View view11 = null;
        public ImageView imgThumbnail;
        public TextView tvSpecies;
        public RelativeLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);
            view11 = itemView;
            imgThumbnail = (ImageView) itemView.findViewById(R.id.imageView25343455);
            tvSpecies = (TextView) itemView.findViewById(R.id.textView253456456);
        //    tvSpecies.setTypeface(EasyFontsCustom.avenirnext_TLPro_Medium(context));
            layout = (RelativeLayout) itemView.findViewById(R.id.top_layout);
        }


    }





    public void updateData(ArrayList<ItemLeftMenu> arrayList2) {
        // TODO Auto-generated method stub
        alName.clear();
        alName.addAll(arrayList2);
        notifyDataSetChanged();
    }



}

