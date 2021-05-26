package com.receipt.invoice.stock.sirproject.Constant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andrognito.flashbar.Flashbar;
import com.andrognito.flashbar.anim.FlashAnim;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.receipt.invoice.stock.sirproject.Company.Companies_Activity;
import com.receipt.invoice.stock.sirproject.Customer.Customer_Activity;
import com.receipt.invoice.stock.sirproject.DN.DebitNotesActivity;
import com.receipt.invoice.stock.sirproject.Estimate.EstimateActivity;
import com.receipt.invoice.stock.sirproject.Home.Home_Activity;
import com.receipt.invoice.stock.sirproject.Invoice.Create_Invoice_Activity;
import com.receipt.invoice.stock.sirproject.CN.CreditNotesActivity;
import com.receipt.invoice.stock.sirproject.InvoiceReminder.InvoiceReminderActivity;
import com.receipt.invoice.stock.sirproject.PO.POActivity;
import com.receipt.invoice.stock.sirproject.PV.PVActivity;
import com.receipt.invoice.stock.sirproject.Product.Product_Activity;
import com.receipt.invoice.stock.sirproject.R;
import com.receipt.invoice.stock.sirproject.Receipts.ReceiptsActivity;
import com.receipt.invoice.stock.sirproject.Report.ReportActivity;
import com.receipt.invoice.stock.sirproject.Service.Service_Activity;
import com.receipt.invoice.stock.sirproject.Settings.SettingsActivity;
import com.receipt.invoice.stock.sirproject.SignupSignin.Signin_Activity;
import com.receipt.invoice.stock.sirproject.Stock.Stock_Activity;
import com.receipt.invoice.stock.sirproject.Tax.Tax_Activity;
import com.receipt.invoice.stock.sirproject.ThankYouNote.ThankYouNoteActivity;
import com.receipt.invoice.stock.sirproject.User.User_Activity;
import com.receipt.invoice.stock.sirproject.Utility;
import com.receipt.invoice.stock.sirproject.Vendor.Vendor_Activity;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Fawad on 3/31/2020.
 */

public class Constant {
    private static final String TAG = "Constant";
    public static String BASE_URL = "http://13.126.22.0/saad/app/index.php/api/";
    public static String BASE_URL_PDF = "http://13.126.22.0/saad/app/uploads/invoice/pdf/";
   // public static String BASE_URL = "https://infinitmobility.com/sir/index.php/api/";
   public static final String ACCESS_TOKEN = "mndnkdcnn";
    public static final String FULLNAME = "fn";
    public static final String EMAIL = "el";
    public static String PREF_BASE = "hdkjhkad";
    public static final String FIRST_TIME="ft";
    public static final String LOGGED_IN = "lin";
    public static final String COMPANY_NAME = "jdch";
    public static final String COMPANY_LOGO = "kdckjhdckhdh";
    public static final String COMPANY_EMAIL = "sdhjhds";
    public static final String COMPANY_PHONE = "jhdxbj";
    public static final String COMPANY_WEB = "ncxbzhjbkjchxkk";
    public static final String COMPANY_ID = "vhgzxjhghjxgzjhgxg";
    public static final String COMPANY_ADDRESS = "dcjkgkdhghjgdhcghdcjh";
    public static final String CURRENCY_ID = "ci";
    public static final String INVOICE = "nejdh";
    public static final String ESTIMATE = "njhxij";
    public static final String STOCK = "kjxhkj";
    public static final String RECEIPT = "jsdxnklm";
    public static final String PURCHASE_ORDER = "ddcdssd";
    public static final String PAYMENT_VOUCHER = "njkx";
    public static final String TAX = "jkhxkj";
    public static final String CUSTOMER = "hxjl";
    public static final String SUPPLIER = "hjcxjkxzh";
    public static final String PRODUCT = "dxojp";
    public static final String SERVICE = "xhkh";
    public static final String DEBIT_NOTE = "khjiojoik";
    public static final String CREDIT_NOTE = "jxhzhuio";
    public static final String  SUB_ADMIN = "jxshoisxuo";
    public static final String CUSTOMER_NAME = "eijxdijiojx";


    public static final String Payment_bank_name = "xhkh";
    public static final String Paypal_email = "khjiojoik";
    public static final String Payment_swift_bic = "jxhzhuio";
    public static final String Cheque_payable_to = "jxshoisxuo";
    public static final String Ibn_number = "eijxdijiojx";




    public static void toolbar(final Activity activity, String title){

        String username = Constant.GetSharedPreferences(activity, Constant.FULLNAME);
        String email = Constant.GetSharedPreferences(activity, Constant.EMAIL);
        AccountHeader header = null;
        Typeface myfont=Typeface.createFromAsset(activity.getAssets(),"Fonts/Ubuntu-Regular.ttf");

                header = new AccountHeaderBuilder()
                        .withActivity(activity)
                        .withAlternativeProfileHeaderSwitching(true)
                        .withCompactStyle(true)
                        .withPaddingBelowHeader(true)
                        .withHeightDp(115)
                        .withDividerBelowHeader(false)
                        //.withPaddingBelowHeader(true)

                        .withSelectionListEnabledForSingleProfile(false)
                        .addProfiles(new ProfileDrawerItem().withName(username).withEmail(email).withTextColor(activity.getResources().getColor(R.color.white))
                                .withTypeface(Typeface.createFromAsset(activity.getAssets(),"Fonts/AzoSans-Regular.otf"))
                                .withIcon(R.mipmap.app_icon))

                        .withOnAccountHeaderSelectionViewClickListener(new AccountHeader.OnAccountHeaderSelectionViewClickListener() {
                            @Override
                            public boolean onClick(View view, IProfile profile) {

                                /*Intent intent = new Intent(activity, AgentProfileActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);*/

                                return true;

                            }
                        }).build();




        //Drawer Items
        PrimaryDrawerItem item = new PrimaryDrawerItem().withIdentifier(1).withIcon(R.drawable.add_new_user_2).withName("Add New User").withTextColor(Color.WHITE);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withIcon(R.drawable.companies_2).withName("My Companies").withTextColor(Color.WHITE);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withIcon(R.drawable.products_2).withName("My Products (Tracked)").withTextColor(Color.WHITE);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withIcon(R.drawable.items_2).withName("My Items").withTextColor(Color.WHITE);
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(5).withIcon(R.drawable.tax_2).withName("Add Taxes").withTextColor(Color.WHITE);
        PrimaryDrawerItem item6 = new PrimaryDrawerItem().withIdentifier(6).withIcon(R.drawable.customers_2).withName("Customers").withTextColor(Color.WHITE);
        PrimaryDrawerItem item7 = new PrimaryDrawerItem().withIdentifier(5).withIcon(R.drawable.suppliers_2).withName("Suppliers").withTextColor(Color.WHITE);
        PrimaryDrawerItem item8 = new PrimaryDrawerItem().withIdentifier(5).withIcon(R.drawable.stocks_2).withName("Stocks").withTextColor(Color.WHITE);

        //2 space
        PrimaryDrawerItem item9 = new PrimaryDrawerItem().withIdentifier(5).withIcon(R.drawable.settings_2).withName("Settings").withTextColor(Color.WHITE);
        PrimaryDrawerItem item10 = new PrimaryDrawerItem().withIdentifier(5).withIcon(R.drawable.logout_2).withName("Logout").withTextColor(Color.WHITE);
        PrimaryDrawerItem item13 = new PrimaryDrawerItem().withIdentifier(5).withIcon(R.drawable.invoices_2).withName("Invoices").withTextColor(Color.WHITE);
        PrimaryDrawerItem item14 = new PrimaryDrawerItem().withIdentifier(5).withIcon(R.drawable.estimate_2).withName("Estimates").withTextColor(Color.WHITE);
        PrimaryDrawerItem item15 = new PrimaryDrawerItem().withIdentifier(5).withIcon(R.drawable.purchase_order_2).withName("Purchase Orders").withTextColor(Color.WHITE);
        PrimaryDrawerItem item16 = new PrimaryDrawerItem().withIdentifier(5).withIcon(R.drawable.recepits_2).withName("Receipts").withTextColor(Color.WHITE);
        PrimaryDrawerItem item17 = new PrimaryDrawerItem().withIdentifier(5).withIcon(R.drawable.payment_vocher_2).withName("Payment Vouchers").withTextColor(Color.WHITE);
        PrimaryDrawerItem item18 = new PrimaryDrawerItem().withIdentifier(5).withIcon(R.drawable.reports_2).withName("Reports").withTextColor(Color.WHITE);
        PrimaryDrawerItem item19 = new PrimaryDrawerItem().withIdentifier(5).withIcon(R.drawable.credit_notes_2).withName("Credit Notes").withTextColor(Color.WHITE);
        PrimaryDrawerItem item20 = new PrimaryDrawerItem().withIdentifier(5).withIcon(R.drawable.debit_note_2).withName("Debit Notes").withTextColor(Color.WHITE);
        PrimaryDrawerItem item21 = new PrimaryDrawerItem().withIdentifier(5).withIcon(R.drawable.thanku_note_2).withName("Thank You Note").withTextColor(Color.WHITE);
        PrimaryDrawerItem item22 = new PrimaryDrawerItem().withIdentifier(5).withIcon(R.drawable.invoice_reminder_2).withName("Invoice Reminders").withTextColor(Color.WHITE);

        PrimaryDrawerItem item11 = new PrimaryDrawerItem().withIdentifier(5).withName("").withTextColor(Color.WHITE);
        PrimaryDrawerItem item12 = new PrimaryDrawerItem().withIdentifier(5).withName("").withTextColor(Color.WHITE);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        final Drawer result = new DrawerBuilder()
                .withAccountHeader(header)
                .withActivity(activity)
                .withHeaderPadding(true)
                .withDrawerWidthPx((width/2)+((width/2)/2))

                //.withDrawerGravity(Gravity.RIGHT)
                //.withSliderBackgroundColorRes(R.color.sidemenublue)
                .withSliderBackgroundDrawable(activity.getResources().getDrawable(R.drawable.side_menu_bg))
                .withSelectedItem(-1)
                .addDrawerItems(
                        item11.withSelectable(false).withTypeface(myfont),
                        item.withSelectable(false).withTypeface(myfont),
                        item2.withSelectable(false).withTypeface(myfont),
                        item3.withSelectable(false).withTypeface(myfont),
                        item4.withSelectable(false).withTypeface(myfont),
                        item5.withSelectable(false).withTypeface(myfont),
                        item6.withSelectable(false).withTypeface(myfont),
                        item7.withSelectable(false).withTypeface(myfont),
                        item8.withSelectable(false).withTypeface(myfont),
                        item13.withSelectable(false).withTypeface(myfont),
                        item14.withSelectable(false).withTypeface(myfont),
                        item15.withSelectable(false).withTypeface(myfont),
                        item16.withSelectable(false).withTypeface(myfont),
                        item17.withSelectable(false).withTypeface(myfont),
                        item18.withSelectable(false).withTypeface(myfont),
                        item19.withSelectable(false).withTypeface(myfont),
                        item20.withSelectable(false).withTypeface(myfont),
                        item21.withSelectable(false).withTypeface(myfont),
                        item22.withSelectable(false).withTypeface(myfont),
                        item12.withSelectable(false).withTypeface(myfont),

                        item9.withSelectable(false).withTypeface(myfont),
                        item10.withSelectable(false).withTypeface(myfont)

                 ).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {

                    //Actions to be performed after clicking drawer items
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        SharedPreferences preferences = activity.getSharedPreferences(Constant.PREF_BASE,MODE_PRIVATE);
                        if(position==2)
                        {
                            String sub_user = preferences.getString(Constant.SUB_ADMIN,"");

                            Log.e(TAG , "SubUser1 "+sub_user);

//                            if (sub_user.equals("1"))
//                            {
                                Intent intent = new Intent(activity,User_Activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivityForResult(intent,101);
//                            }
//                            else if (sub_user.equals("0"))
//                            {
//                                ErrorToast(activity,"Permission Denied");
//                            }
                        }

                        if(position==3)
                        {

                                Intent intent = new Intent(activity,Companies_Activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);
                        }

                        if(position==4)
                        {
                            String product = preferences.getString(Constant.PRODUCT,"");
                            Log.e("product",product);
                            if (product.equals("1"))
                            {
                                Intent intent = new Intent(activity, Product_Activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);
                            }
                            else if (product.equals("0"))
                            {
                                ErrorToast(activity,"Permission Denied");
                            }

                        }

                        if(position==5) {

                            String service = preferences.getString(Constant.SERVICE,"");
                            Log.e("service",service);
                            if (service.equals("1"))
                            {
                                Intent intent = new Intent(activity, Service_Activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);
                            }
                            else if (service.equals("0"))
                            {
                            }
                            else
                            {
                                Intent intent = new Intent(activity, Service_Activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);
                            }
                        }

                        if(position==6){

                            String tax = preferences.getString(Constant.TAX,"");
                            Log.e("tax",tax);
                            if (tax.equals("1"))
                            {
                                Intent intent = new Intent(activity, Tax_Activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);
                            }
                            else if (tax.equals("0"))
                            {
                                ErrorToast(activity,"Permission Denied");
                            }


                        }
                        if(position==7)
                        {
                            String customer = preferences.getString(Constant.CUSTOMER,"");
                            Log.e("customer",customer);
                            if (customer.equals("1"))
                            {
                                Intent intent = new Intent(activity, Customer_Activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);
                            }
                            else if (customer.equals("0"))
                            {
                                ErrorToast(activity,"Permission Denied");
                            }



                        }
                        if(position==8)
                        {
                            Intent intent = new Intent(activity, Vendor_Activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);


                        }
                        if(position==9)
                        {

                            String stock = preferences.getString(Constant.STOCK,"");
                            Log.e("stock",stock);
                            if (stock.equals("1"))
                            {
                                Intent intent = new Intent(activity, Stock_Activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);
                            }
                            else if (stock.equals("0"))
                            {
                                ErrorToast(activity,"Permission Denied");
                            }

                        }

                        if (position==10)
                        {
                            String invoice = preferences.getString(Constant.INVOICE,"");
                            Log.e("invoice",invoice);
                            if (invoice.equals("1"))
                            {
                                Intent intent = new Intent(activity, Create_Invoice_Activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);
                            }
                            else if (invoice.equals("0"))
                            {
                                ErrorToast(activity,"Permission Denied");
                            }



                        }

                        if(position==11)
                        {
                            Intent intent = new Intent(activity, EstimateActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);

                        }
                        if(position==12)
                        {
                            Intent intent = new Intent(activity, POActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);

                        }
                        if(position==13)
                        {
                            Intent intent = new Intent(activity, ReceiptsActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);

                        }
                        if(position==14)
                        {
                            Intent intent = new Intent(activity, PVActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);
                        }
                        if(position==15)
                        {
                            Intent intent = new Intent(activity, ReportActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);
                        }
                        if(position==16)
                        {
                            Intent intent = new Intent(activity, CreditNotesActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);
                        }

                        if(position==17)
                        {
                            Intent intent = new Intent(activity, DebitNotesActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);
                        }

                        if(position==18)
                        {
                            Intent intent = new Intent(activity, ThankYouNoteActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);
                        }

                        if(position==19)
                        {
                            Intent intent = new Intent(activity, InvoiceReminderActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);
                        }

                        if(position==21)
                        {
                            Intent intent = new Intent(activity, SettingsActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);
                        }


                        if(position==22)
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


                            Intent intent = new Intent(activity, Signin_Activity.class);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);
                            activity.finishAffinity();
                            activity.finish();

                        }


                        return false;
                    }
                })
                .build();

        //tool bar
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        TextView titleView = toolbar.findViewById(R.id.title1);
        ImageView backbtn = toolbar.findViewById(R.id.backbtn);
//        ImageView imageViewptint = toolbar.findViewById(R.id.imageViewptint);
//        imageViewptint.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        ImageView ham = toolbar.findViewById(R.id.ham);
        titleView.setText(title);
        titleView.setTypeface(Typeface.createFromAsset(activity.getAssets(),"Fonts/AzoSans-Bold.otf"));

            backbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    activity.onBackPressed();
                }
            });

            ham.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (result.isDrawerOpen()) {

                        result.closeDrawer();
                    } else {
                        result.openDrawer();
                    }                }
            });


//            if (title.equals("Customers")){
//                ham.setVisibility(View.VISIBLE);
//                backbtn.setVisibility(View.GONE);
//
//            }
//            else if (title.equals("Vendors")){
//                ham.setVisibility(View.VISIBLE);
//                backbtn.setVisibility(View.GONE);
//
//            }
        if (title.equals("")){
            ham.setVisibility(View.VISIBLE);
            backbtn.setVisibility(View.GONE);
        }
/*
        else if (title.equals("Companies")){
            ham.setVisibility(View.VISIBLE);
            backbtn.setVisibility(View.GONE);
        }
        else if (title.equals("Details")){
            ham.setVisibility(View.VISIBLE);
            backbtn.setVisibility(View.GONE);
        }
        else if (title.equals("Taxes")){
            ham.setVisibility(View.VISIBLE);
            backbtn.setVisibility(View.GONE);
        }
        else if (title.equals("Services")){
            ham.setVisibility(View.VISIBLE);
            backbtn.setVisibility(View.GONE);
        }
        else if (title.equals("Products")){
            ham.setVisibility(View.VISIBLE);
            backbtn.setVisibility(View.GONE);
        }
        else if (title.equals("Users")){
            ham.setVisibility(View.VISIBLE);
            backbtn.setVisibility(View.GONE);
        }
        else if (title.equals("Stock")){
            ham.setVisibility(View.VISIBLE);
            backbtn.setVisibility(View.GONE);
        }
*/

    }

    public static void bottomNav(final Activity activity, int position){



       // ImageView addproperty = activity.findViewById(R.id.addprop);

        AHBottomNavigation bottomNavigation = (AHBottomNavigation) activity.findViewById(R.id.bottom_navigation);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Home", R.drawable.nav_home_2, R.color.white);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Estimates", R.drawable.nav_estimate_2, R.color.white);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Invoices", R.drawable.nav_invoice_2, R.color.white);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("Receipts", R.drawable.nav_receipt_2, R.color.white);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem("PO", R.drawable.nav_po_2, R.color.white);
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);
        bottomNavigation.addItem(item5);




//        addproperty.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(activity, Create_Invoice_Activity.class);
//                activity.startActivity(intent);
//
//            }
//        });




        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if(position==0){
                    Intent intent = new Intent(activity, Home_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                }
                else if(position==1){
                   // Toast.makeText(activity,"Coming soon",Toast.LENGTH_SHORT).show();
                       /* Intent intent = new Intent(activity, Create_Invoice_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
*/
                    Intent intent = new Intent(activity, EstimateActivity.class);
                    activity.startActivity(intent);

                }
                else if(position==2){

                    Intent intent = new Intent(activity, Create_Invoice_Activity.class);
                    activity.startActivity(intent);


                }

                else if(position==3){
                    Intent intent = new Intent(activity, ReceiptsActivity.class);
                    activity.startActivity(intent);
                }else if(position==4){
                    Intent intent = new Intent(activity, POActivity.class);
                    activity.startActivity(intent);
                }

                return false;
            }
        });

        //bottomNavigation.setDefaultBackgroundResource(R.drawable.bottomnavlayout);
    /*   bottomNavigation.setBehaviorTranslationEnabled(false);
       bottomNavigation.setAccentColor(R.color.red);
       bottomNavigation.setInactiveColor(Color.parseColor("#c5c5c5"));*/
        //bottomNavigation.setInactiveColor(Color.parseColor("#ffffff"));
        bottomNavigation.setCurrentItem(position, false);
        bottomNavigation.setBehaviorTranslationEnabled(false);
        bottomNavigation.setAccentColor(Color.parseColor("#1B60FB"));
        bottomNavigation.setInactiveColor(Color.parseColor("#6695FF"));
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);

    }

    public static void SuccessToast(Activity context,String message){
        Utility.hideKeypad(context);
        new Flashbar.Builder(context)
                .gravity(Flashbar.Gravity.BOTTOM)
                .title("\nSuccess")
                .titleTypeface(Typeface.createFromAsset(context.getAssets(),"Fonts/AzoSans-Bold.otf"))
                .titleSizeInSp(15)
                .message(message)
                //.messageTypeface(Typeface.createFromAsset(getAssets(),"fonts/CatamaraBold.ttf"))
                .backgroundDrawable(context.getResources().getDrawable(R.drawable.success_toast_background))
                .duration(2000)
                .enableSwipeToDismiss()
                .castShadow(false)
                .enterAnimation(FlashAnim.with(context)
                        .animateBar()
                        .duration(750)
                        .alpha()
                        .overshoot())
                .exitAnimation(FlashAnim.with(context)
                        .animateBar()
                        .duration(400)
                        .accelerateDecelerate())
                .build().show();
    }

    public static void ErrorToast(Activity context,String message){

        Utility.hideKeypad(context);

        new Flashbar.Builder(context)
                .gravity(Flashbar.Gravity.BOTTOM)
                .title("\nError")
                .titleTypeface(Typeface.createFromAsset(context.getAssets(),"Fonts/AzoSans-Bold.otf"))
                .titleSizeInSp(15)
                .message(message)
                //.messageTypeface(Typeface.createFromAsset(getAssets(),"fonts/CatamaraBold.ttf"))
                .backgroundDrawable(context.getResources().getDrawable(R.drawable.error_toast_background))
                .duration(3000)
                .enableSwipeToDismiss()
                .castShadow(false)
                .enterAnimation(FlashAnim.with(context)
                        .animateBar()
                        .duration(750)
                        .alpha()
                        .overshoot())
                .exitAnimation(FlashAnim.with(context)
                        .animateBar()
                        .duration(400)
                        .accelerateDecelerate())
                .build().show();
    }



    public static void ErrorToastTop(Activity context,String message){

        Utility.hideKeypad(context);

        new Flashbar.Builder(context)
                .gravity(Flashbar.Gravity.TOP)
                .title("\nError")
                .titleTypeface(Typeface.createFromAsset(context.getAssets(),"Fonts/AzoSans-Bold.otf"))
                .titleSizeInSp(15)
                .message(message)
                //.messageTypeface(Typeface.createFromAsset(getAssets(),"fonts/CatamaraBold.ttf"))
                .backgroundDrawable(context.getResources().getDrawable(R.drawable.error_toast_background))
                .duration(3000)
                .enableSwipeToDismiss()
                .castShadow(false)
                .enterAnimation(FlashAnim.with(context)
                        .animateBar()
                        .duration(750)
                        .alpha()
                        .overshoot())
                .exitAnimation(FlashAnim.with(context)
                        .animateBar()
                        .duration(400)
                        .accelerateDecelerate())
                .build().show();
    }
    public static String GetSharedPreferences(Context context, String key){
        SharedPreferences preferences = context.getSharedPreferences(Constant.PREF_BASE,Context.MODE_PRIVATE);
        String val="";
        if (preferences.contains(key)){
            val = preferences.getString(key,"");
        }
        Log.e("valuFromPrefs",val);
        return val;
    }
}
