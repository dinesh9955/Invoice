package com.receipt.invoice.stock.sirproject.Model;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.appsflyer.AppsFlyer2dXConversionCallback;
import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GlobalVariabal  extends Application {


    private static final String TAG = "GlobalVariabal";

    public static String getCurencycode() {
        return Curencycode;
    }

    public static void setCurencycode(String curencycode) {
        Curencycode = curencycode;
    }

    private  static String Curencycode;

    public static String getPercnetagetax() {
        return percnetagetax;
    }

    public static void setPercnetagetax(String percnetagetax) {
        GlobalVariabal.percnetagetax = percnetagetax;
    }

    public static String getAponttaxt() {
        return aponttaxt;
    }

    public static void setAponttaxt(String aponttaxt) {
        GlobalVariabal.aponttaxt = aponttaxt;
    }

    public static String getTaxtedittxt() {
        return taxtedittxt;
    }

    public static void setTaxtedittxt(String taxtedittxt) {
        GlobalVariabal.taxtedittxt = taxtedittxt;
    }

    private  static String percnetagetax=null;
    private  static String aponttaxt=null;
    private  static String taxtedittxt=null;

    public ArrayList<Itemproductselect> getItemList() {
        return ItemList;
    }

    public void setItemList(ArrayList<Itemproductselect> itemList) {
        ItemList = itemList;
    }

    private ArrayList<Itemproductselect> ItemList;

    public ArrayList<String> getProducttotalprice() {
        return producttotalprice;
    }

    public void setProducttotalprice(ArrayList<String> producttotalprice) {
        this.producttotalprice = producttotalprice;
    }

    ArrayList<String> producttotalprice= new ArrayList<>();


    public String getTemplatevariable() {
        return templatevariable;
    }

    public void setTemplatevariable(String templatevariable) {
        this.templatevariable = templatevariable;
    }

    private  String  templatevariable;



    private static final String AF_DEV_KEY = "yLJo3QQZNawuqif7hLtW3Q";
    FirebaseCrashlytics crashlytics;
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            crashlytics = FirebaseCrashlytics.getInstance();
            crashlytics.log("Start logging!");
            crashlytics.setCustomKey("DeviceName", "Android" );
            crashlytics.setCustomKey("Email", "dinesh.kumar@apptunix.com");
        }catch (Exception e) {
            Log.e(TAG , "getMessage3 "+e.getMessage());
        }





//        AppsFlyerConversionListener conversionListener = new AppsFlyerConversionListener() {
//            @Override
//            public void onConversionDataSuccess(Map<String, Object> conversionData) {
//
//                for (String attrName : conversionData.keySet()) {
//                    Log.d("LOG_TAG", "attribute: " + attrName + " = " + conversionData.get(attrName));
//                }
//            }
//
//            @Override
//            public void onConversionDataFail(String errorMessage) {
//                Log.d("LOG_TAG", "error getting conversion data: " + errorMessage);
//            }
//
//            @Override
//            public void onAppOpenAttribution(Map<String, String> attributionData) {
//                for (String attrName : attributionData.keySet()) {
//                    Log.d("LOG_TAG", "attribute: " + attrName + " = " + attributionData.get(attrName));
//                }
//            }
//
//            @Override
//            public void onAttributionFailure(String errorMessage) {
//                Log.d("LOG_TAG", "error onAttributionFailure : " + errorMessage);
//            }
//        };
//
//        AppsFlyerLib.getInstance().init(AF_DEV_KEY, conversionListener, this);
//        AppsFlyerLib.getInstance().setDebugLog(true);
//        AppsFlyerLib.getInstance().start(this);
//        AppsFlyerConversionListener conversionDataListener =
//                new AppsFlyerConversionListener() {
//
//                    @Override
//                    public void onInstallConversionDataLoaded(Map<String, String> conversionData) {
//
//                    }
//
//                    @Override
//                    public void onInstallConversionFailure(String errorMessage) {
//                        Log.d(AppsFlyerLib.LOG_TAG, "error getting conversion data: " + errorMessage);
//                    }
//
//                    @Override
//                    public void onAppOpenAttribution(Map<String, String> attributionData) {
//                        Log.d(AppsFlyerLib.LOG_TAG, "DEEP LINK WORKING");
//                    }
//
//                    @Override
//                    public void onAttributionFailure(String errorMessage) {
//                        Log.d(AppsFlyerLib.LOG_TAG, "error onAttributionFailure : " + errorMessage);
//                    }
//                };


        AppsFlyerConversionListener appsFlyerConversionListener = new AppsFlyerConversionListener(){

            @Override
            public void onConversionDataSuccess(Map<String, Object> map) {
                Log.e(TAG, "onConversionDataSuccess " +  map.toString());
            }

            @Override
            public void onConversionDataFail(String s) {
                Log.e(TAG, "onConversionDataFail " + s);
            }

            @Override
            public void onAppOpenAttribution(Map<String, String> map) {
                Log.e(TAG, "onAppOpenAttribution " + map.toString());

            }

            @Override
            public void onAttributionFailure(String s) {
                Log.e(TAG, "onAttributionFailure " + s);

            }
        };

        AppsFlyerLib.getInstance().init(AF_DEV_KEY, appsFlyerConversionListener, this);
        AppsFlyerLib.getInstance().startTracking(this);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}