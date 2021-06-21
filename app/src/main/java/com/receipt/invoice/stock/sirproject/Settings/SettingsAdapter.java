package com.receipt.invoice.stock.sirproject.Settings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.receipt.invoice.stock.sirproject.Adapter.Product_Bottom_Adapter;
import com.receipt.invoice.stock.sirproject.BuildConfig;
import com.receipt.invoice.stock.sirproject.Home.Home_Activity;
import com.receipt.invoice.stock.sirproject.Invoice.SavePref;
import com.receipt.invoice.stock.sirproject.Product.Product_Activity;
import com.receipt.invoice.stock.sirproject.R;
import com.receipt.invoice.stock.sirproject.Report.ReportActivity;
import com.receipt.invoice.stock.sirproject.Report.ReportAdapter;
import com.receipt.invoice.stock.sirproject.Report.ReportViewActivity;
import com.receipt.invoice.stock.sirproject.SignupSignin.Signin_Activity;

import java.util.ArrayList;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolderForCat> {

    //InvoiceCallBack invoiceCallBack;

    private Activity mcontext;

    ArrayList<Integer> arrayListIcons = new ArrayList<>();
    ArrayList<String> arrayListNames = new ArrayList<>();

    SavePref pref = new SavePref();
    private SettingsAdapter settingsAdapter;

    int languagePostion = -1;

    public SettingsAdapter(Activity mcontext, ArrayList<Integer> arrayListIcons2, ArrayList<String> list) {
        this.mcontext = mcontext;
        arrayListNames = list;
        arrayListIcons = arrayListIcons2;

        pref.SavePref(mcontext);

        settingsAdapter = this;
    }






    @NonNull
    @Override
    public SettingsAdapter.ViewHolderForCat onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myitem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_settings, viewGroup, false);
        SettingsAdapter.ViewHolderForCat viewHolderForCat = new SettingsAdapter.ViewHolderForCat(myitem);
        return viewHolderForCat;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final SettingsAdapter.ViewHolderForCat viewHolderForCat, final int i) {


        viewHolderForCat.imageViewIcon.setImageResource(arrayListIcons.get(i));
        viewHolderForCat.textViewName.setText(""+arrayListNames.get(i));


        viewHolderForCat.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i == 0){
                    numberFormatDialog();
                } else if(i == 1){
                    Intent intent = new Intent(mcontext, OnlinePaymentGatewayActivity.class);
                    mcontext.startActivity(intent);
                } else if(i == 2){
                    Intent intent = new Intent(mcontext, SubscribeActivity.class);
                    mcontext.startActivity(intent);
                } else if(i == 3){
//                    Intent intent = new Intent(mcontext, OnlinePaymentGatewayActivity.class);
//                    mcontext.startActivity(intent);
                } else if(i == 4){
//                    Intent intent = new Intent(mcontext, OnlinePaymentGatewayActivity.class);
//                    mcontext.startActivity(intent);
                } else if(i == 5){
                    try {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, mcontext.getString(R.string.app_name));
                        String shareMessage= mcontext.getString(R.string.app_name)+"\n\n";
                        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID+"\n\n";
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                        mcontext.startActivity(Intent.createChooser(shareIntent, mcontext.getString(R.string.setting_ChooseOne)));
                    } catch(Exception e) {
                        //e.toString();
                    }
                } else if(i == 6){
                    Intent intent = new Intent(mcontext, FAQsActivity.class);
                    mcontext.startActivity(intent);
                } else if(i == 7){
                    Intent intent = new Intent(mcontext, LanguageActivity.class);
                    mcontext.startActivity(intent);
                } else if(i == 8){
                    Intent intent = new Intent(mcontext, WebShowActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("positionNext", i);
                    mcontext.startActivity(intent);
                } else if(i == 9){
                    Uri uri = Uri.parse("market://details?id=" + mcontext.getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    try {
                        mcontext.startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        mcontext.startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=" + mcontext.getPackageName())));
                    }
                } else if(i == 10){
                    Intent intent = new Intent(mcontext, WebShowActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("positionNext", i);
                    mcontext.startActivity(intent);
                }else if(i == 11){
                    Intent intent = new Intent(mcontext, WebShowActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("positionNext", i);
                    mcontext.startActivity(intent);
                }else if(i == 12){
                    Intent intent = new Intent(mcontext, SupportActivity.class);
                    mcontext.startActivity(intent);
                }
            }
        });



    }




    @Override
    public int getItemCount() {
        return arrayListNames.size();
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


        ImageView imageViewIcon;
        TextView textViewName ;
        View view;

        public ViewHolderForCat(@NonNull View itemView) {
            super(itemView);

            imageViewIcon = itemView.findViewById(R.id.icon_setting);
            textViewName = itemView.findViewById(R.id.text_setting);

            view = itemView;

//            SwipeLayout swipeLayout = (SwipeLayout)itemView.findViewById(R.id.swipe);
//            swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
//            swipeLayout.addDrag(SwipeLayout.DragEdge.Left, swipeLayout.findViewById(R.id.left_view));
//            swipeLayout.addDrag(SwipeLayout.DragEdge.Right, swipeLayout.findViewById(R.id.right_view));
//
//            textViewLeftPaidUnPaid = itemView.findViewById(R.id.textdelete1);

//            customernamtxt = itemView.findViewById(R.id.customernamtxt);
//            invoicenbtxt = itemView.findViewById(R.id.invoicenbtxt);
//            invoiceduetxt = itemView.findViewById(R.id.invoiceduetxt);
//            invoicepricetxt = itemView.findViewById(R.id.invoicepricetxt);
//            statustxt = itemView.findViewById(R.id.statustxt);
//            invoicestatus = itemView.findViewById(R.id.invoicestatus);
//            invoicesttsuspend = itemView.findViewById(R.id.invoicesttsuspend);
//
//            invoicestatus.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
//            invoicesttsuspend.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
//            invoicenbtxt.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Medium.otf"));
//            invoiceduetxt.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Light.otf"));
//            invoicepricetxt.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Bold.otf"));
//            statustxt.setTypeface(Typeface.createFromAsset(mcontext.getAssets(),"Fonts/AzoSans-Bold.otf"));

        }

    }
    public void updateList(ArrayList<String> list){
        arrayListNames = list;
        notifyDataSetChanged();
    }




    public void numberFormatDialog() {

        final Dialog mybuilder = new Dialog(mcontext);
        mybuilder.setContentView(R.layout.number_format_dialog_list);

        TextView txtBack = (TextView) mybuilder.findViewById(R.id.txtBack);
        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mybuilder.dismiss();
            }
        });

        TextView txtSave = (TextView) mybuilder.findViewById(R.id.txtSave);
        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.setNumberFormatPosition(languagePostion);
                mybuilder.dismiss();
                Intent intent = new Intent(mcontext, Home_Activity.class);
                mcontext.startActivity(intent);
               // mcontext.finishAffinity();
                mcontext.finish();
            }
        });

        RecyclerView recycler_invoices = mybuilder.findViewById(R.id.recycler_list);

        ArrayList<String> arrayListNames = new ArrayList<>();
        arrayListNames.add("1,000,000.00");
        arrayListNames.add("1,00,00,000.00");
        arrayListNames.add("1.000.000,00");
        arrayListNames.add("1 000 000,00");


        languagePostion = pref.getNumberFormatPosition();

        NumberFormatAdapter invoicelistAdapterdt = new NumberFormatAdapter(mcontext, arrayListNames, settingsAdapter);
        recycler_invoices.setAdapter(invoicelistAdapterdt);
        invoicelistAdapterdt.updateLanguagePosition(languagePostion);
        recycler_invoices.setLayoutManager(new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false));
        recycler_invoices.setHasFixedSize(true);
        invoicelistAdapterdt.notifyDataSetChanged();



        mybuilder.show();
        mybuilder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Window window = mybuilder.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(R.color.transparent);

    }


    public void numberFormatPosition(int position){
        languagePostion = position;
    }


}