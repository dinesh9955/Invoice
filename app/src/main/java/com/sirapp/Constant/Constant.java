package com.sirapp.Constant;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.sirapp.Customer.Customer_Activity;
import com.sirapp.Estimate.EstimateActivity;
import com.sirapp.Home.ItemLeftMenu;
import com.sirapp.Home.LeftMenuAdapter;
import com.sirapp.InvoiceReminder.InvoiceReminderActivity;
import com.sirapp.Receipts.ReceiptsActivity;
import com.sirapp.Settings.SettingsActivity;
import com.sirapp.SignupSignin.Signin_Activity;
import com.sirapp.ThankYouNote.ThankYouNoteActivity;
import com.sirapp.Company.Companies_Activity;
import com.sirapp.DN.DebitNotesActivity;
import com.sirapp.Home.Home_Activity;
import com.sirapp.Invoice.InvoiceActivity;
import com.sirapp.CN.CreditNotesActivity;
import com.sirapp.Invoice.SavePref;
import com.sirapp.PO.POActivity;
import com.sirapp.PV.PVActivity;
import com.sirapp.Product.Product_Activity;
import com.sirapp.R;
import com.sirapp.Report.ReportActivity;
import com.sirapp.Service.Service_Activity;
import com.sirapp.Stock.Stock_Activity;
import com.sirapp.Tax.Tax_Activity;
import com.sirapp.User.User_Activity;
import com.sirapp.Utils.LocaleHelper;
import com.sirapp.Utils.Utility;
import com.sirapp.Vendor.Vendor_Activity;

import java.util.ArrayList;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Fawad on 3/31/2020.
 */

public class Constant {
    private static final String TAG = "Constant";



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


    public static final String Payment_bank_name = "xhkhtry";
    public static final String Paypal_email = "khjiojoiktry";
    public static final String Payment_swift_bic = "jxhzhuiowewe";
    public static final String Cheque_payable_to = "jxshoisxuo11";
    public static final String Ibn_number = "eijxdijiojx233";


//    private Drawer fff = null;

    Drawer result;

    public static void toolbar(final Activity activity, String title){

//        ArrayList<ItemLeftMenu> contacts = new ArrayList<ItemLeftMenu>();
//
//        String username = Constant.GetSharedPreferences(activity, Constant.FULLNAME);
//        String email = Constant.GetSharedPreferences(activity, Constant.EMAIL);
//        SavePref pref = new SavePref();
//        pref.SavePref(activity);
//
////        AccountHeader header = null;
//        Typeface myfont=Typeface.createFromAsset(activity.getAssets(),"Fonts/Ubuntu-Regular.ttf");
////
////                header = new AccountHeaderBuilder()
////                        .withActivity(activity)
////                        .withAlternativeProfileHeaderSwitching(true)
////                        .withCompactStyle(true)
////                        .withHeightDp(115)
//////                        .withDividerBelowHeader(false)
//////                        .withPaddingBelowHeader(true)
////
////                        .withSelectionListEnabledForSingleProfile(false)
////                        .addProfiles(
////                                new ProfileDrawerItem().withName(username).withEmail(email).withTextColor(activity.getResources().getColor(R.color.white))
////                                .withTypeface(Typeface.createFromAsset(activity.getAssets(),"Fonts/AzoSans-Regular.otf"))
////                                .withIcon(R.mipmap.app_icon)
////                             //   ,new ProfileDrawerItem().withName("sepahdar ghorbani").withEmail("sepahdar.gh41@gmail.com")
////
////                                //(IProfile) new PrimaryDrawerItem().withIdentifier(5).withIcon(R.drawable.slide_mm).withName(activity.getString(R.string.invoice_reminders)).withTextColor(Color.WHITE)
////
////                        )
////
////
////
////                        .withOnAccountHeaderSelectionViewClickListener(new AccountHeader.OnAccountHeaderSelectionViewClickListener() {
////                            @Override
////                            public boolean onClick(View view, IProfile profile) {
////
////                                /*Intent intent = new Intent(activity, AgentProfileActivity.class);
////                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
////                                activity.startActivity(intent);*/
////
////                                return true;
////
////                            }
////                        }).build();
//
//
//
//
//        //Drawer Items
//        PrimaryDrawerItem item = new PrimaryDrawerItem().withIdentifier(1).withIcon(R.drawable.add_new_user_2).withName(activity.getString(R.string.add_new_user)).withTextColor(Color.WHITE);
//        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withIcon(R.drawable.companies_2).withName(activity.getString(R.string.my_companies)).withTextColor(Color.WHITE);
//        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withIcon(R.drawable.products_2).withName(activity.getString(R.string.my_products)).withTextColor(Color.WHITE);
//        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withIcon(R.drawable.items_2).withName(activity.getString(R.string.my_items)).withTextColor(Color.WHITE);
//        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(5).withIcon(R.drawable.tax_2).withName(activity.getString(R.string.add_taxes)).withTextColor(Color.WHITE);
//        PrimaryDrawerItem item6 = new PrimaryDrawerItem().withIdentifier(6).withIcon(R.drawable.customers_2).withName(activity.getString(R.string.home_customers)).withTextColor(Color.WHITE);
//        PrimaryDrawerItem item7 = new PrimaryDrawerItem().withIdentifier(5).withIcon(R.drawable.suppliers_2).withName(activity.getString(R.string.suppliers)).withTextColor(Color.WHITE);
//        PrimaryDrawerItem item8 = new PrimaryDrawerItem().withIdentifier(5).withIcon(R.drawable.stocks_2).withName(activity.getString(R.string.stocks)).withTextColor(Color.WHITE);
//
//        //2 space
//        PrimaryDrawerItem item9 = new PrimaryDrawerItem().withIdentifier(5).withIcon(R.drawable.settings_2).withName(activity.getString(R.string.settings)).withTextColor(Color.WHITE);
//        PrimaryDrawerItem item10 = new PrimaryDrawerItem().withIdentifier(5).withIcon(R.drawable.logout_2).withName(activity.getString(R.string.logout)).withTextColor(Color.WHITE);
//        PrimaryDrawerItem item13 = new PrimaryDrawerItem().withIdentifier(5).withIcon(R.drawable.invoices_2).withName(activity.getString(R.string.invoices)).withTextColor(Color.WHITE);
//        PrimaryDrawerItem item14 = new PrimaryDrawerItem().withIdentifier(5).withIcon(R.drawable.estimate_2).withName(activity.getString(R.string.estimate)).withTextColor(Color.WHITE);
//        PrimaryDrawerItem item15 = new PrimaryDrawerItem().withIdentifier(5).withIcon(R.drawable.purchase_order_2).withName(activity.getString(R.string.purchase_orders)).withTextColor(Color.WHITE);
//        PrimaryDrawerItem item16 = new PrimaryDrawerItem().withIdentifier(5).withIcon(R.drawable.recepits_2).withName(activity.getString(R.string.receipts)).withTextColor(Color.WHITE);
//        PrimaryDrawerItem item17 = new PrimaryDrawerItem().withIdentifier(5).withIcon(R.drawable.payment_vocher_2).withName(activity.getString(R.string.payment_vouchers)).withTextColor(Color.WHITE);
//        PrimaryDrawerItem item18 = new PrimaryDrawerItem().withIdentifier(5).withIcon(R.drawable.reports_2).withName(activity.getString(R.string.reports)).withTextColor(Color.WHITE);
//        PrimaryDrawerItem item19 = new PrimaryDrawerItem().withIdentifier(5).withIcon(R.drawable.credit_notes_2).withName(activity.getString(R.string.credit_notes)).withTextColor(Color.WHITE);
//        PrimaryDrawerItem item20 = new PrimaryDrawerItem().withIdentifier(5).withIcon(R.drawable.debit_note_2).withName(activity.getString(R.string.debit_notes)).withTextColor(Color.WHITE);
//        PrimaryDrawerItem item21 = new PrimaryDrawerItem().withIdentifier(5).withIcon(R.drawable.thanku_note_2).withName(activity.getString(R.string.thank_you_note)).withTextColor(Color.WHITE);
//        PrimaryDrawerItem item22 = new PrimaryDrawerItem().withIdentifier(5).withIcon(R.drawable.invoice_reminder_2).withName(activity.getString(R.string.invoice_reminders)).withTextColor(Color.WHITE);
//
//        PrimaryDrawerItem item111 = new PrimaryDrawerItem().withIdentifier(5).withIcon(R.drawable.slide_mm)
//                .withIcon(R.mipmap.app_icon)
//                .withName(activity.getString(R.string.invoice_reminders))
//                .withDescription("dgdfgdfg\nhfghj")
//                .withBadge("fdsffgfg")
//                .withTextColor(Color.WHITE);
//
//        PrimaryDrawerItem item12 = new PrimaryDrawerItem().withIdentifier(5).withName("").withTextColor(Color.WHITE);
//        PrimaryDrawerItem item11 = new PrimaryDrawerItem().withIdentifier(5).withName("").withTextColor(Color.WHITE);
//
////        PrimaryDrawerItem item111 = new PrimaryDrawerItem().withIdentifier(5).withName("").withTextColor(Color.WHITE);
////        PrimaryDrawerItem item122 = new PrimaryDrawerItem().withIdentifier(5).withName("").withTextColor(Color.WHITE);
//
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int height = displayMetrics.heightPixels;
//        int width = displayMetrics.widthPixels;
//
//        LayoutInflater inflater = activity.getLayoutInflater();
//        View lst_item_view = inflater.inflate(R.layout.custom_header, null);
//        TextView headerName = (TextView) lst_item_view.findViewById(R.id.headerName);
//        TextView headerEmail = (TextView) lst_item_view.findViewById(R.id.headerEmail);
//        TextView headerDesc = (TextView) lst_item_view.findViewById(R.id.headerDesc);
//
//        headerName.setText(""+username);
//        headerEmail.setText(""+email);
//        if(pref.getSubsType().equalsIgnoreCase("onemonth")){
//            headerDesc.setText("Monthly Subscription");
//        }else if(pref.getSubsType().equalsIgnoreCase("oneyear")){
//            headerDesc.setText("Yearly Subscription");
//        }else{
//            headerDesc.setText("");
//        }
//
//
//
////        RecyclerView recyclerView = (RecyclerView) lst_item_view.findViewById(R.id.recycler_viewMenu);
////        recyclerView.setHasFixedSize(true);
////        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
////
////        LeftMenuAdapter mAdapter = new LeftMenuAdapter(activity, contacts);
////        recyclerView.setAdapter(mAdapter);
////
////        contacts = new Utility().getItemLeftMenuWithoutLoginUser(activity);
////        mAdapter.updateData(contacts);
//
//      //  final Drawer result = new DrawerBuilder().build();
//
//        final Drawer result = new DrawerBuilder()
//                //.withAccountHeader(lst_item_view)
//                .withActivity(activity)
//                .withHeaderPadding(true)
////                .withDrawerWidthPx((width/2)+((width/2)/2))
//             //   .withStickyHeader(lst_item_view)
//                .withHeader(lst_item_view)
//                .withTranslucentStatusBar(true)
//                .withStickyFooterDivider(false)
//                .withDisplayBelowStatusBar(true)
//                //.withDrawerGravity(Gravity.RIGHT)
//                //.withSliderBackgroundColorRes(R.color.sidemenublue)
//                .withSliderBackgroundDrawable(activity.getResources().getDrawable(R.drawable.side_menu_bg))
//                .withSelectedItem(-1)
//
//
//                .addDrawerItems(
//                        item.withSelectable(false).withTypeface(myfont),
//                        item2.withSelectable(false).withTypeface(myfont),
//                        item3.withSelectable(false).withTypeface(myfont),
//                        item4.withSelectable(false).withTypeface(myfont),
//                        item5.withSelectable(false).withTypeface(myfont),
//                        item6.withSelectable(false).withTypeface(myfont),
//                        item7.withSelectable(false).withTypeface(myfont),
//                        item8.withSelectable(false).withTypeface(myfont),
//                        item13.withSelectable(false).withTypeface(myfont),
//                        item14.withSelectable(false).withTypeface(myfont),
//                        item15.withSelectable(false).withTypeface(myfont),
//                        item16.withSelectable(false).withTypeface(myfont),
//                        item17.withSelectable(false).withTypeface(myfont),
//                        item18.withSelectable(false).withTypeface(myfont),
//                        item19.withSelectable(false).withTypeface(myfont),
//                        item20.withSelectable(false).withTypeface(myfont),
//                        item21.withSelectable(false).withTypeface(myfont),
//                        item22.withSelectable(false).withTypeface(myfont),
//                        item12.withSelectable(false).withTypeface(myfont),
//
//                        item9.withSelectable(false).withTypeface(myfont),
//                        item10.withSelectable(false).withTypeface(myfont)
//
//
//
//                 ).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
//
//                    //Actions to be performed after clicking drawer items
//                    @Override
//                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
//                        SharedPreferences preferences = activity.getSharedPreferences(Constant.PREF_BASE,MODE_PRIVATE);
//
//                        if(position==1)
//                        {
//                            if(preferences.getString(Constant.SUB_ADMIN,"").equalsIgnoreCase("1")){
//                                Intent intent = new Intent(activity, User_Activity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                activity.startActivity(intent);
//                            }else{
//                                createDialogOpenClass(activity);
//                            }
//                        }
//
//                        if(position==2)
//                        {
//                            if(preferences.getString(Constant.SUB_ADMIN,"").equalsIgnoreCase("1")){
//                                Intent intent = new Intent(activity, Companies_Activity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                activity.startActivity(intent);
//                            }else{
//                                createDialogOpenClass(activity);
//                            }
//
//                        }
//
//                        if(position==3)
//                        {
//                            if(preferences.getString(Constant.PRODUCT,"").equalsIgnoreCase("1")){
//                                Intent intent = new Intent(activity, Product_Activity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                activity.startActivity(intent);
//                            }else{
//                                createDialogOpenClass(activity);
//                            }
//                        }
//
//                        if(position==4) {
//                            if(preferences.getString(Constant.SERVICE,"").equalsIgnoreCase("1")){
//                                Intent intent = new Intent(activity, Service_Activity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                activity.startActivity(intent);
//                            }else{
//                                createDialogOpenClass(activity);
//                            }
//                        }
//
//                        if(position==5){
//                            if(preferences.getString(Constant.TAX,"").equalsIgnoreCase("1")){
//                                Intent intent = new Intent(activity, Tax_Activity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                activity.startActivity(intent);
//                            }else{
//                                createDialogOpenClass(activity);
//                            }
//                        }
//                        if(position==6)
//                        {
//                            if(preferences.getString(Constant.CUSTOMER,"").equalsIgnoreCase("1")){
//                                Intent intent = new Intent(activity, Customer_Activity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                activity.startActivity(intent);
//                            }else{
//                                createDialogOpenClass(activity);
//                            }
//                        }
//                        if(position==7)
//                        {
//                            if(preferences.getString(Constant.SUPPLIER,"").equalsIgnoreCase("1")){
//                                Intent intent = new Intent(activity, Vendor_Activity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                activity.startActivity(intent);
//                            }else{
//                                createDialogOpenClass(activity);
//                            }
//                        }
//                        if(position==8)
//                        {
//                            if(preferences.getString(Constant.STOCK,"").equalsIgnoreCase("1")){
//                                Intent intent = new Intent(activity, Stock_Activity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                activity.startActivity(intent);
//                            }else{
//                                createDialogOpenClass(activity);
//                            }
//                        }
//
//                        if (position==9)
//                        {
//                            if(preferences.getString(Constant.INVOICE,"").equalsIgnoreCase("1")){
//                                Intent intent = new Intent(activity, InvoiceActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                activity.startActivity(intent);
//                            }else{
//                                createDialogOpenClass(activity);
//                            }
//                        }
//
//                        if(position==10)
//                        {
//                            if(preferences.getString(Constant.ESTIMATE,"").equalsIgnoreCase("1")){
//                                Intent intent = new Intent(activity, EstimateActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                activity.startActivity(intent);
//                            }else{
//                                createDialogOpenClass(activity);
//                            }
//                        }
//                        if(position==11)
//                        {
//                            if(preferences.getString(Constant.PURCHASE_ORDER,"").equalsIgnoreCase("1")){
//                                Intent intent = new Intent(activity, POActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                activity.startActivity(intent);
//                            }else{
//                                createDialogOpenClass(activity);
//                            }
//                        }
//                        if(position==12)
//                        {
//                            if(preferences.getString(Constant.RECEIPT,"").equalsIgnoreCase("1")){
//                                Intent intent = new Intent(activity, ReceiptsActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                activity.startActivity(intent);
//                            }else{
//                                createDialogOpenClass(activity);
//                            }
//                        }
//                        if(position==13)
//                        {
//                            if(preferences.getString(Constant.PAYMENT_VOUCHER,"").equalsIgnoreCase("1")){
//                                Intent intent = new Intent(activity, PVActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                activity.startActivity(intent);
//                            }else{
//                                createDialogOpenClass(activity);
//                            }
//                        }
//                        if(position==14)
//                        {
//                            Intent intent = new Intent(activity, ReportActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            activity.startActivity(intent);
//                        }
//                        if(position==15)
//                        {
//                            if(preferences.getString(Constant.CREDIT_NOTE,"").equalsIgnoreCase("1")){
//                                Intent intent = new Intent(activity, CreditNotesActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                activity.startActivity(intent);
//                            }else{
//                                createDialogOpenClass(activity);
//                            }
//
//                        }
//                        if(position==16)
//                        {
//                            if(preferences.getString(Constant.DEBIT_NOTE,"").equalsIgnoreCase("1")){
//                                Intent intent = new Intent(activity, DebitNotesActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                activity.startActivity(intent);
//                            }else{
//                                createDialogOpenClass(activity);
//                            }
//                        }
//
//                        if(position==17)
//                        {
//                            Intent intent = new Intent(activity, ThankYouNoteActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            activity.startActivity(intent);
//                        }
//
//                        if(position==18)
//                        {
//                            Intent intent = new Intent(activity, InvoiceReminderActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            activity.startActivity(intent);
//                        }
//
//                        if(position==20)
//                        {
//                            Intent intent = new Intent(activity, SettingsActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            activity.startActivity(intent);
//                        }
//
//
//                        if(position==21)
//                        {
//
//                            preferences.edit().remove(Constant.LOGGED_IN).commit();
//                            preferences.edit().remove(Constant.INVOICE).commit();
//                            preferences.edit().remove(Constant.ESTIMATE).commit();
//                            preferences.edit().remove(Constant.STOCK).commit();
//                            preferences.edit().remove(Constant.RECEIPT).commit();
//                            preferences.edit().remove(Constant.PURCHASE_ORDER).commit();
//                            preferences.edit().remove(Constant.PAYMENT_VOUCHER).commit();
//                            preferences.edit().remove(Constant.TAX).commit();
//                            preferences.edit().remove(Constant.CUSTOMER).commit();
//                            preferences.edit().remove(Constant.SUPPLIER).commit();
//                            preferences.edit().remove(Constant.PRODUCT).commit();
//                            preferences.edit().remove(Constant.SERVICE).commit();
//                            preferences.edit().remove(Constant.DEBIT_NOTE).commit();
//                            preferences.edit().remove(Constant.CREDIT_NOTE).commit();
//                            preferences.edit().remove(Constant.SUB_ADMIN).commit();
//                            preferences.edit().remove(Constant.ACCESS_TOKEN).commit();
//                            preferences.edit().remove(Constant.FULLNAME).commit();
//                            preferences.edit().remove(Constant.EMAIL).commit();
//                            SavePref pref = new SavePref();
//                            pref.SavePref(activity);
//                            pref.setSubsType("");
//                            pref.setNumberFormatPosition(0);
//                            pref.setLanguagePosition(0);
//
//                            Context context = LocaleHelper.setLocale(activity, "en");
//                            Locale myLocale = new Locale("en");
//                            Resources res = context.getResources();
//                            DisplayMetrics dm = res.getDisplayMetrics();
//                            Configuration conf = res.getConfiguration();
//                            conf.locale = myLocale;
//                            res.updateConfiguration(conf, dm);
//
//
//
//                            Intent intent = new Intent(activity, Signin_Activity.class);
//                            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            activity.startActivity(intent);
//                            activity.finishAffinity();
//                            activity.finish();
//
//                        }
//
//
//                        return false;
//                    }
//                })
//
//                .build();
//
////                result.getDrawerLayout().setFitsSystemWindows(false);
////                result.getSlider().setFitsSystemWindows(false);
//


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

//            ham.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (result.isDrawerOpen()) {
//
//                        result.closeDrawer();
//                    } else {
//                        result.openDrawer();
//                    }
//                }
//            });




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

        final SharedPreferences pref = activity.getSharedPreferences(Constant.PREF_BASE,MODE_PRIVATE);

       // ImageView addproperty = activity.findViewById(R.id.addprop);
//
//        AHBottomNavigation bottomNavigation = (AHBottomNavigation) activity.findViewById(R.id.bottom_navigation);
//        AHBottomNavigationItem item1 = new AHBottomNavigationItem(activity.getString(R.string.home), R.drawable.nav_home_2, R.color.white);
//        AHBottomNavigationItem item2 = new AHBottomNavigationItem(activity.getString(R.string.estimate), R.drawable.nav_estimate_2, R.color.white);
//        AHBottomNavigationItem item3 = new AHBottomNavigationItem(activity.getString(R.string.invoices), R.drawable.nav_invoice_2, R.color.white);
//        AHBottomNavigationItem item4 = new AHBottomNavigationItem(activity.getString(R.string.receipts), R.drawable.nav_receipt_2, R.color.white);
//        AHBottomNavigationItem item5 = new AHBottomNavigationItem(activity.getString(R.string.po), R.drawable.nav_po_2, R.color.white);
//        bottomNavigation.addItem(item1);
//        bottomNavigation.addItem(item2);
//        bottomNavigation.addItem(item3);
//        bottomNavigation.addItem(item4);
//        bottomNavigation.addItem(item5);
//
//        bottomNavigation.setTitleTextSizeInSp (10, 10);
//
//
//
////        addproperty.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////
////                Intent intent = new Intent(activity, Create_Invoice_Activity.class);
////                activity.startActivity(intent);
////
////            }
////        });
//
//
//
//
//        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
//            @Override
//            public boolean onTabSelected(int position, boolean wasSelected) {
//                if(position==0){
//                    Intent intent = new Intent(activity, Home_Activity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    activity.startActivity(intent);
//                }
//                else if(position==1){
//                    if(pref.getString(Constant.ESTIMATE,"").equalsIgnoreCase("1")){
//                        Intent intent = new Intent(activity, EstimateActivity.class);
//                        activity.startActivity(intent);
//                    }else{
//                        createDialogOpenClass(activity);
//                    }
//                }
//                else if(position==2){
//                    if(pref.getString(Constant.INVOICE,"").equalsIgnoreCase("1")){
//                        Intent intent = new Intent(activity, InvoiceActivity.class);
//                        activity.startActivity(intent);
//                    }else{
//                        createDialogOpenClass(activity);
//                    }
//                }
//                else if(position==3){
//                    if(pref.getString(Constant.RECEIPT,"").equalsIgnoreCase("1")){
//                        Intent intent = new Intent(activity, ReceiptsActivity.class);
//                        activity.startActivity(intent);
//                    }else{
//                        createDialogOpenClass(activity);
//                    }
//                }else if(position==4){
//                    if(pref.getString(Constant.PURCHASE_ORDER,"").equalsIgnoreCase("1")){
//                        Intent intent = new Intent(activity, POActivity.class);
//                        activity.startActivity(intent);
//                    }else{
//                        createDialogOpenClass(activity);
//                    }
//                }
//
//                return false;
//            }
//        });
//
//        //bottomNavigation.setDefaultBackgroundResource(R.drawable.bottomnavlayout);
//    /*   bottomNavigation.setBehaviorTranslationEnabled(false);
//       bottomNavigation.setAccentColor(R.color.red);
//       bottomNavigation.setInactiveColor(Color.parseColor("#c5c5c5"));*/
//        //bottomNavigation.setInactiveColor(Color.parseColor("#ffffff"));
//        bottomNavigation.setCurrentItem(position, false);
//        bottomNavigation.setBehaviorTranslationEnabled(false);
//        bottomNavigation.setAccentColor(Color.parseColor("#1B60FB"));
//        bottomNavigation.setInactiveColor(Color.parseColor("#6695FF"));
//        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);


        LinearLayout linearLayout1 = activity.findViewById(R.id.tab1);
        LinearLayout linearLayout2 = activity.findViewById(R.id.tab2);
        LinearLayout linearLayout3 = activity.findViewById(R.id.tab3);
        LinearLayout linearLayout4 = activity.findViewById(R.id.tab4);
        LinearLayout linearLayout5 = activity.findViewById(R.id.tab5);

        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Home_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }
        });

        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pref.getString(Constant.ESTIMATE,"").equalsIgnoreCase("1")){
                    Intent intent = new Intent(activity, EstimateActivity.class);
                    activity.startActivity(intent);
                }else{
                    createDialogOpenClass(activity);
                }
            }
        });

        linearLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pref.getString(Constant.INVOICE,"").equalsIgnoreCase("1")){
                    Intent intent = new Intent(activity, InvoiceActivity.class);
                    activity.startActivity(intent);
                }else{
                    createDialogOpenClass(activity);
                }
            }
        });

        linearLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pref.getString(Constant.RECEIPT,"").equalsIgnoreCase("1")){
                    Intent intent = new Intent(activity, ReceiptsActivity.class);
                    activity.startActivity(intent);
                }else{
                    createDialogOpenClass(activity);
                }
            }
        });

        linearLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pref.getString(Constant.PURCHASE_ORDER,"").equalsIgnoreCase("1")){
                    Intent intent = new Intent(activity, POActivity.class);
                    activity.startActivity(intent);
                }else{
                    createDialogOpenClass(activity);
                }
            }
        });
    }




    public static void SuccessToast(Activity context,String message){
        if(context != null){
            Utility.hideKeypad(context);
            new Flashbar.Builder(context)
                    .gravity(Flashbar.Gravity.BOTTOM)
                    .title(context.getString(R.string.dialog_Success))
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

    }

    public static void ErrorToast(Activity context,String message){
        if(context != null){
            Utility.hideKeypad(context);

            new Flashbar.Builder(context)
                    .gravity(Flashbar.Gravity.BOTTOM)
                    .title(context.getString(R.string.error))
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

    }



    public static void ErrorToastTop(Activity context,String message){
        if(context != null){
            Utility.hideKeypad(context);

            new Flashbar.Builder(context)
                    .gravity(Flashbar.Gravity.TOP)
                    .title(context.getString(R.string.error))
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

    }
    public static String GetSharedPreferences(Context context, String key){
        String val="";
        if(context != null){
            SharedPreferences preferences = context.getSharedPreferences(Constant.PREF_BASE,Context.MODE_PRIVATE);
            if (preferences.contains(key)){
                val = preferences.getString(key,"");
            }
        }
        return val;
    }




    public static void createDialogOpenClass(final Activity splash) {
        // TODO Auto-generated method stub

        InputMethodManager imm = (InputMethodManager) splash.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = splash.getCurrentFocus();
        if (view == null) {
            view = new View(splash);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


        splash.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
//				 Builder dialog = new Builder(splash, AlertDialog.THEME_HOLO_LIGHT);
//				 dialog.setTitle("Brain At Work");
//				 dialog.setMessage(string);
//				 dialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						dialog.dismiss();
//					}
//				});
//				 dialog.show();


                AlertDialog.Builder builder = new AlertDialog.Builder(splash);
                builder.setTitle(splash.getString(R.string.permission_title));
                builder.setMessage(splash.getString(R.string.permission_message))
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
//				.setNegativeButton("No", new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int id) {
//						dialog.cancel();
//					}
//				});
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }








    public static class LeftMenuAdapter extends RecyclerView.Adapter<com.sirapp.Home.LeftMenuAdapter.ViewHolder> {
        private SparseBooleanArray sSelectedItems;
        private int row_index = -1 ;
        private static final String TAG = "LeftMenuAdapter";
        ArrayList<ItemLeftMenu> alName = new ArrayList<ItemLeftMenu>();
        Activity activity;

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
        public com.sirapp.Home.LeftMenuAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
            final View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.left_menu_item, viewGroup, false);
            com.sirapp.Home.LeftMenuAdapter.ViewHolder viewHolder = new com.sirapp.Home.LeftMenuAdapter.ViewHolder(v);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(final com.sirapp.Home.LeftMenuAdapter.ViewHolder viewHolder, final int position) {
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


//                fff.c

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

        public class ViewHolder extends RecyclerView.ViewHolder{
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




}
