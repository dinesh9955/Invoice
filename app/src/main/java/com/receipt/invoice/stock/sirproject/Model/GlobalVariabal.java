package com.receipt.invoice.stock.sirproject.Model;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.multidex.MultiDex;
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
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}