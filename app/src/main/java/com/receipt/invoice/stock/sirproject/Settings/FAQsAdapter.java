package com.receipt.invoice.stock.sirproject.Settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.receipt.invoice.stock.sirproject.R;

import java.util.ArrayList;

public class FAQsAdapter extends RecyclerView.Adapter<FAQsAdapter.ViewHolderForCat> {
    private static final String TAG = "FAQsAdapter";

    //InvoiceCallBack invoiceCallBack;

    private Context mcontext;

    ArrayList<String> arrayListNames = new ArrayList<>();

    private int selectedPos = -1;

    LanguageActivity activity;

    public FAQsAdapter(Context mcontext, ArrayList<String> list) {
        this.mcontext = mcontext;
        arrayListNames = list;
    }




    @NonNull
    @Override
    public FAQsAdapter.ViewHolderForCat onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myitem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_faqs, viewGroup, false);
        FAQsAdapter.ViewHolderForCat viewHolderForCat = new FAQsAdapter.ViewHolderForCat(myitem);
        return viewHolderForCat;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final FAQsAdapter.ViewHolderForCat viewHolderForCat, final int i) {
        viewHolderForCat.textViewHeader.setText(""+arrayListNames.get(i));

        viewHolderForCat.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolderForCat.nestedScrollView.getVisibility() == View.GONE) {
                    viewHolderForCat.nestedScrollView.setVisibility(View.VISIBLE);
                    viewHolderForCat.imageViewCheck.setImageResource(R.drawable.directional26);
                }else{
                    viewHolderForCat.nestedScrollView.setVisibility(View.GONE);
                    viewHolderForCat.imageViewCheck.setImageResource(R.drawable.directional25);
                }

                notifyDataSetChanged();
                setFoterData(i, viewHolderForCat.textViewName);

            }
        });



    }

    private void setFoterData(int position, TextView textView) {
        if(position == 0){
            textView.setText("To get started, click on sign up here and create an account. Then log in with your credentials. \n" +
                    "First, you need to create a company from My companies module. Enter as much details, logo, payment details, etc.; as required to display on Invoices and other documents. Then create products, services, taxes, customers, suppliers from respective modules in the side menu. You can edit any of these details at anytime. This is in accordance with the controlling feature of the app so the users can track any activity / inventory at anytime and have all available data stored within the app for future reference/ reporting.\n" +
                    "Once above is done, then you can create invoices, estimates, purchase orders, receipts, payment vouchers from respective modules in side menu or tray icons in the bottom of home screen within seconds.\n");
        }else if(position == 1){
            textView.setText("If you add new companies, customers, suppliers, products, items and taxes directly from Create Invoice or other pages, then you need to refresh by selecting the company name again on top, while creating invoice or other documents, to display the newly created data.");
        }else if(position == 2){
            textView.setText("To add new users, go to add new users module from the side menu, then click on add users on the top right, then enter the details and select single / multiple access rights to be granted, then click on add user. The new user added can access the app through the username and password entered while adding user. The new user can reset password from the login screen by clicking on forgot password and following the steps thereto.");
        }else if(position == 3){
            textView.setText("To add a warehouse, go to My companies module from the side menu, then click on the company in which warehouse should be added, then click on add warehouse from top right, then enter the details and click on save and continue.");
        }else if(position == 4){
            textView.setText("To track quantity in a particular warehouse, go to Stocks module from the side menu, then select the company, then click on any product which will display the quantity available in each warehouse.");
        }else if(position == 5){
            textView.setText("To record opening stock of items, go to Stocks module then select update stock and record the transaction.");
        }else if(position == 6){
            textView.setText("To record additional items found during stock taking, go to Stocks module then select update stock and then record the items. To remove missing items during stock taking, go to Stocks module then select wastage / damage and then record the missing items.");
        }else if(position == 7){
            textView.setText("To move stock between warehouses, go to Stocks module from the main menu then select stock movement and then record the transaction.");
        }else if(position == 8){
            textView.setText("When an issued purchase order is marked as delivery received, stock items in the purchase order will be automatically added to the inventory stock. When a sales invoice is issued, stock items in the sales invoice will be automatically reduced from the inventory stock.");
        }else if(position == 9){
            textView.setText("Stock value is calculated as per the product rate entered while creating product in My Products module.");
        }else if(position == 10){
            textView.setText("To create an Invoice, Go to Invoices Module from the side menu or from the tray icon, then click on create invoice at top right, then select company and enter the relevant details, then preview invoice if required by clicking on more icon next to create invoice, then click on create invoice, your Invoice will be created and can be viewed / shared from summary page of Invoices module.");
        }else if(position == 11){
            textView.setText("Warehouse should be selected only when product is selected to enable inventory tracking. If you select an item, do not select warehouse.");
        }else if(position == 12){
            textView.setText("If u want to track Inventory for your product, create a product under My products module. You also need to create a warehouse under My companies module to enable Stock tracking. If you do not want to track Inventory, create an item under My Items module. Quantity under Items module is not tracked. ");
        }else if(position == 13){
            textView.setText("To perform any of the above functions, go to Invoices, then select the company, then go to the specific Invoice and swipe left, then click on more and select your preference.");
        }else if(position == 14){
            textView.setText("While creating the company, you need to select color as per preference or matching your brand identity. The color selected will be applied to the different templates in Invoices / Estimates. Select any template as per your choice while creating Invoice / Estimate. If no color has been selected, all templates will display the standard format.");
        }else if(position == 15){
            textView.setText("If warehouse selected does not have sufficient quantity for invoicing, then you will have to first add sufficient quantity to the particular warehouse. Go to Stocks module from the side menu, then you can either add quantity from update stock option or move stock from one warehouse to another from stock movement option, on the top.");
        }else if(position == 16){
            textView.setText("To perform any of the above functions, go to Invoices, then select the company, then go to the specific Invoice and Swipe left, then click on more and select your preference.");
        }else if(position == 17){
            textView.setText("Invoice seen status can be checked only if invoice is shared as a link. When the link is opened by the receiver, the status will automatically change from pending to seen in Invoice module Summary. If the invoice is shared only in PDF format, then seen status cannot be determined and will show as pending.");
        }else if(position == 18){
            textView.setText("To mark invoices as paid, go to Invoices module from side menu, then select company, then go to the invoice from the list which needs to be marked as paid and Swipe right, then click on mark as paid. If an invoice is marked as paid then a receipt should be created to reflect in customer statement of account. Similarly, if a purchase order is paid then a payment voucher should be created to reflect in supplier statement of account.");
        }else if(position == 19){
            textView.setText("First go to invoices summary, swipe the particular invoice right and Mark as paid. Then swipe the invoice left, under more, click on Convert to Receipt then click create receipt.");
        }else if(position == 20){
            textView.setText("Mark Purchase order as delivery received to add quantity to inventory. To mark purchase order as delivery received, go to Purchase orders module from the side menu and select the company, then go to the specific purchase order, then swipe right and click on Mark as delivery received.");
        }else if(position == 21){
            textView.setText("To delete an invoice, you need to create a credit note. To delete a Purchase order, you need to create a debit note. Since items in invoice / purchase order are linked to inventory they cannot be deleted directly.\n" +
                    "Alternatively, you can mark the particular Invoice / Purchase order as void, then it will not display in the relevant reports. If you are tracking products inventory, then after marking void, you need to go to Stocks module and add / delete the quantity in void Invoice / Purchase order through update stocks or wastage option.\n");
        }else if(position == 22){
            textView.setText("To mark as void, go to particular invoice / purchase order, swipe left then click on more then select Mark as Void. ");
        }else if(position == 23){
            textView.setText("To create a debit note, go to debit notes from the side menu, then click on create debit notes on top right, then record the transaction and click on create debit note on the bottom. Quantity entered in the debit note will be reduced from the Stock inventory balance.\n" +
                    "To create a Credit note, go to credit notes from the side menu, then click on create credit notes on top right, then record the transaction and click on create credit note on the bottom. Quantity entered in the credit note will be added back to the Stock inventory balance.\n");
        }else if(position == 24){
            textView.setText("Details of the first company created by the user will display on home screen by default. To switch companies, click on the star icon on top right on home screen then select the required company. The company selected shall display on home screen as and when required.");
        }else if(position == 25){
            textView.setText("It takes a few minutes to set up with Stripe or PayPal. Simply enable online payments in your app and follow the instructions.");
        }else if(position == 26){
            textView.setText("There is no upfront or monthly fee. There is a charge per transaction, which varies by country. To check the charges in your country, please visit Stripe pricing or https://www.paypal.com/us/webapps/mpp/merchant-fees");
        }else if(position == 27){
            textView.setText("To change comma format in numbers, go to settings then click on comma format selection then select your preference. Format selected will be applicable throughout the user account.");
        }else if(position == 28){
            textView.setText("To share an invoice with payment links, first you need to set up online payment gateways from settings. Then, while creating invoice, select the respective payment gateway you wish to get paid through. You can select both options as well. Then share invoice as link only. The payment links will be automatically displayed in the email.");
        }else if(position == 29){
            textView.setText("If an invoice has been paid through the shared payment links, then particular invoice will display \"Paid\" in Invoices summary. Alternatively, you will be notified by the respective payment gateway platform.");
        }else if(position == 30){
            textView.setText("Your first payment will take 7 business days and all subsequent payments will take between 2-7 business days to reach your account. You can check the estimated arrival date of your payment using your Stripe / PayPal dashboard.");
        }else if(position == 31){
            textView.setText("SIR handles card payments through Stripe, a market leader in processing payments. Setting up with Stripe is easy and there are No set up costs. You only pay per transaction. For more details, visit the Stripe pricing page.\n" +
                    "On Stripe dashboard you can have complete visibility of all your transactions including card payments, pending payments and fees.\n");
        }else if(position == 32){
            textView.setText("To switch / restore / upgrade a subscription, go to Settings then click on Subscription and select your preference accordingly. Terms and conditions apply.");
        }else if(position == 33){
            textView.setText("To switch language, go to settings then click on language and select your preference. ");
        }else if(position == 34){
            textView.setText("All your data is totally safe and secure on our Cloud based server.");
        }else if(position == 35){
            textView.setText("If your query is not listed here, contact us via support feature in settings and we shall get back to you as soon as possible.");
        }
    }


    @Override
    public int getItemCount() {
        return arrayListNames.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateLanguagePosition(int languagePostion) {
        selectedPos = languagePostion;
        notifyDataSetChanged();
    }
    //end for removing redundancy

    public class ViewHolderForCat extends RecyclerView.ViewHolder {


        ImageView imageViewCheck;
        TextView textViewHeader ;
        TextView textViewName ;
        View view;
        NestedScrollView nestedScrollView;

        public ViewHolderForCat(@NonNull View itemView) {
            super(itemView);

            textViewHeader = itemView.findViewById(R.id.text_setting);
            textViewName = itemView.findViewById(R.id.name);
            imageViewCheck = itemView.findViewById(R.id.imageView);
            nestedScrollView = itemView.findViewById(R.id.nestedScroll);

            view = itemView;


        }

    }
    public void updateList(ArrayList<String> list){
        arrayListNames = list;
        notifyDataSetChanged();
    }
}