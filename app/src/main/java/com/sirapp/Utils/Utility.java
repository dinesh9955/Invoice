package com.sirapp.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.*;

import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.sirapp.Home.ItemLeftMenu;
import com.sirapp.Model.ItemQuantity;
import com.sirapp.Model.Product_list;
import com.sirapp.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.*;

/**
 * Created by AnaadIT on 3/9/2017.
 */

public class Utility {
    public static final int MY_PERMISSIONS_REQUEST = 123;
    private static final String TAG = "Utility";

//


    public static Boolean isAppAvailable(Context context, String appName) {
        PackageManager pm = context.getPackageManager();
        boolean isInstalled;
        try {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES);
            isInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            isInstalled = false;
        }
        return isInstalled;
    }


    public static String getRealValueInvoiceWithoutPlus(Context context, String sss) {
        String valueIs = "";

        if(sss.toString().length() > 0){
            Log.e(TAG, "truee ");
            Boolean flag = Character.isDigit(sss.charAt(sss.length() - 1));
            Log.e(TAG, "cccccc "+flag);
            if(flag == true){
                String str = sss;
                StringBuilder sb = new StringBuilder();
                for (int i = str.length() - 1; i >= 0; i --) {
                    char c = str.charAt(i);
                    if (Character.isDigit(c)) {
                        sb.insert(0, c);
                    } else {
                        break;
                    }
                }
                String result = sb.toString();

                Log.e(TAG, "ccccccresult "+result);
                valueIs = context.getString(R.string.share_Invoice)+" # "+result;
            }
        }

        return valueIs;
    }

    public static String getRealValueEstimateWithoutPlus(Context context, String sss) {
        String valueIs = "";

        if(sss.toString().length() > 0){
            Log.e(TAG, "truee ");
            Boolean flag = Character.isDigit(sss.charAt(sss.length() - 1));
            Log.e(TAG, "cccccc "+flag);
            if(flag == true){
                String str = sss;
                StringBuilder sb = new StringBuilder();
                for (int i = str.length() - 1; i >= 0; i --) {
                    char c = str.charAt(i);
                    if (Character.isDigit(c)) {
                        sb.insert(0, c);
                    } else {
                        break;
                    }
                }
                String result = sb.toString();

                Log.e(TAG, "ccccccresult "+result);
                valueIs = context.getString(R.string.share_Estimate)+" # "+result;
            }
        }

        return valueIs;
    }

    public static String getRealValueReceiptWithoutPlus(Context context, String sss) {
        String valueIs = "";

        if(sss.toString().length() > 0){
            Log.e(TAG, "truee ");
            Boolean flag = Character.isDigit(sss.charAt(sss.length() - 1));
            Log.e(TAG, "cccccc "+flag);
            if(flag == true){
                String str = sss;
                StringBuilder sb = new StringBuilder();
                for (int i = str.length() - 1; i >= 0; i --) {
                    char c = str.charAt(i);
                    if (Character.isDigit(c)) {
                        sb.insert(0, c);
                    } else {
                        break;
                    }
                }
                String result = sb.toString();

                Log.e(TAG, "ccccccresult "+result);
                valueIs = context.getString(R.string.share_Receipt)+" # "+result;
            }
        }

        return valueIs;
    }

    public static String getRealValueCreditNoteWithoutPlus(Context context, String sss) {
        String valueIs = "";

        if(sss.toString().length() > 0){
            Log.e(TAG, "truee ");
            Boolean flag = Character.isDigit(sss.charAt(sss.length() - 1));
            Log.e(TAG, "cccccc "+flag);
            if(flag == true){
                String str = sss;
                StringBuilder sb = new StringBuilder();
                for (int i = str.length() - 1; i >= 0; i --) {
                    char c = str.charAt(i);
                    if (Character.isDigit(c)) {
                        sb.insert(0, c);
                    } else {
                        break;
                    }
                }
                String result = sb.toString();

                Log.e(TAG, "ccccccresult "+result);
                valueIs = context.getString(R.string.share_CreditNote)+" # "+result;
            }
        }

        return valueIs;
    }

    public static String getRealValueDebitNoteWithoutPlus(Context context, String sss) {
        String valueIs = "";

        if(sss.toString().length() > 0){
            Log.e(TAG, "truee ");
            Boolean flag = Character.isDigit(sss.charAt(sss.length() - 1));
            Log.e(TAG, "cccccc "+flag);
            if(flag == true){
                String str = sss;
                StringBuilder sb = new StringBuilder();
                for (int i = str.length() - 1; i >= 0; i --) {
                    char c = str.charAt(i);
                    if (Character.isDigit(c)) {
                        sb.insert(0, c);
                    } else {
                        break;
                    }
                }
                String result = sb.toString();

                Log.e(TAG, "ccccccresult "+result);
                valueIs = context.getString(R.string.share_DebitNote)+" # "+result;
            }
        }

        return valueIs;
    }

    public static String getRealValuePOWithoutPlus(Context context, String sss) {
        String valueIs = "";

        if(sss.toString().length() > 0){
            Log.e(TAG, "truee ");
            Boolean flag = Character.isDigit(sss.charAt(sss.length() - 1));
            Log.e(TAG, "cccccc "+flag);
            if(flag == true){
                String str = sss;
                StringBuilder sb = new StringBuilder();
                for (int i = str.length() - 1; i >= 0; i --) {
                    char c = str.charAt(i);
                    if (Character.isDigit(c)) {
                        sb.insert(0, c);
                    } else {
                        break;
                    }
                }
                String result = sb.toString();

                Log.e(TAG, "ccccccresult "+result);
                valueIs = context.getString(R.string.share_PurchaseOrder)+" # "+result;
            }
        }

        return valueIs;
    }

    public static String getRealValuePVWithoutPlus(Context context, String sss) {
        String valueIs = "";

        if(sss.toString().length() > 0){
            Log.e(TAG, "truee ");
            Boolean flag = Character.isDigit(sss.charAt(sss.length() - 1));
            Log.e(TAG, "cccccc "+flag);
            if(flag == true){
                String str = sss;
                StringBuilder sb = new StringBuilder();
                for (int i = str.length() - 1; i >= 0; i --) {
                    char c = str.charAt(i);
                    if (Character.isDigit(c)) {
                        sb.insert(0, c);
                    } else {
                        break;
                    }
                }
                String result = sb.toString();

                Log.e(TAG, "ccccccresult "+result);
                valueIs = context.getString(R.string.share_PaymentVoucher)+" # "+result;
            }
        }

        return valueIs;
    }




    public static boolean checkAndRequestPermissions(Activity context, int request) {
        int camera = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        int writeExternalStorage = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readExternalStorage = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
//        int coarseLocartion = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
//        int fineLocartion = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        int callPhone = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
        int readContacts = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (writeExternalStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (readExternalStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
//        if (coarseLocartion != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
//        }
//        if (fineLocartion != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
//        }
        if (callPhone != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }
        if (readContacts != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), request);
            return false;
        }
        return true;
    }


    public static boolean checkPermissionsCheck(Activity splashScreen) {
        if (ActivityCompat.checkSelfPermission(splashScreen, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(splashScreen, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(splashScreen, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(splashScreen, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(splashScreen, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(splashScreen, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(splashScreen, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                ) {
            return true;
        }
        return false;
    }


    public static boolean checkAdditionPermissionsCheck(final Activity splashScreen, String permissions[], int[] grantResults, final int REQUEST_ID_MULTIPLE_PERMISSIONS) {

        Map<String, Integer> perms = new HashMap<>();

        perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
//        perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
//        perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
        perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
        perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
        perms.put(Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);
        perms.put(Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);

        if (grantResults.length > 0) {
            for (int i = 0; i < permissions.length; i++)
                perms.put(permissions[i], grantResults[i]);
            // Check for both permissions
            if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
//                    perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
//                    perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    perms.get(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED &&
                    perms.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                    ) {
                //   Log.d(TAG, "Please allow access to all asked permissions. Due to the nature of the friendlywagon app, access to these areas on your mobile devices are necessary  for the app to function properly.");
                // process the normal flow
                //else any one or both the permissions are not granted
//                Toast.makeText(splashScreen, "CCCCCCCCCc", Toast.LENGTH_LONG)
//                        .show();
                return true;
            } else {
                //  Log.d(TAG, "Please allow access to all asked permissions. Due to the nature of the friendlywagon app, access to these areas on your mobile devices are necessary  for the app to function properly.");
                if (ActivityCompat.shouldShowRequestPermissionRationale(splashScreen, Manifest.permission.CAMERA) ||
//                        ActivityCompat.shouldShowRequestPermissionRationale(splashScreen, Manifest.permission.ACCESS_FINE_LOCATION) ||
//                        ActivityCompat.shouldShowRequestPermissionRationale(splashScreen, Manifest.permission.ACCESS_COARSE_LOCATION) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(splashScreen, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(splashScreen, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(splashScreen, Manifest.permission.CALL_PHONE) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(splashScreen, Manifest.permission.READ_CONTACTS)
                        ) {
//                    showDialogOK(splashScreen, "Please allow access to all asked permissions, access to your mobile devices are necessary for the app to function properly.",
                    showDialogOK(splashScreen, "Please allow access to all asked permissions.",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            checkAndRequestPermissions(splashScreen, REQUEST_ID_MULTIPLE_PERMISSIONS);
                                            break;
                                        case DialogInterface.BUTTON_NEGATIVE:
                                            break;
                                    }
                                }
                            });
                } else {
                    return true;
//                    Toast.makeText(splashScreen, "Go to settings and enable permissions", Toast.LENGTH_LONG)
//                            .show();
                }
            }
        } else {

        }


        return false;
    }


    private static void showDialogOK(Activity splashScreen, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(splashScreen)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("CANCEL", okListener)
                .create()
                .show();
    }




    public static void hideKeypad(Activity mainActivity) {
        if(mainActivity != null){
            InputMethodManager imm = (InputMethodManager) mainActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view = mainActivity.getCurrentFocus();
            if (view == null) {
                view = new View(mainActivity);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public static String getReplaceDollor(String sss) {
        String res = "";

        if(sss.contains("$$")){
            res = sss.replace("$$", "&#36;");
        }else{
            if(sss.contains("$")){
                res = sss.replace("$" , "&#36;");
            }else{
//                if(sss.contains(cruncycode)){
////                    res = getReplaceSingleCurrency(sss , cruncycode);
////                    res = res+""+cruncycode;
                    res = sss;
//                }
            }
        }
        return res;
    }

    public static String getReplaceSingleCurrency(String text, String cruncycode) {
        String res = "";
        if(text.contains(cruncycode)){
            res = text.replace(cruncycode, "");
        }else{
            res = text;
        }
        return res;
    }





    public static String getReplaceCurrency(String text, String cruncycode) {
        String res = "";
        if(text.contains(cruncycode)){
            res = text.replace(cruncycode, "");
        }else{
            res = text;
        }
        return getRemoveMinus(res).replace(",","").replace(" ","");
    }



    public static String getRemoveMinus(String sss) {
        String res = "";
        if(sss.contains("-")){
            res = sss.replace("-", "");
        }
        else{
            res = sss;
        }
        return res;
    }


    public static String getRemoveDoubleMinus(String sss) {
        String res = "";
        if(sss.contains("--")){
            res = sss.replace("--", "-");
        }
        else{
            res = sss;
        }
        return res;
    }


    public static String getReplaceMinus(String sss) {
        String res = "";
        if(sss.contains("--")){
            res = sss.replace("--", "-");
        }else{
            res = sss;
        }
        return res;
    }


    public static String isEmptyNull(String address) {
        if(address != null && !address.isEmpty() && !address.equalsIgnoreCase("null")) {
            return address;
        }else{
            return "";
        }
    }



    public static String getReplaceCurrencyAndPlus(String text, String cruncycode) {
        String res = "";
        if(text.contains(cruncycode)){
            res = text.replace(cruncycode, "");
        }else{
            res = text;
        }
        return getRemovePlus(res);
    }


    public static String getRemovePlus(String sss) {
        String res = "";
        if(sss.contains("+")){
            res = sss.replace("+", "");
        } else{
            res = sss;
        }
//        if(!res.equalsIgnoreCase("")){
//            res = "+"+res;
//        }
        return res;
    }


    public static String getRemoveSinglePlus(String sss) {
        String res = "";
        if(sss.contains("+")){
            res = sss.replace("+", "");
        } else{
            res = sss;
        }
//        if(!res.equalsIgnoreCase("")){
//            res = "+"+res;
//        }
        return res;
    }



    public static String getContainsReplaceCurrency(String text, String cruncycode) {
        String res = "";
        if(text.contains(cruncycode)){
            res = text.replace(cruncycode, "");
            res = res+cruncycode;
            if(res.contains("$")){
                res = res.replace("$", "&#36;");
            }else{
                res = res;
            }
        }else{
            res = text;
        }
        return res;
    }


    public static boolean isCompare(ArrayList<Product_list> product_bottom, String product_id) {
        boolean b = false;
        if(product_bottom.size() > 0){
            for(int i = 0; i < product_bottom.size() ; i++){
                if(product_bottom.get(i).getProduct_id().equalsIgnoreCase(product_id)){
                    b = true;
                }
            }
        }
        return  b;
    }

    public static File getFileLogo(Activity activity) {
        Bitmap icon = BitmapFactory.decodeResource(activity.getResources(), R.drawable.thanksimg);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File f = new File(Environment.getExternalStorageDirectory()
                + File.separator + "share.jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return f;
    }


    public static String getPatternFormat(String s, double value) {
        String defaultPattern = "0.0";

        if(s.equalsIgnoreCase("0")){
            NumberFormat nf = NumberFormat.getInstance(new Locale("en", "US"));
            nf.setMinimumFractionDigits(2);
            nf.setMaximumFractionDigits(2);
            defaultPattern = nf.format(value);
//            defaultPattern = "###,###,###,##0.00";
        }else if(s.equalsIgnoreCase("1")){
            NumberFormat nf = NumberFormat.getInstance(new Locale("en", "IN"));
            nf.setMinimumFractionDigits(2);
            nf.setMaximumFractionDigits(2);
            defaultPattern = nf.format(value);
//            defaultPattern = "##,##,##,##0.00";
        }else if(s.equalsIgnoreCase("2")){
            NumberFormat nf = NumberFormat.getInstance(new Locale("da", "DK"));
            nf.setMinimumFractionDigits(2);
            nf.setMaximumFractionDigits(2);
            defaultPattern = nf.format(value);
//            defaultPattern = "###.###.###.##0,00";
        }else if(s.equalsIgnoreCase("3")){
            NumberFormat nf = NumberFormat.getInstance(new Locale("sk", "SK"));
            nf.setMinimumFractionDigits(2);
            nf.setMaximumFractionDigits(2);
            defaultPattern = nf.format(value);
//            defaultPattern = "### ### ### ###,00";
        }

        return defaultPattern;
    }
    //    t E/Abc: savedPDFFile 12,345,678.00
//        2021-05-26 21:47:37.199 28287-28287/com.sirapp E/Abc: savedPDFFile 12 345 678,00
//        2021-05-26 21:47:37.215 28287-28287/com.sirapp E/Abc: savedPDFFile 12.345.678,00
//        2021-05-26 21:47:37.228 28287-28287/com.sirapp E/Abc: savedPDFFile 1,23,45,678.00









    public static String getRealValue(String sss, String withPlus) {
        String valueIs = "";
        if(sss.toString().length() > 0){

            // char cc = invoicenum.getText().toString().charAt(invoicenum.getText().toString().length() - 1);

            boolean gg = isNumeric(sss);
            Log.e(TAG, "gggggg "+gg);

            boolean dd = isChar(sss);
            Log.e(TAG, "dddddd "+dd);

            if(gg == false && dd == false){
                Log.e(TAG, "truee ");
                Boolean flag = Character.isDigit(sss.charAt(sss.length() - 1));
                Log.e(TAG, "cccccc "+flag);
                if(flag == true){

                    String str = sss;


                    StringBuilder sb = new StringBuilder();
                    for (int i = str.length() - 1; i >= 0; i --) {
                        char c = str.charAt(i);
                        if (Character.isDigit(c)) {
                            sb.insert(0, c);
                        } else {
                            break;
                        }
                    }
                    String result = sb.toString();

                    Log.e(TAG, "ccccccresult "+result);

                    String vvvvv = sss.substring(0, sss.length() - result.length());
                    Log.e(TAG, "ccccccvvvvv "+vvvvv);

                    int secondLength = result.length();
                    Log.e(TAG, "secondLength "+secondLength);

                    int wrappedValue = Integer.parseInt(result);
                    Log.e(TAG, "wrappedValue "+wrappedValue);

                    int XXXXX = String.valueOf(wrappedValue).length();
                    int XXXXXPlus = wrappedValue+1;
//                   int first =  String.valueOf(secondLength).length();
//                    int second = String.valueOf(wrappedValue).length();
//                    Log.e(TAG, "first "+first);
                    Log.e(TAG, "XXXXX "+XXXXX);

                    String vvvvvSS = sss.substring(0, sss.length() - XXXXX);
                    Log.e(TAG, "vvvvvSS "+vvvvvSS+XXXXXPlus);
                    valueIs = vvvvvSS+XXXXXPlus;
                }
            }else{
                boolean ddd = isChar(sss);
                if(ddd == false){
                    int myValue = Integer.parseInt(sss)+1;
                    valueIs = withPlus+" "+myValue;
                }
            }
        }
        return valueIs;
    }




    static String extractInt(String str)
    {
        // Replacing every non-digit number
        // with a space(" ")
        str = str.replaceAll("[^\\d]", " ");

        // Remove extra spaces from the beginning
        // and the ending of the string
        str = str.trim();

        // Replace all the consecutive white
        // spaces with a single space
        str = str.replaceAll(" +", " ");

        if (str.equals(""))
            return "-1";

        return str;
    }


    public static boolean isNumeric(String str)
    {
        for (char c : str.toCharArray())
        {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }


    public static boolean isChar(String str)
    {
        for (char c : str.toCharArray())
        {
            if (Character.isDigit(c)) return false;
        }
        return true;
    }



    public static ItemQuantity getQuantityByProductId(ArrayList<Product_list> product_bottom, String product_id) {
        ItemQuantity itemQuantity = new ItemQuantity();
        double en_quantity = 0.0;
        String category = "";
        if(product_bottom.size() > 0){
            for(int i = 0; i < product_bottom.size() ; i ++){
                Log.e(TAG, product_bottom.get(i).getProduct_id()+" ::: "+product_id+ " :::: "+product_bottom.get(i).getProduct_type());

                if(product_bottom.get(i).getProduct_type().equalsIgnoreCase("PRODUCT")){
                    if(product_bottom.get(i).getProduct_id().equalsIgnoreCase(product_id)){
                        Log.e(TAG, product_bottom.get(i).getProduct_id()+" ::: "+product_id);
                        try{
                            en_quantity = Double.parseDouble(product_bottom.get(i).getQuantity());
                            category = product_bottom.get(i).getProduct_type();

                            Log.e(TAG, "categoryQQQ "+category);

                            itemQuantity.setProduct_type(category);
                            itemQuantity.setEn_quantity(en_quantity);
                        }catch (Exception e){

                        }
                        break;
                    }else{

                    }
                }else {

                }

            }
        }
        return itemQuantity;
    }



    public static String DEFAULT_INVOICE = "Inv # 1";
    public static String DEFAULT_RECEIPT = "Rec # 1";
    public static String DEFAULT_ESTIMATE = "Est # 1";
    public static String DEFAULT_CN = "CRN # 1";
    public static String DEFAULT_DN = "DN # 1";
    public static String DEFAULT_PO = "PO # 1";
    public static String DEFAULT_PV = "PVN # 1";



    public static File getJPEGtoPNG(File fileimage) {
        Bitmap icon = BitmapFactory.decodeFile(String.valueOf(fileimage));
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File f = new File(Environment.getExternalStorageDirectory()
                + File.separator + "upload.jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //fileimage = f;
        Log.e(TAG, "fileimageBBBf "+f);
        return f;
    }


    public static File getJPEGtoPNGIssuer(File fileimage) {
        Bitmap icon = BitmapFactory.decodeFile(String.valueOf(fileimage));
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File f = new File(Environment.getExternalStorageDirectory()
                + File.separator + "upload1.jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //fileimage = f;
        Log.e(TAG, "fileimageBBBf "+f);
        return f;
    }

    public static File getJPEGtoPNGReceiver(File fileimage) {
        Bitmap icon = BitmapFactory.decodeFile(String.valueOf(fileimage));
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File f = new File(Environment.getExternalStorageDirectory()
                + File.separator + "upload2.jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //fileimage = f;
        Log.e(TAG, "fileimageBBBf "+f);
        return f;
    }

    public static File getJPEGtoPNGCompanySeal(File fileimage) {
        Bitmap icon = BitmapFactory.decodeFile(String.valueOf(fileimage));
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File f = new File(Environment.getExternalStorageDirectory()
                + File.separator + "upload3.jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //fileimage = f;
        Log.e(TAG, "fileimageBBBf "+f);
        return f;
    }



    public static File getJPEGtoPNGImage1(File fileimage) {
        Bitmap icon = BitmapFactory.decodeFile(String.valueOf(fileimage));
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        File f = new File(Environment.getExternalStorageDirectory()
                + File.separator + "image1.jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //fileimage = f;
        Log.e(TAG, "fileimageBBBf "+f);
        return f;
    }


    public static File getJPEGtoPNGImage2(File fileimage) {
        Bitmap icon = BitmapFactory.decodeFile(String.valueOf(fileimage));
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        File f = new File(Environment.getExternalStorageDirectory()
                + File.separator + "image2.jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //fileimage = f;
        Log.e(TAG, "fileimageBBBf "+f);
        return f;
    }

    public static File getJPEGtoPNGImage3(File fileimage) {
        Bitmap icon = BitmapFactory.decodeFile(String.valueOf(fileimage));
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        File f = new File(Environment.getExternalStorageDirectory()
                + File.separator + "image3.jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //fileimage = f;
        Log.e(TAG, "fileimageBBBf "+f);
        return f;
    }

    public static File getJPEGtoPNGImage4(File fileimage) {
        Bitmap icon = BitmapFactory.decodeFile(String.valueOf(fileimage));
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        File f = new File(Environment.getExternalStorageDirectory()
                + File.separator + "image4.jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //fileimage = f;
        Log.e(TAG, "fileimageBBBf "+f);
        return f;
    }

    public static File getJPEGtoPNGImage5(File fileimage) {
        Bitmap icon = BitmapFactory.decodeFile(String.valueOf(fileimage));
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        File f = new File(Environment.getExternalStorageDirectory()
                + File.separator + "image5.jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //fileimage = f;
        Log.e(TAG, "fileimageBBBf "+f);
        return f;
    }


    public static void glideSet(Context context, File fileimage, ImageView image) {
        GlideApp.with(context)
                .load(fileimage)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .apply(new RequestOptions().centerCrop().circleCrop().placeholder(R.drawable.app_icon))
                .placeholder(R.drawable.app_icon)
                .into(image);
    }



    public static void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteDirectory() {
        File mFile = new File(Environment.getExternalStorageDirectory() + "/Notes");
        try {
            deleteFolder(mFile);
        } catch (IOException e) {
            // Toast.makeText(Home_Activity.this, "Unable to delete folder", Toast.LENGTH_SHORT).show();
        }
    }


    public static void deleteFolder(File folder) throws IOException {
        if (folder.isDirectory()) {
            for (File ct : folder.listFiles()){
                deleteFolder(ct);
            }
        }
        if (!folder.delete()) {
            throw new FileNotFoundException("Unable to delete: " + folder);
        }
    }



    public ArrayList<ItemLeftMenu> getItemLeftMenuWithoutLoginUser(Activity activity) {
        ArrayList<ItemLeftMenu> arrayList = new ArrayList<ItemLeftMenu>();

        ItemLeftMenu vehicle1 = new ItemLeftMenu();
        vehicle1.setName(activity.getString(R.string.add_new_user));
        vehicle1.setIcon(R.drawable.add_new_user_2);
        arrayList.add(vehicle1);

        ItemLeftMenu vehicle11 = new ItemLeftMenu();
        vehicle11.setName(activity.getString(R.string.my_companies));
        vehicle11.setIcon(R.drawable.companies_2);
        arrayList.add(vehicle11);


        ItemLeftMenu vehicle2 = new ItemLeftMenu();
        vehicle2.setName(activity.getString(R.string.my_products));
        vehicle2.setIcon(R.drawable.products_2);
        arrayList.add(vehicle2);


        ItemLeftMenu vehicle22 = new ItemLeftMenu();
        vehicle22.setName(activity.getString(R.string.my_items));
        vehicle22.setIcon(R.drawable.items_2);
        arrayList.add(vehicle22);



        ItemLeftMenu vehicle221 = new ItemLeftMenu();
        vehicle221.setName(activity.getString(R.string.add_taxes));
        vehicle221.setIcon(R.drawable.tax_2);
        arrayList.add(vehicle221);



        ItemLeftMenu vehicle22s = new ItemLeftMenu();
        vehicle22s.setName(activity.getString(R.string.home_customers));
        vehicle22s.setIcon(R.drawable.customers_2);
        arrayList.add(vehicle22s);


        ItemLeftMenu vehicle221s = new ItemLeftMenu();
        vehicle221s.setName(activity.getString(R.string.suppliers));
        vehicle221s.setIcon(R.drawable.suppliers_2);
        arrayList.add(vehicle221s);

        ItemLeftMenu vehicle22s4 = new ItemLeftMenu();
        vehicle22s4.setName(activity.getString(R.string.stocks));
        vehicle22s4.setIcon(R.drawable.stocks_2);
        arrayList.add(vehicle22s4);

        ItemLeftMenu vehicle225s = new ItemLeftMenu();
        vehicle225s.setName(activity.getString(R.string.invoices));
        vehicle225s.setIcon(R.drawable.invoices_2);
        arrayList.add(vehicle225s);


        ItemLeftMenu vehicle2256s = new ItemLeftMenu();
        vehicle2256s.setName(activity.getString(R.string.estimate));
        vehicle2256s.setIcon(R.drawable.estimate_2);
        arrayList.add(vehicle2256s);

        ItemLeftMenu vehicle2258s = new ItemLeftMenu();
        vehicle2258s.setName(activity.getString(R.string.purchase_orders));
        vehicle2258s.setIcon(R.drawable.purchase_order_2);
        arrayList.add(vehicle2258s);

        ItemLeftMenu vehicle220s = new ItemLeftMenu();
        vehicle220s.setName(activity.getString(R.string.receipts));
        vehicle220s.setIcon(R.drawable.recepits_2);
        arrayList.add(vehicle220s);


        ItemLeftMenu vehicle2210s = new ItemLeftMenu();
        vehicle2210s.setName(activity.getString(R.string.payment_vouchers));
        vehicle2210s.setIcon(R.drawable.payment_vocher_2);
        arrayList.add(vehicle2210s);

        ItemLeftMenu vehicle2320s = new ItemLeftMenu();
        vehicle2320s.setName(activity.getString(R.string.reports));
        vehicle2320s.setIcon(R.drawable.reports_2);
        arrayList.add(vehicle2320s);

        ItemLeftMenu vehicle2270s = new ItemLeftMenu();
        vehicle2270s.setName(activity.getString(R.string.credit_notes));
        vehicle2270s.setIcon(R.drawable.credit_notes_2);
        arrayList.add(vehicle2270s);


        ItemLeftMenu vehicle80s = new ItemLeftMenu();
        vehicle80s.setName(activity.getString(R.string.debit_notes));
        vehicle80s.setIcon(R.drawable.debit_note_2);
        arrayList.add(vehicle80s);

        ItemLeftMenu vehicle8870s = new ItemLeftMenu();
        vehicle8870s.setName(activity.getString(R.string.thank_you_note));
        vehicle8870s.setIcon(R.drawable.thanku_note_2);
        arrayList.add(vehicle8870s);

        ItemLeftMenu vehicle00s = new ItemLeftMenu();
        vehicle00s.setName(activity.getString(R.string.invoice_reminders));
        vehicle00s.setIcon(R.drawable.invoice_reminder_2);
        arrayList.add(vehicle00s);


//        ItemLeftMenu vehicle00sss = new ItemLeftMenu();
//        vehicle00sss.setName("");
//       // vehicle00sss.setIcon(R.drawable.invoice_reminder_2);
//        arrayList.add(vehicle00sss);

        ItemLeftMenu vehicle110s = new ItemLeftMenu();
        vehicle110s.setName(activity.getString(R.string.settings));
        vehicle110s.setIcon(R.drawable.settings_2);
        arrayList.add(vehicle110s);

        ItemLeftMenu vehicle00ds = new ItemLeftMenu();
        vehicle00ds.setName(activity.getString(R.string.logout));
        vehicle00ds.setIcon(R.drawable.logout_2);
        arrayList.add(vehicle00ds);

        return arrayList;
    }



    public static String getDensityName(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        if (density >= 4.0) {
            return "xxxhdpi";
        }
        if (density >= 3.0) {
            return "xxhdpi";
        }
        if (density >= 2.0) {
            return "xhdpi";
        }
        if (density >= 1.5) {
            return "hdpi";
        }
        if (density >= 1.0) {
            return "mdpi";
        }
        return "ldpi";
    }



    public static int getScale(Context context, WebView webView){
        int PIC_WIDTH= webView.getRight()-webView.getLeft();
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        Double val = new Double(width)/new Double(PIC_WIDTH);
        val = val * 100d;
        return val.intValue();
    }


    public static boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }
}
