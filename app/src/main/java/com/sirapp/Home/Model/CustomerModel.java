
package com.sirapp.Home.Model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class CustomerModel {

    @SerializedName("address")
    private String mAddress;
    @SerializedName("company_id")
    private String mCompanyId;
    @SerializedName("contact_name")
    private String mContactName;
    @SerializedName("customer_id")
    private String mCustomerId;
    @SerializedName("customer_name")
    private String mCustomerName;
    @SerializedName("date_added")
    private String mDateAdded;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("image")
    private Object mImage;
    @SerializedName("mobile_number")
    private String mMobileNumber;
    @SerializedName("phone_number")
    private String mPhoneNumber;
    @SerializedName("shipping_address_1")
    private String mShippingAddress1;
    @SerializedName("shipping_address_2")
    private String mShippingAddress2;
    @SerializedName("shipping_city")
    private String mShippingCity;
    @SerializedName("shipping_country")
    private String mShippingCountry;
    @SerializedName("shipping_firstname")
    private String mShippingFirstname;
    @SerializedName("shipping_lastname")
    private String mShippingLastname;
    @SerializedName("shipping_postcode")
    private String mShippingPostcode;
    @SerializedName("shipping_zone")
    private Object mShippingZone;
    @SerializedName("status")
    private String mStatus;
    @SerializedName("user_id")
    private String mUserId;
    @SerializedName("website")
    private String mWebsite;

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getCompanyId() {
        return mCompanyId;
    }

    public void setCompanyId(String companyId) {
        mCompanyId = companyId;
    }

    public String getContactName() {
        return mContactName;
    }

    public void setContactName(String contactName) {
        mContactName = contactName;
    }

    public String getCustomerId() {
        return mCustomerId;
    }

    public void setCustomerId(String customerId) {
        mCustomerId = customerId;
    }

    public String getCustomerName() {
        return mCustomerName;
    }

    public void setCustomerName(String customerName) {
        mCustomerName = customerName;
    }

    public String getDateAdded() {
        return mDateAdded;
    }

    public void setDateAdded(String dateAdded) {
        mDateAdded = dateAdded;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public Object getImage() {
        return mImage;
    }

    public void setImage(Object image) {
        mImage = image;
    }

    public String getMobileNumber() {
        return mMobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        mMobileNumber = mobileNumber;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    public String getShippingAddress1() {
        return mShippingAddress1;
    }

    public void setShippingAddress1(String shippingAddress1) {
        mShippingAddress1 = shippingAddress1;
    }

    public String getShippingAddress2() {
        return mShippingAddress2;
    }

    public void setShippingAddress2(String shippingAddress2) {
        mShippingAddress2 = shippingAddress2;
    }

    public String getShippingCity() {
        return mShippingCity;
    }

    public void setShippingCity(String shippingCity) {
        mShippingCity = shippingCity;
    }

    public String getShippingCountry() {
        return mShippingCountry;
    }

    public void setShippingCountry(String shippingCountry) {
        mShippingCountry = shippingCountry;
    }

    public String getShippingFirstname() {
        return mShippingFirstname;
    }

    public void setShippingFirstname(String shippingFirstname) {
        mShippingFirstname = shippingFirstname;
    }

    public String getShippingLastname() {
        return mShippingLastname;
    }

    public void setShippingLastname(String shippingLastname) {
        mShippingLastname = shippingLastname;
    }

    public String getShippingPostcode() {
        return mShippingPostcode;
    }

    public void setShippingPostcode(String shippingPostcode) {
        mShippingPostcode = shippingPostcode;
    }

    public Object getShippingZone() {
        return mShippingZone;
    }

    public void setShippingZone(Object shippingZone) {
        mShippingZone = shippingZone;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public String getWebsite() {
        return mWebsite;
    }

    public void setWebsite(String website) {
        mWebsite = website;
    }

    public String getCustomer_image_path() {
        return customer_image_path;
    }

    public void setCustomer_image_path(String customer_image_path) {
        this.customer_image_path = customer_image_path;
    }

    private String customer_image_path;

}
