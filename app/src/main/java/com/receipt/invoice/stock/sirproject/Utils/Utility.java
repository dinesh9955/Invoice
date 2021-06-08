package com.receipt.invoice.stock.sirproject.Utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;

import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.receipt.invoice.stock.sirproject.Model.ItemQuantity;
import com.receipt.invoice.stock.sirproject.Model.Product_list;
import com.receipt.invoice.stock.sirproject.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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


    public static String getRealValueInvoiceWithoutPlus(String sss) {
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
                valueIs = "Invoice # "+result;
            }
        }

        return valueIs;
    }

    public static String getRealValueEstimateWithoutPlus(String sss) {
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
                valueIs = "Estimate # "+result;
            }
        }

        return valueIs;
    }


    public static String getRealValueReceiptWithoutPlus(String sss) {
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
                valueIs = "Receipt # "+result;
            }
        }

        return valueIs;
    }


    public static String getRealValueCreditNoteWithoutPlus(String sss) {
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
                valueIs = "Credit Note # "+result;
            }
        }

        return valueIs;
    }


    public static String getRealValueDebitNoteWithoutPlus(String sss) {
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
                valueIs = "Debit Note # "+result;
            }
        }

        return valueIs;
    }


    public static String getRealValuePOWithoutPlus(String sss) {
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
                valueIs = "Purchase Order # "+result;
            }
        }

        return valueIs;
    }


    public static String getRealValuePVWithoutPlus(String sss) {
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
                valueIs = "Payment Voucher # "+result;
            }
        }

        return valueIs;
    }




    public static boolean checkAndRequestPermissions(Activity context, int request) {
//        int camera = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        int writeExternalStorage = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readExternalStorage = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
//        int coarseLocartion = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
//        int fineLocartion = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
//        int callPhone = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
//        int readContacts = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS);

        List<String> listPermissionsNeeded = new ArrayList<>();

//        if (camera != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.CAMERA);
//        }
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
//        if (callPhone != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
//        }
//        if (readContacts != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
//        }

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
        perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
        perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
        perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
        perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
        perms.put(Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);
        perms.put(Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);

        if (grantResults.length > 0) {
            for (int i = 0; i < permissions.length; i++)
                perms.put(permissions[i], grantResults[i]);
            // Check for both permissions
            if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
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
                        ActivityCompat.shouldShowRequestPermissionRationale(splashScreen, Manifest.permission.ACCESS_FINE_LOCATION) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(splashScreen, Manifest.permission.ACCESS_COARSE_LOCATION) ||
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
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }




    public static void hideKeypad(Activity mainActivity) {
        InputMethodManager imm = (InputMethodManager) mainActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = mainActivity.getCurrentFocus();
        if (view == null) {
            view = new View(mainActivity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


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
        Bitmap icon = BitmapFactory.decodeResource(activity.getResources(),
                R.drawable.thanksimg);
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
            defaultPattern = nf.format(value);
//            defaultPattern = "###,###,###,##0.00";
        }else if(s.equalsIgnoreCase("1")){
            NumberFormat nf = NumberFormat.getInstance(new Locale("en", "IN"));
            nf.setMinimumFractionDigits(2);
            defaultPattern = nf.format(value);
//            defaultPattern = "##,##,##,##0.00";
        }else if(s.equalsIgnoreCase("2")){
            NumberFormat nf = NumberFormat.getInstance(new Locale("da", "DK"));
            nf.setMinimumFractionDigits(2);
            defaultPattern = nf.format(value);
//            defaultPattern = "###.###.###.##0,00";
        }else if(s.equalsIgnoreCase("3")){
            NumberFormat nf = NumberFormat.getInstance(new Locale("sk", "SK"));
            nf.setMinimumFractionDigits(2);
            defaultPattern = nf.format(value);
//            defaultPattern = "### ### ### ###,00";
        }

        return defaultPattern;
    }
    //    t E/Abc: savedPDFFile 12,345,678.00
//        2021-05-26 21:47:37.199 28287-28287/com.receipt.invoice.stock.sirproject E/Abc: savedPDFFile 12 345 678,00
//        2021-05-26 21:47:37.215 28287-28287/com.receipt.invoice.stock.sirproject E/Abc: savedPDFFile 12.345.678,00
//        2021-05-26 21:47:37.228 28287-28287/com.receipt.invoice.stock.sirproject E/Abc: savedPDFFile 1,23,45,678.00









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
}
