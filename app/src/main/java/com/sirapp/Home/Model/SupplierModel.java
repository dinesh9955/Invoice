
package com.sirapp.Home.Model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class SupplierModel {

    @SerializedName("address")
    private String mAddress;
    @SerializedName("company_id")
    private String mCompanyId;
    @SerializedName("contact_name")
    private String mContactName;
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
    @SerializedName("status")
    private String mStatus;
    @SerializedName("supplier_id")
    private String mSupplierId;
    @SerializedName("supplier_name")
    private String mSupplierName;
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

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getSupplierId() {
        return mSupplierId;
    }

    public void setSupplierId(String supplierId) {
        mSupplierId = supplierId;
    }

    public String getSupplierName() {
        return mSupplierName;
    }

    public void setSupplierName(String supplierName) {
        mSupplierName = supplierName;
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

    public String getSupplier_image_path() {
        return supplier_image_path;
    }

    public void setSupplier_image_path(String supplier_image_path) {
        this.supplier_image_path = supplier_image_path;
    }

    private String supplier_image_path;

}
