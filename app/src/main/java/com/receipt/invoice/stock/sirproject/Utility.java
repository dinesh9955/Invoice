package com.receipt.invoice.stock.sirproject;

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

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
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
                res = sss;
            }
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
        return getRemoveMinus(res);
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
        if(sss.contains("++")){
            res = sss.replace("+", "");
        }
        else{
            res = sss;
        }
        return res;
    }


}
