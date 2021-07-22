package com.sirapp.API;

import android.content.Context;
import android.content.SharedPreferences;

public class SavePref {

    public static final String TAG = "SavePref";
    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private static SavePref _instance;
    public static final String PREF_TOKEN = "TehBolaDriver";
    public static final String PREF_TOKEN_DATA = "TehBolaDriver";

    public SavePref(Context c) {
        context = c;
        preferences = context.getSharedPreferences(PREF_TOKEN, 0);
        editor = preferences.edit();
    }

    public SharedPreferences getSharedPrefrences() {
        return preferences;
    }

    public void removeKey(String key) {

        editor.remove(key).commit();
    }

    public static SavePref getInstance(Context context) {
        if (_instance == null) {
            _instance = new SavePref(context);
        }
        return _instance;
    }

    public void setString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key, String def_value) {
        return preferences.getString(key, def_value);
    }

    public void setDEVICE_TOKEN(String device_token) {
        editor.putString("device_token", device_token);
        editor.commit();
    }

    public String getDEVICE_TOKEN() {
        String device_token = preferences.getString("device_token", "");
        return device_token;
    }

    public void setLast_name(String last_name) {
        editor.putString("last_name", last_name);
        editor.commit();
    }

    public String getLast_name() {
        String last_name = preferences.getString("last_name", "");
        return last_name;

    }

    public void setFirebaseToken(String FirebaseToken) {
        editor.putString("FirebaseToken", FirebaseToken);
        editor.commit();

    }

    public String getFirebaseToken() {

        String FirebaseToken = preferences.getString("FirebaseToken", "");
        return FirebaseToken;
    }

    public void setBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key, boolean def_value) {
        return preferences.getBoolean(key, def_value);
    }


    public void setIslogout(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getIslogout(String key, boolean def_value) {

        return preferences.getBoolean(key, def_value);
    }

    public void setEmailLogin(String EmailLogin) {
        editor.putString("EmailLogin", EmailLogin);
        editor.commit();
    }

    public String getEmailLogin() {
        String EmailLogin = preferences.getString("EmailLogin", "");
        return EmailLogin;
    }

    public void setPassword(String password) {
        editor.putString("password", password);
        editor.commit();
    }

    public void setLat(String latitude) {
        editor.putString("latitude", latitude);
        editor.commit();
    }

    public String getLat() {
        String latitude = preferences.getString("latitude", "");
        return latitude;
    }

    public void setLong(String longitude) {
        editor.putString("longitude", longitude);
        editor.commit();
    }

    public String getLong() {
        String longitude = preferences.getString("longitude", "");
        return longitude;
    }

    public String getPassword() {
        String password = preferences.getString("password", "");
        return password;
    }

    public void setPhoto(String photo) {
        editor.putString("photo", photo);
        editor.commit();
    }

    public String getPhoto() {
        String photo = preferences.getString("photo", "");
        return photo;
    }

    public void setFirstName(String firstName) {
        editor.putString("firstName", firstName);
        editor.commit();
    }

    public String getFirstName() {
        String firstName = preferences.getString("firstName", "");
        return firstName;
    }

    public void setFrontImage(String frontImage) {
        editor.putString("frontImage", frontImage);
        editor.commit();
    }

    public String getFrontImage() {
        String frontImage = preferences.getString("frontImage", "");
        return frontImage;
    }


    public void setID(String id) {
        editor.putString("id", id);
        editor.commit();
    }

    public String getID() {
        String id = preferences.getString("id", "");
        return id;
    }


    public void setName(String name) {
        editor.putString("name", name);
        editor.commit();
    }

    public String getName() {
        String name = preferences.getString("name", "");
        return name;
    }

    public void setPhone(String phone) {
        editor.putString("phone", phone);
        editor.commit();
    }

    public String getPhone() {
        String phone = preferences.getString("phone", "");
        return phone;
    }

    public void setCountryCode(String CountryCode) {
        editor.putString("CountryCode", CountryCode);
        editor.commit();
    }

    public String getCountryCode() {
        String CountryCode = preferences.getString("CountryCode", "");
        return CountryCode;
    }

    public void setProfileImage(String profileImage) {
        editor.putString("profileImage", profileImage);
        editor.commit();
    }

    public String getProfileImage() {
        String profileImage = preferences.getString("profileImage", "");
        return profileImage;
    }

    public void setType(String type) {
        editor.putString("type", type);
        editor.commit();
    }

    public String getType() {
        String type = preferences.getString("type", "");
        return type;
    }
    public void setAddress(String Address) {
        editor.putString("Address", Address);
        editor.commit();
    }

    public String getAddress() {
        String Address = preferences.getString("Address", "");
        return Address;
    }
    public void setCompanyId(String CompanyId) {
        editor.putString("CompanyId", CompanyId);
        editor.commit();
    }

    public String getCompanyId() {
        String CompanyId = preferences.getString("CompanyId", "");
        return CompanyId;
    }

    public void setLatitude(String latitude) {
        editor.putString("latitude", latitude);
        editor.commit();
    }

    public String getlatitude() {
        String latitude = preferences.getString("latitude", "");
        return latitude;

    }

    public void setlongitude(String longitute) {
        editor.putString("longitute", longitute);
        editor.commit();
    }

    public String getlongitute() {
        String longitute = preferences.getString("longitute", "");
        return longitute;

    }

    public void setDriverOnline(String driver_online) {
        editor.putString("driver_online", driver_online);
        editor.commit();
    }

    public String getDriverOnline() {
        String driver_online = preferences.getString("driver_online", "");
        return driver_online;

    }

    public void setAssigned_Vehicle_Id(String assigned_vehicle_id) {
        editor.putString("assigned_vehicle_id", assigned_vehicle_id);
        editor.commit();
    }

    public String getAssigned_Vehicle_Id() {
        String assigned_vehicle_id = preferences.getString("assigned_vehicle_id", "");
        return assigned_vehicle_id;
    }

    public void setAuthorization_key(String access_token) {
        editor.putString("access_token", access_token);
        editor.commit();
    }

    public String getAuthorization_key() {
        String access_token = preferences.getString("access_token", "");
        return access_token;
    }

    public static void setDeviceToken(Context mContext, String key, String value) {
        SharedPreferences sharedpreferences = mContext.getSharedPreferences(PREF_TOKEN_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getDeviceToken(Context mContext, String key) {
        SharedPreferences preferences = mContext.getSharedPreferences(PREF_TOKEN_DATA, Context.MODE_PRIVATE);
        String stringvalue = preferences.getString(key, "");
        return stringvalue;
    }

    public void clearPreferences() {

        preferences.edit().clear().commit();
    }
}
