package com.sirapp.Invoice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.sirapp.BuildConfig;
import com.sirapp.Constant.Constant;
import com.sirapp.Adapter.CustomViewPagerAdapter;
import com.sirapp.Base.BaseActivity;
import com.sirapp.Home.GoProActivity;
import com.sirapp.R;
import com.sirapp.Utils.LockableViewPager;
import com.sirapp.Utils.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class InvoiceActivity extends BaseActivity {

    public static Activity activity;

    ImageView imageViewShare;
    private static final String TAG = "Create_Invoice_Activity";
    LockableViewPager viewPager;
    TabLayout tabs;
    public static Context contextOfApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__invoice_);
        //Constant.bottomNav(Create_Invoice_Activity.this,1);

        overridePendingTransition(R.anim.flip_out,R.anim.flip_in);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Constant.toolbar(InvoiceActivity.this,getString(R.string.header_invoices));

        activity = this;
        //pref.setCustomerName("");


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        viewPager = findViewById(R.id.viewPagerLayout);
        tabs = findViewById(R.id.tabs);

        setUpViewPager(viewPager);

        tabs.setupWithViewPager(viewPager);

        imageViewShare = findViewById(R.id.shareButton);
        imageViewShare.setVisibility(View.VISIBLE);

        TextView customers = (TextView) LayoutInflater.from(this).inflate(R.layout.tabview, null);
        TextView addcustomer = (TextView) LayoutInflater.from(this).inflate(R.layout.tabview, null);

        customers.setTextColor(getResources().getColor(R.color.lightpurple));
        addcustomer.setTextColor(getResources().getColor(R.color.lightpurple));
        addcustomer.setText(getString(R.string.header_invoices_list));
        customers.setText(getString(R.string.header_invoices_create));

        customers.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Regular.otf"));
        addcustomer.setTypeface(Typeface.createFromAsset(getAssets(),"Fonts/AzoSans-Regular.otf"));

        tabs.getTabAt(1).setCustomView(customers);
        tabs.getTabAt(0).setCustomView(addcustomer);

        if (getIntent().hasExtra("key")){
            viewPager.setCurrentItem(1);
        }

        if (getIntent().hasExtra("add")){
            viewPager.setCurrentItem(1);
            viewPager.setOffscreenPageLimit(1);
        }

        viewPager.setSwipeable(false);


        imageViewShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
//                        .setLink(Uri.parse("https://www.sirproject.page.link/"))
                        .setLink(Uri.parse(buildDeepLink()))
                        .setDomainUriPrefix("https://sirproject.page.link/")
//                        .setDomainUriPrefix("https://sirproject.page.link/subscription")
                        .setAndroidParameters(
                                new DynamicLink.AndroidParameters.Builder("com.sirapp")
                                        .setFallbackUrl(Uri.parse("https://www.sirproject.page.link/"))
                                        .setMinimumVersion(1)
                                        .build())
                        .buildDynamicLink();

                Uri dynamicLinkUri = dynamicLink.getUri();

                Log.e(TAG, "dynamicLinkUri "+dynamicLinkUri);

                shareDynamicLink(dynamicLinkUri);
            }
        });
    }

    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }
   private void setUpViewPager(ViewPager pager) {

       CustomViewPagerAdapter adapter = new CustomViewPagerAdapter(getSupportFragmentManager());
       adapter.addFragment(new  List_of_Invoices(), getString(R.string.header_invoices_list));
       adapter.addFragment(new Fragment_Create_Invoice(), getString(R.string.header_invoices_create));
       pager.setAdapter(adapter);



   }







    public String buildDeepLink() {
        // Get the unique appcode for this app.
        String appCode = getString(R.string.app_name);

        // Get this app's package name.
        String packageName = getPackageName();

        // Build the link with all required parameters
        Uri.Builder builder = new Uri.Builder()
                .scheme("https")
                .authority("sirproject.page.link")
                .path("/invoice")
                .appendQueryParameter("invoiceID", "invoice")
//                .appendQueryParameter("apn", packageName)
                ;

        // Return the completed deep link.
        return builder.build().toString();
    }

    public void shareDynamicLink(Uri dynamicLink)
    {
        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(dynamicLink)
                .buildShortDynamicLink()
                .addOnCompleteListener(Objects.requireNonNull(InvoiceActivity.this), new OnCompleteListener<ShortDynamicLink>()
                {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task)
                    {
                        if (task.isSuccessful())
                        {
                            // Short link created
                            Uri shortLink = Objects.requireNonNull(task.getResult()).getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();

                            Log.e("DynamicLink", "shortLink: " + shortLink + System.lineSeparator());
                            Log.e("DynamicLink", "flowChartLink: " + flowchartLink + System.lineSeparator());

//                            Intent shareIntent = new Intent();
//                            String msg = "Check this out: " + shortLink;
//                            shareIntent.setAction(Intent.ACTION_SEND);
//                            shareIntent.putExtra(Intent.EXTRA_TEXT, msg);
//                            shareIntent.setType("text/plain");
//                            startActivity(shareIntent);

                            Intent intentShareFile = new Intent(Intent.ACTION_SEND_MULTIPLE);

                            File mFile2 = new File("/sdcard/share.jpg");
                            Uri imageUri2 = FileProvider.getUriForFile(
                                    InvoiceActivity.this,
                                    BuildConfig.APPLICATION_ID + ".provider",
                                    mFile2);

                            ArrayList<Uri> uriArrayList = new ArrayList<>();
                            if (mFile2.exists()) {
                                uriArrayList.add(imageUri2);
                            }

                            intentShareFile.setType("application/pdf/*|image/*");
                            intentShareFile.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriArrayList);

                            intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "Sir-app");

                            String allData = "Sir-app" + "\n\n"+shortLink;

                            intentShareFile.putExtra(Intent.EXTRA_TEXT, allData);

                            if (Utility.isAppAvailable(InvoiceActivity.this, "com.samsung.android.email.provider")) {
                                intentShareFile.setPackage("com.samsung.android.email.provider");
                            }else if (Utility.isAppAvailable(InvoiceActivity.this, "com.google.android.gm")) {
                                    intentShareFile.setPackage("com.google.android.gm");
                            }
                            startActivity(intentShareFile);

                        }
                        else
                        {
                            Toast.makeText(InvoiceActivity.this, "Failed to share event.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        Log.e(TAG, "requestCode Path"+requestCode);
//        Log.e(TAG, "resultCode Path"+resultCode);

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

}






