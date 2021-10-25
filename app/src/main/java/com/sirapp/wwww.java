package com.sirapp;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.gson.Gson;
import com.sirapp.Base.BaseActivity;
import com.sirapp.Utils.Utility;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class wwww extends BaseActivity implements PurchasesUpdatedListener,
        BillingClientStateListener, SkuDetailsResponseListener {

    private String TAG = "wwww";
    private String DEEP_LINK_URL = "https://sirproject.page.link";

    Button button;
    WebView webView;

    BillingClient billingClient;

    public  void adjustFontScale( Configuration configuration,float scale) {

        configuration.fontScale = scale;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        getBaseContext().getResources().updateConfiguration(configuration, metrics);

    }


    private PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
        @Override
        public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
            // To be implemented in a later section.
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abc);

        button = (Button) findViewById(R.id.button2);
        webView = findViewById(R.id.invoiceweb);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);



       // webView.setInitialScale(20);
        String name = "debit.html";
        String nameName = "file:///android_asset/debit.html";

        try {
            String content = IOUtils.toString(getAssets().open(name));
            webView.loadDataWithBaseURL(nameName, content, "text/html", "UTF-8", null);

        }catch (Exception e){

        }



        billingClient = BillingClient.newBuilder(wwww.this).setListener(
                this).enablePendingPurchases().build();
        billingClient.startConnection(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                billingClient.startConnection(new BillingClientStateListener() {
                    @Override
                    public void onBillingSetupFinished(BillingResult billingResult) {

                        if (billingResult.getResponseCode() ==  BillingClient.BillingResponseCode.OK) {
                            // The BillingClient is ready. You can query purchases here.
                            Log.e(TAG, "onBillingSetupFinished");

                        }
                    }
                    @Override
                    public void onBillingServiceDisconnected() {
                        // Try to restart the connection on the next request to
                        // Google Play by calling the startConnection() method.
                        Log.e(TAG, "onBillingServiceDisconnected");
                    }
                });

                List<String> skuList = new ArrayList<> ();
                skuList.add("android.test.purchased");
//                skuList.add("com.sir.oneyear");
//                skuList.add("com.sirapp.onemonth");
              //  skuList.add("gas");
                SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
//                params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
                billingClient.querySkuDetailsAsync(params.build(),
                        new SkuDetailsResponseListener() {
                            @Override
                            public void onSkuDetailsResponse(BillingResult billingResult,
                                                             List<SkuDetails> skuDetailsList) {

//                                String ddd = new Gson().toJson(skuDetailsList);
//                                Log.e(TAG, "onSkuDetailsResponse "+ddd);
//                                if(skuDetailsList != null){
//                                    for(int i = 0 ; i < skuDetailsList.size() ; i++){
//                                        if(skuDetailsList.get(i).getSku().equals("com.sir.oneyear")){
//                                            Log.e(TAG, "skuDetailsList "+skuDetailsList.get(i).getSku());
//                                        }
//                                    }
//                                }

                                // Process the result.
                                String ddd = new Gson().toJson(skuDetailsList);
                                Log.e(TAG, "onSkuDetailsResponse "+ddd);
                                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                .setSkuDetails(skuDetailsList.get(0))
                                .build();

                                int responseCode = billingClient.launchBillingFlow(wwww.this, billingFlowParams).getResponseCode();
                                Log.e(TAG, "responseCode "+responseCode);
                            }
                        });

//                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
//                        .setSkuDetails(skuDetails)
//                        .build();
//                int responseCode = billingClient.launchBillingFlow(activity, billingFlowParams).getResponseCode();

                //adjustFontScale(getResources().getConfiguration(),2.0f);

              //  Settings.System.putFloat(getBaseContext().getContentResolver(), Settings.System.FONT_SCALE, (float) 1.0);

//                Configuration configuration = wwww.this.getResources().getConfiguration();
//                // This will apply to all text like -> Your given text size * fontScale
//                configuration.fontScale = 2.0f;
//
//                createConfigurationContext(configuration);




                float scaleAA = getResources().getConfiguration().fontScale;
                Log.e(TAG, "manufacturerModelscaleAA :" + scaleAA);

//                PrintManager printManager = (PrintManager) primaryBaseActivity.getSystemService(Context.PRINT_SERVICE);
//
//                PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();
//
//                //provide name to your newly generated pdf file
//                String jobName = getString(R.string.app_name) + " Print Test";
//
//                PrintAttributes.Builder builder = new PrintAttributes.Builder();
//                builder.setMediaSize( PrintAttributes.MediaSize.ISO_A4);
//             //   builder.setResolution(new PrintAttributes.Resolution("zooey", PRINT_SERVICE,600,600));
//               // builder.setResolution(PrintAttributes.Resolution("pdf","pdf", 600, 600));
//               // builder.setMinMargins(PrintAttributes.Margins.NO_MARGINS);
//                builder.setMinMargins(new PrintAttributes.Margins(0, 0, 0, 0));
//                 printManager.print(jobName, printAdapter, builder.build());

                //generatePdf();
            }
        });



//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Uri newDeepLink = buildDeepLink(Uri.parse(DEEP_LINK_URL));
////
////                Log.e(TAG, "newDeepLink "+newDeepLink);
//
//                String content1 = "<!DOCTYPE html>\n" +
//                        "<html>\n" +
//                        "  <head>\n" +
//                        "    <title>Title of the document</title>\n" +
//                        "    <style>\n" +
//                        "      .button {\n" +
//                        "        background-color: #1c87c9;\n" +
//                        "        border: none;\n" +
//                        "        color: white;\n" +
//                        "        padding: 20px 34px;\n" +
//                        "        text-align: center;\n" +
//                        "        text-decoration: none;\n" +
//                        "        display: inline-block;\n" +
//                        "        font-size: 20px;\n" +
//                        "        margin: 4px 2px;\n" +
//                        "        cursor: pointer;\n" +
//                        "      }\n" +
//                        "    </style>\n" +
//                        "  </head>\n" +
//                        "  <body>\n" +
//                        "    <a href=\"" + "urlPaypal" + "\" class=\"button\">" + "linkWitch" + "</a>\n" +
//                        "  </body>\n" +
//                        "</html>";
//
//                //generateNoteOnSD(wwww.this , "track.html", ""+content1.toString());
//
////                BottomSheetDialog bottomSheetDialog;
////                bottomSheetDialog = new BottomSheetDialog(wwww.this);
////
////                View view = LayoutInflater.from(wwww.this).inflate(R.layout.aaaaa, null);
//////                BottomSheetBehavior behavior = BottomSheetBehavior.from(view);
////                BottomSheetBehavior.from(view)
////                        .setState(BottomSheetBehavior.STATE_EXPANDED);
////
////                bottomSheetDialog.setContentView(view);
////                bottomSheetDialog.show();
//
////                final Dialog mybuilder=new Dialog(wwww.this);
////                mybuilder.setContentView(R.layout.aaaaa);
////                mybuilder.show();
////                mybuilder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
////                Window window = mybuilder.getWindow();
////                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                // window.setBackgroundDrawableResource(R.color.transparent);
//
////                File mFile2 = new File("/sdcard/share.jpg");
////                Uri imageUri2 = FileProvider.getUriForFile(
////                        wwww.this,
////                        BuildConfig.APPLICATION_ID + ".provider",
////                        mFile2);
////
////                Intent intentShareFile = new Intent(Intent.ACTION_SEND_MULTIPLE);
////                ArrayList<Uri> uriArrayList = new ArrayList<>();
////                uriArrayList.add(imageUri2);
////                uriArrayList.add(imageUri2);
////                Collections.reverse(uriArrayList);
////
////                intentShareFile.setType("application/pdf/*|image/*|text/html");
////                intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "subject");
////                intentShareFile.putExtra(Intent.EXTRA_TEXT, "text2");
////                intentShareFile.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriArrayList);
////                intentShareFile.setPackage("com.google.android.gm");
////                Log.e(TAG, "paypalXX10 ");
////                try {
////                    startActivity(intentShareFile);
////                    Log.e(TAG, "paypalXX72 ");
////                } catch (Exception e) {
////                    Log.e(TAG, "paypalXX32 " + e.getMessage());
////                }
//
//
//                Intent intentShareFile = new Intent(Intent.ACTION_SEND);
//                ArrayList<Uri> uriArrayList = new ArrayList<>();
//                String MEDIA_PATH = new String(Environment.getExternalStorageDirectory() + "/SIR" );
////                File mFile22 = new File("/sdcard/Notes/PayPal.html");
////                uriArrayList.add(imageUri2);
////                uriArrayList.add(imageUri2);
////               // Collections.reverse(uriArrayList);
//
//                final File files = new File(MEDIA_PATH);
//
//                File list[] = files.listFiles();
//                for (int i = 0; i < list.length; i++) {
//                    Log.e(TAG, "listAA "+list[i]);
//                   // uriArrayList.add(list[i].getName());
//                   // File mFile2 = new File(list[i].getAbsoluteFile());
//                    Uri imageUri2 = FileProvider.getUriForFile(
//                            wwww.this,
//                            BuildConfig.APPLICATION_ID + ".provider",
//                            list[i].getAbsoluteFile());
//                    uriArrayList.add(imageUri2);
//                }
//
//
//              //  intentShareFile.setType("application/pdf/*|image/*|text/html");
//
////                shareIntent.putExtra(Intent.EXTRA_STREAM, pictureUri);
//
////                File mFile2 = new File("/sdcard/share.jpg");
////
////                Uri imageUri2 = FileProvider.getUriForFile(
////                        wwww.this,
////                        BuildConfig.APPLICATION_ID + ".provider",
////                        mFile2);
//
//                intentShareFile.putExtra(Intent.EXTRA_STREAM, uriArrayList);
////                intentShareFile.putExtra(Intent.EXTRA_STREAM, imageUri2);
//                intentShareFile.setType("*/*");
//                intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                intentShareFile.setPackage("com.google.android.gm");
//                startActivity(intentShareFile);
//
//
//
//
//
//
//            }
//
//
//
//
//        });
//





    }



    public void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            Log.e(TAG, "gpxfile "+gpxfile.toString());
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



   public Uri buildDeepLink(Uri deepLink) {
       DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
               .setLink(Uri.parse(deepLink.toString()))
               .setDomainUriPrefix("https://rastogi.page.link")
               // Open links with this app on Android
               .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("com.sirapp").build())
               // Open links with com.example.ios on iOS
               .setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build())

               .buildDynamicLink();

       Uri dynamicLinkUri = dynamicLink.getUri();

        return dynamicLinkUri;
    }


    public static String getMimeType(File file, Context context)
    {
        Uri uri = Uri.fromFile(file);
        ContentResolver cR = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = mime.getExtensionFromMimeType(cR.getType(uri));
        return type;
    }


    @TargetApi(19)
    private void generatePdf() {
        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        //builder.setColorMode(PrintAttributes.COLOR_MODE_COLOR);
        builder.setMediaSize(PrintAttributes.MediaSize.ISO_A4); // or ISO_A0
        builder.setMinMargins(PrintAttributes.Margins.NO_MARGINS);
        builder.setResolution(new PrintAttributes.Resolution("1", "label", 1080, 1080));
        PrintedPdfDocument document = new PrintedPdfDocument(this, builder.build());
        PdfDocument.Page page = document.startPage(1);
        View content = webView;
        content.draw(page.getCanvas());
        document.finishPage(page);
        try {
            File f = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "document.pdf");

           // File file = new File(getExternalFilesDir(null).getAbsolutePath(), "document.pdf");
            document.writeTo(new FileOutputStream(f));
        } catch (IOException e) {
            Log.e("cannot generate pdf", e.getMessage());
        }
        document.close();
    }

    @Override
    public void onBillingServiceDisconnected() {
        Log.e(TAG, "onBillingServiceDisconnected");
    }

    @Override
    public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
        Log.e(TAG, "onBillingSetupFinished");
    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {

        Log.e(TAG, "onPurchasesUpdated");

        switch (billingResult.getResponseCode()) {
            case BillingClient.BillingResponseCode.OK:
                if (null != list) {
                    Log.d(TAG, "PURCX");
                    String ddd = new Gson().toJson(list);
                    Log.e(TAG, "onSkuDetailsResponseXX "+ddd);
                   // processPurchaseList(list, null);

//
//                    Purchase purchase = list.get(0);
//
//                    String productID = purchase.getSkus();

                    return;
                } else {
                    Log.d(TAG, "Null Purchase List Returned from OK response!");
                }
                break;
            case BillingClient.BillingResponseCode.USER_CANCELED:
                Log.i(TAG, "onPurchasesUpdated: User canceled the purchase");
                break;
            case BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED:
                Log.i(TAG, "onPurchasesUpdated: The user already owns this item");
                break;
            case BillingClient.BillingResponseCode.DEVELOPER_ERROR:
                Log.e(TAG, "onPurchasesUpdated: Developer error means that Google Play " +
                        "does not recognize the configuration. If you are just getting started, " +
                        "make sure you have configured the application correctly in the " +
                        "Google Play Console. The SKU product ID must match and the APK you " +
                        "are using must be signed with release keys."
                );
                break;
            default:
                Log.d(TAG, "BillingResult [" + billingResult.getResponseCode() + "]: "
                        + billingResult.getDebugMessage());
        }

    }

    @Override
    public void onSkuDetailsResponse(@NonNull BillingResult billingResult, @Nullable List<SkuDetails> list) {
        Log.e(TAG, "onSkuDetailsResponse");
    }
}
